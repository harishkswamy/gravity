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
import gravity.ComponentStrategy;
import gravity.Location;
import gravity.ProxyableComponent;
import gravity.UsageException;
import gravity.WrapperException;
import gravity.util.ClassUtils;
import gravity.util.ReflectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is a flyweight that will be shared by all its proxy instances.
 * 
 * @author Harish Krishnaswamy
 * @version $Id: DefaultComponent.java,v 1.10 2004-05-29 16:54:07 harishkswamy Exp $
 */
public class DefaultComponent implements ProxyableComponent
{
    private ComponentKey        _key;
    private Location            _retrievalLocation;
    private Class               _implementation;
    private Object[]            _constructorArgs;
    private ComponentCallback[] _callbacks;
    private Location            _registrationLocation;
    private ComponentStrategy   _componentStrategy;

    public DefaultComponent(ComponentKey compKey)
    {
        _key = compKey;

        _componentStrategy = new LazyLoadingComponentStrategy(null, this);
    }

    public Object getInstance()
    {
        return _componentStrategy.getComponentInstance();
    }

    public void registerImplementation(Class compClass, Object[] ctorArgs,
        ComponentCallback[] callbacks)
    {
        _implementation = compClass;

        registerConstructorArguments(ctorArgs);

        registerCallbacks(callbacks);
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

    public void setRegistrationLocation(Location location)
    {
        _registrationLocation = location;
    }

    public void setRetrievalLocation(Location location)
    {
        _retrievalLocation = location;
    }

    public Class getInterface()
    {
        return _key.getComponentInterface();
    }

    public boolean isInDispatchingState()
    {
        return _componentStrategy.isDispatching();
    }

    private ComponentStrategy getStrategyToDecorate()
    {
        if (_componentStrategy.getClass() == LazyLoadingComponentStrategy.class)
            return null;
        else
            return _componentStrategy;
    }

    protected SingletonComponentStrategy newSingletonStrategy(ComponentStrategy strategy,
        ProxyableComponent comp)
    {
        return new SingletonComponentStrategy(strategy, comp);
    }

    public void wrapStrategyWithSingleton()
    {
        // If already in singleton state, do nothing
        if (_componentStrategy instanceof SingletonComponentStrategy)
            return;

        _componentStrategy = newSingletonStrategy(getStrategyToDecorate(), this);
    }

    protected PoolingComponentStrategy newPoolingStrategy(ComponentStrategy strategy,
        ProxyableComponent comp)
    {
        return new PoolingComponentStrategy(strategy, comp);
    }

    public void wrapStrategyWithPooling()
    {
        // If already in pooling state, do nothing
        if (_componentStrategy instanceof PoolingComponentStrategy)
            return;

        _componentStrategy = newPoolingStrategy(getStrategyToDecorate(), this);
    }

    protected ThreadLocalComponentStrategy newThreadLocalStrategy(ComponentStrategy strategy,
        ProxyableComponent comp)
    {
        return new ThreadLocalComponentStrategy(strategy, comp);
    }

    public void wrapStrategyWithThreadLocal()
    {
        // If already in thread local state, do nothing
        if (_componentStrategy instanceof ThreadLocalComponentStrategy)
            return;

        _componentStrategy = newThreadLocalStrategy(getStrategyToDecorate(), this);
    }

    public Object getConcreteInstance()
    {
        return _componentStrategy.getConcreteComponentInstance();
    }

    // Construct new instance ======================================================================

    private void invokeInitializationCallbacks(Object instance)
    {
        if (_callbacks == null)
            return;

        for (int i = 0; i < _callbacks.length; i++)
        {
            ComponentCallback callback = _callbacks[i];
            ComponentPhase phase = callback.getComponentPhase();

            if (phase == ComponentPhase.INJECTION || phase == ComponentPhase.START_UP)
            {
                ReflectUtils.invokeMethod(instance, callback.getName(), callback.getArguments());
            }
        }
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
        Object instance = ReflectUtils.invokeConstructor(_implementation, _constructorArgs);

        return initializeComponent(instance);
    }

    /**
     * Builds a component via setter injection.
     * <p>
     * Instantiates the component from the supplied class and initializes it.
     * 
     * @return Fully constructed and initialized component.
     */
    private Object constructViaMethodInjection()
    {
        Object instance = ClassUtils.newInstance(_implementation);

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
     *         When the supplied implementation class is null.
     * @throws WrapperException
     *         When there is any problem while building the component.
     * @see gravity.DynamicWeaver#weave(Object)
     */
    public Object newInstance()
    {
        if (_implementation == null)
            throw new UsageException("Implementation not registered for component: " + this);

        try
        {
            Object instance;

            if (_constructorArgs == null)
                instance = constructViaMethodInjection();

            else
                instance = constructViaComboInjection();

            return instance;
        }
        catch (Exception e)
        {
            throw WrapperException.wrap(e, "Unable to construct new instance for component: "
                + this);
        }
    }

    // End - Construct new instance ================================================================

    public void collectInstance(Object inst)
    {
        _componentStrategy.collectComponentInstance(inst);
    }

    public String toString()
    {
        return "[Key: " + _key + ", Implementation: " + _implementation
            + ", Registration Location: " + _registrationLocation + ", Last Retrieval Location: "
            + _retrievalLocation + "]";
    }
}