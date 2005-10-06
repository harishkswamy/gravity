// Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package gravity.impl;

import gravity.ComponentCallback;
import gravity.ComponentInstanceBuilder;
import gravity.ComponentPhase;
import gravity.ComponentProxy;
import gravity.ComponentStrategy;
import gravity.ComponentStrategyType;
import gravity.Context;
import gravity.DynamicWeaver;
import gravity.RealizableComponent;
import gravity.UsageException;
import gravity.util.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This builder is responsible for producing component instances. The builder uses the
 * {@link gravity.ComponentStrategy}registered with it to produce instances. The strategy will come
 * back to this builder for concrete instances in the event one is needed.
 * <p>
 * Concrete instances are produced either from the registered implementation or the factory
 * delegate. When neither is registered, an exception is thrown, when both are registered, the
 * instance is produced from the implementation.
 * <p>
 * The DefaultComponentInstanceBuilder may be shared by multiple components, i.e. when components
 * act as facets of the same implementation, they all share the same instance builder.
 * 
 * @author Harish Krishnaswamy
 * @version $Id: DefaultComponentInstanceBuilder.java,v 1.2 2005-10-06 21:59:27 harishkswamy Exp $
 */
public final class DefaultComponentInstanceBuilder implements ComponentInstanceBuilder
{
    private Context             _context;
    private Class               _implementation;
    private Object[]            _constructorArgs;
    private ComponentCallback[] _callbacks;
    private ComponentStrategy   _componentStrategy;
    private Object              _factoryDelegate;
    private String              _factoryMethodName;
    private Object[]            _factoryMethodArgs;

    public void initialize(Context context)
    {
        _context = context;

        _componentStrategy = (ComponentStrategy) context.newApiInstance(ComponentStrategy.class);
        _componentStrategy.initialize(context, null);
    }

    public void registerImplementation(Class compClass, Object[] ctorArgs,
        ComponentCallback[] callbacks)
    {
        _implementation = compClass;
        _constructorArgs = ctorArgs;
        _callbacks = callbacks;
    }

    public void registerFactoryDelegate(Object factory, String factoryMethodName,
        Object[] factoryMethodArgs, ComponentCallback[] callbacks)
    {
        _factoryDelegate = factory;
        _factoryMethodName = factoryMethodName;
        _factoryMethodArgs = factoryMethodArgs;
        _callbacks = callbacks;
    }

    public void registerConstructorArguments(Object[] args)
    {
        if (args == null)
            return;

        if (_constructorArgs == null)
            _constructorArgs = args;

        else
        {
            List deps = new ArrayList(Arrays.asList(_constructorArgs));

            deps.addAll(Arrays.asList(args));

            _constructorArgs = deps.toArray();
        }
    }

    public void registerCallbacks(ComponentCallback[] callbacks)
    {
        if (callbacks == null)
            return;

        if (_callbacks == null)
            _callbacks = callbacks;

        else
        {
            List tCallbacks = new ArrayList(Arrays.asList(_callbacks));

            tCallbacks.addAll(Arrays.asList(callbacks));

            _callbacks = (ComponentCallback[]) tCallbacks.toArray(new ComponentCallback[tCallbacks.size()]);
        }
    }

    /**
     * @return Returns a proxy instance for the provided component.
     */
    public Object getInstance(RealizableComponent comp)
    {
        // PERF Startup performance improvement buffer
        // The ComponentProxy object can be created once and cached here to improve startup
        // performance at the cost of memory. The proxy object is only needed at the time of
        // generating a component instance and that is typically done during startup.
        ComponentProxy proxy = (ComponentProxy) _context.newApiInstance(ComponentProxy.class);
        proxy.initialize(_context);

        return proxy.newInstance(comp);
    }

    public boolean isInDispatchingState()
    {
        return _componentStrategy.isDispatching();
    }

    public void wrapStrategy(ComponentStrategyType strategyType)
    {
        // If already of the same type, do nothing
        if (strategyType.isTypeOf(_componentStrategy))
            return;

        _componentStrategy = strategyType.newInstance(_context, _componentStrategy);
    }

    public Object getConcreteInstance(RealizableComponent comp)
    {
        return _componentStrategy.getComponentInstance(comp);
    }

    // Construct new instance ======================================================================

    private void invokeCallbacks(Object instance, ComponentPhase compPhase)
    {
        for (int i = 0; i < _callbacks.length; i++)
        {
            ComponentCallback callback = _callbacks[i];
            ComponentPhase phase = callback.getComponentPhase();

            if (compPhase.equals(phase))
            {
                _context.getMutableContainer().realizeKeys(callback.getArguments());

                _context.getReflectUtils().invokeMethod(instance, callback.getName(),
                    callback.getArguments());
            }
        }
    }

    /**
     * This method will invoke all injection callbacks followed by all startup callbacks.
     */
    private void invokeInitializationCallbacks(Object instance)
    {
        if (_callbacks == null)
            return;

        invokeCallbacks(instance, ComponentPhase.INJECTION);
        invokeCallbacks(instance, ComponentPhase.START_UP);
    }

    /**
     * Gives {@link gravity.DynamicWeaver}an opportunity to weave the supplied object and then it
     * sets any dependencies via the setter methods with the provided property name/value map.
     */
    private Object initializeComponent(Object instance)
    {
        // This is the hook to let cross-cutting concerns be weaved into the component.
        DynamicWeaver weaver = (DynamicWeaver) _context.newApiInstance(DynamicWeaver.class);

        Object enhInst = weaver.weave(instance);

        invokeInitializationCallbacks(enhInst);

        return enhInst;
    }

    /**
     * Builds a component via constructor injection.
     * <p>
     * Instantiates the component from the supplied class and arguments and initializes it.
     * 
     * @return Fully constructed and initialized component.
     */
    private Object constructViaConstructor()
    {
        _context.getMutableContainer().realizeKeys(_constructorArgs);

        Object instance = _context.getReflectUtils().invokeConstructor(_implementation,
            _constructorArgs);

        return initializeComponent(instance);
    }

    /**
     * Delegates the instance creation to the factory delegate and then initializes it.
     */
    private Object constructViaFactoryDelegate()
    {
        _context.getMutableContainer().realizeKeys(_factoryMethodArgs);

        Object instance = _context.getReflectUtils().invokeMethod(_factoryDelegate,
            _factoryMethodName, _factoryMethodArgs);

        return initializeComponent(instance);
    }

    /**
     * Builds a component via constructor and/or setter injection.
     * <p>
     * When the supplied array of constructor arguments is null, this method will use the setter
     * injection strategy, otherwise it uses the constructor injection strategy.
     * <p>
     * When both the array of constructor arguments and the map of setter properties are supplied,
     * this method will do a combo injection (constructor injection + method injection).
     * 
     * @return Fully constructed and initialized component.
     * @throws UsageException
     *             When the supplied implementation class is null.
     * @throws WrapperException
     *             When there is any problem while building the component.
     * @see gravity.DynamicWeaver#weave(Object)
     * @see gravity.ComponentCallback
     * @see gravity.ComponentPhase
     */
    public Object newInstance(RealizableComponent comp)
    {
        if (_implementation == null && _factoryDelegate == null)
            throw _context.getExceptionWrapper().wrap(new UsageException(),
                Message.COMPONENT_IMPLEMENTATION_NOT_REGISTERED, comp);

        try
        {
            Object instance;

            if (_implementation == null)
                instance = constructViaFactoryDelegate();

            else
                instance = constructViaConstructor();

            return instance;
        }
        catch (Exception e)
        {
            throw _context.getExceptionWrapper().wrap(e,
                Message.CANNOT_CONSTRUCT_COMPONENT_INSTANCE, comp);
        }
    }

    // End - Construct new instance ================================================================

    public void collectInstance(Object inst)
    {
        _componentStrategy.collectComponentInstance(inst);
    }

    public String toString()
    {
        if (_factoryDelegate == null)
            return "[Component Implementation: " + _implementation + ", Strategy:"
                + _componentStrategy + "]";

        return "[Component Factory: " + _factoryDelegate + ", Factory Method: "
            + _factoryMethodName + ", Strategy:" + _componentStrategy + "]";
    }
}