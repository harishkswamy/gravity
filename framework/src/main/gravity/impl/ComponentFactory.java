// Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package gravity.impl;

import gravity.ComponentCallback;
import gravity.ComponentPhase;
import gravity.ComponentProxy;
import gravity.ComponentStrategy;
import gravity.Container;
import gravity.Gravity;
import gravity.RealizableComponent;
import gravity.UsageException;
import gravity.WrapperException;
import gravity.util.Message;
import gravity.util.ReflectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The component factory is responsible for producing component instances. The factory uses the
 * {@link gravity.ComponentStrategy}registered with it to produce instances. The strategy will come
 * back to this factory for concrete instances in the event one is needed.
 * <p>
 * Concrete instances are produced either from the registered implementation or the factory
 * delegate. When neither is registered, an exception is thrown, when both are registered, the
 * implementation is used.
 * <p>
 * The ComponentFactory may be shared by multiple components, i.e. when components act as facets of
 * the same implementation, they all share the same component factory.
 * 
 * @author Harish Krishnaswamy
 * @version $Id: ComponentFactory.java,v 1.4 2004-09-02 04:02:51 harishkswamy Exp $
 */
public class ComponentFactory
{
    private Class               _implementation;
    private Object[]            _constructorArgs;
    private ComponentCallback[] _callbacks;
    private ComponentStrategy   _componentStrategy;
    private Object              _factoryDelegate;
    private String              _factoryMethodName;
    private Object[]            _factoryMethodArgs;

    public ComponentFactory()
    {
        _componentStrategy = new LazyLoadingComponentStrategy(null);
    }

    public void registerImplementation(Class compClass, Object[] ctorArgs,
        ComponentCallback[] callbacks)
    {
        _implementation = compClass;

        registerConstructorArguments(ctorArgs);

        registerCallbacks(callbacks);
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
            List methods = new ArrayList(Arrays.asList(_callbacks));

            methods.addAll(Arrays.asList(callbacks));

            _callbacks = (ComponentCallback[]) methods.toArray(new ComponentCallback[0]);
        }
    }

    /**
     * @return Returns a proxy instance for the provided component.
     */
    public Object getInstance(RealizableComponent comp)
    {
        ComponentProxy proxy = ComponentProxyFactory.getInstance().getComponentProxy();

        return proxy.newInstance(comp);
    }

    public boolean isInDispatchingState()
    {
        return _componentStrategy.isDispatching();
    }

    private ComponentStrategy getStrategyToDecorate()
    {
        if (_componentStrategy.getClass() == LazyLoadingComponentStrategy.class)
            return null;

        return _componentStrategy;
    }

    protected SingletonComponentStrategy newSingletonStrategy(ComponentStrategy strategy)
    {
        return new SingletonComponentStrategy(strategy);
    }

    public void wrapStrategyWithSingleton()
    {
        // If already in singleton state, do nothing
        if (_componentStrategy instanceof SingletonComponentStrategy)
            return;

        _componentStrategy = newSingletonStrategy(getStrategyToDecorate());
    }

    protected PoolingComponentStrategy newPoolingStrategy(ComponentStrategy strategy)
    {
        return new PoolingComponentStrategy(strategy);
    }

    public void wrapStrategyWithPooling()
    {
        // If already in pooling state, do nothing
        if (_componentStrategy instanceof PoolingComponentStrategy)
            return;

        _componentStrategy = newPoolingStrategy(getStrategyToDecorate());
    }

    protected ThreadLocalComponentStrategy newThreadLocalStrategy(ComponentStrategy strategy)
    {
        return new ThreadLocalComponentStrategy(strategy);
    }

    public void wrapStrategyWithThreadLocal()
    {
        // If already in thread local state, do nothing
        if (_componentStrategy instanceof ThreadLocalComponentStrategy)
            return;

        _componentStrategy = newThreadLocalStrategy(getStrategyToDecorate());
    }

    public Object getConcreteInstance(RealizableComponent comp)
    {
        return _componentStrategy.getComponentInstance(comp);
    }

    // Construct new instance ======================================================================

    private void realizeKeys(Object[] args)
    {
        if (args == null)
            return;

        Container container = Gravity.getInstance().getContainer();

        for (int i = 0; i < args.length; i++)
        {
            if (args[i] instanceof ComponentKey)
                args[i] = container.getComponentInstance(args[i]);
        }
    }

    private void invokeCallbacks(Object instance, ComponentPhase compPhase)
    {
        for (int i = 0; i < _callbacks.length; i++)
        {
            ComponentCallback callback = _callbacks[i];
            ComponentPhase phase = callback.getComponentPhase();

            if (compPhase.equals(phase))
            {
                realizeKeys(callback.getArguments());
                ReflectUtils.invokeMethod(instance, callback.getName(), callback.getArguments());
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
        Object enhInst = DynamicWeaverFactory.getDynamicWeaver().weave(instance);

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
    private Object constructViaComboInjection()
    {
        realizeKeys(_constructorArgs);

        Object instance = ReflectUtils.invokeConstructor(_implementation, _constructorArgs);

        return initializeComponent(instance);
    }

    /**
     * Delegates the instance creation to the factory delegate and then initializes it.
     */
    private Object constructViaFactoryDelegate()
    {
        realizeKeys(_factoryMethodArgs);

        Object instance = ReflectUtils.invokeMethod(_factoryDelegate, _factoryMethodName,
            _factoryMethodArgs);

        return initializeComponent(instance);
    }

    /**
     * Builds a component via constructor and/or setter injection.
     * <p>
     * When the supplied array of constructor arguments is null, this method will use the setter
     * injection strategy, otherwise it uses the constructor injection strategy.
     * <p>
     * When both the array of constructor arguments and the map of setter properties are supplied,
     * this method will do a combo injection (constructor injection + setter injection).
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
            throw new UsageException(Message.COMPONENT_IMPLEMENTATION_NOT_REGISTERED, comp);

        try
        {
            Object instance;

            if (_implementation == null)
                instance = constructViaFactoryDelegate();

            else
                instance = constructViaComboInjection();

            return instance;
        }
        catch (Exception e)
        {
            throw WrapperException.wrap(e, Message.CANNOT_CONSTRUCT_COMPONENT_INSTANCE, comp);
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
            return "[Class: " + _implementation + ", Strategy:" + _componentStrategy + "]";

        return "[Delegate: " + _factoryDelegate + ", Factory Method: " + _factoryMethodName
            + ", Strategy:" + _componentStrategy + "]";
    }
}