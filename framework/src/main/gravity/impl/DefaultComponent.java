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
 * @version $Id: DefaultComponent.java,v 1.8 2004-05-27 05:32:27 harishkswamy Exp $
 */
public class DefaultComponent implements ProxyableComponent
{
    private ComponentKey        _key;
    private Location            _retrievalLocation;
    private Class               _implementation;
    private Object[]            _constructorDependencies;
    private ComponentCallback[] _callbackMethods;
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

    public void registerImplementation(Class compClass, Object[] ctorDeps,
        ComponentCallback[] callbacks)
    {
        _implementation = compClass;
        _constructorDependencies = ctorDeps;
        _callbackMethods = callbacks;
    }

    public void setRegistrationLocation(Location location)
    {
        _registrationLocation = location;
    }

    public void registerConstructorArguments(Object[] args)
    {
        if (_constructorDependencies == null)
            _constructorDependencies = args;

        else
        {
            List deps = new ArrayList(Arrays.asList(_constructorDependencies));

            deps.addAll(Arrays.asList(args));

            _constructorDependencies = deps.toArray();
        }
    }

    public void registerCallbackMethods(ComponentCallback[] callbacks)
    {
        if (_callbackMethods == null)
            _callbackMethods = callbacks;

        else
        {
            List methods = new ArrayList(Arrays.asList(_callbackMethods));

            methods.addAll(Arrays.asList(callbacks));

            _callbackMethods = (ComponentCallback[]) methods.toArray(new ComponentCallback[0]);
        }
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

    private void invokeInitializationMethods(Object instance)
    {
        if (_callbackMethods == null)
            return;

        for (int i = 0; i < _callbackMethods.length; i++)
        {
            ComponentCallback method = _callbackMethods[i];
            ComponentPhase phase = method.getComponentPhase();

            if (phase == ComponentPhase.INJECTION || phase == ComponentPhase.START_UP)
            {
                ReflectUtils.invokeMethod(instance, method.getName(), method.getArguments());
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

        invokeInitializationMethods(enhInst);

        return enhInst;
    }

    /**
     * Builds a service via constructor injection.
     * <p>
     * Instantiates the service from the supplied class and arguments and initializes it.
     * 
     * @return Fully constructed and initialized service.
     */
    private Object constructViaComboInjection()
    {
        Object instance = ReflectUtils.invokeConstructor(_implementation, _constructorDependencies);

        return initializeComponent(instance);
    }

    /**
     * Builds a service via setter injection.
     * <p>
     * Instantiates the service from the supplied class and initializes it.
     * 
     * @return Fully constructed and initialized service.
     */
    private Object constructViaMethodInjection()
    {
        Object instance = ClassUtils.newInstance(_implementation);

        return initializeComponent(instance);
    }

    /**
     * Builds a service via constructor and/or setter injection.
     * <p>
     * When the supplied array of constructor arguments is null, this method will use the setter
     * injection strategy, otherwise it uses the constructor injection strategy.
     * <p>
     * When both the array of constructor arguments and the map of setter properties are supplied,
     * this method will do a combo injection (constructor injection + setter injection).
     * 
     * @return Fully constructed and initialized service.
     * @throws UsageException
     *         When the supplied implementation class is null.
     * @throws WrapperException
     *         When there is any problem while building the service.
     * @see gravity.DynamicWeaver#weave(Object)
     */
    public Object newInstance()
    {
        // TODO make this a passive aggressive check
        if (_implementation == null)
            throw new UsageException("Implementation not registered for component: " + this
                + " at location: " + _retrievalLocation);

        try
        {
            Object instance;

            if (_constructorDependencies == null)
                instance = constructViaMethodInjection();

            else
                instance = constructViaComboInjection();

            return instance;
        }
        catch (Exception e)
        {
            throw WrapperException.wrap(e, "Unable to construct new instance for component: "
                + this + " at location: " + _registrationLocation);
        }
    }

    // End - Construct new instance ================================================================

    public void collectInstance(Object inst)
    {
        _componentStrategy.collectComponentInstance(inst);
    }

    public String toString()
    {
        return "[Key: " + _key + ", Implementation: " + _implementation + "]";
    }
}