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

import gravity.ComponentState;
import gravity.Location;
import gravity.ProxyableComponent;
import gravity.UsageException;
import gravity.WrapperException;
import gravity.util.ClassUtils;
import gravity.util.ReflectUtils;

import java.util.Iterator;
import java.util.Map;

/**
 * @author Harish Krishnaswamy
 * @version $Id: DefaultComponent.java,v 1.4 2004-05-18 21:29:35 harishkswamy Exp $
 */
public class DefaultComponent implements ProxyableComponent
{
    private ComponentKey   _key;
    private Location       _retrievalLocation;
    private Class          _implementation;
    private Object[]       _constructorDependencies;
    private Map            _methodDependencies;
    private Location       _registrationLocation;
    private ComponentState _componentState;

    public DefaultComponent(ComponentKey compKey)
    {
        _key = compKey;

        _componentState = new LazyLoadingComponentState(null, this);
    }

    public Object getInstance()
    {
        return _componentState.getComponentInstance();
    }

    public void registerImplementation(Class compClass, Object[] ctorDeps, Map setrDeps)
    {
        _implementation = compClass;
        _constructorDependencies = ctorDeps;
        _methodDependencies = setrDeps;
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
        return _componentState.isDispatching();
    }

    private ComponentState getStateToDecorate()
    {
        if (_componentState.getClass() == LazyLoadingComponentState.class)
            return null;
        else
            return _componentState;
    }

    protected SingletonComponentState newSingletonState(ComponentState state,
        ProxyableComponent comp)
    {
        return new SingletonComponentState(state, comp);
    }

    public void wrapStateWithSingleton()
    {
        // If already in singleton state, do nothing
        if (_componentState instanceof SingletonComponentState)
            return;

        _componentState = newSingletonState(getStateToDecorate(), this);
    }

    protected PoolingComponentState newPoolingState(ComponentState state, ProxyableComponent comp)
    {
        return new PoolingComponentState(state, comp);
    }

    public void wrapStateWithPooling()
    {
        // If already in pooling state, do nothing
        if (_componentState instanceof PoolingComponentState)
            return;

        _componentState = newPoolingState(getStateToDecorate(), this);
    }

    protected ThreadLocalComponentState newThreadLocalState(ComponentState state,
        ProxyableComponent comp)
    {
        return new ThreadLocalComponentState(state, comp);
    }

    public void wrapStateWithThreadLocal()
    {
        // If already in thread local state, do nothing
        if (_componentState instanceof ThreadLocalComponentState)
            return;

        _componentState = newThreadLocalState(getStateToDecorate(), this);
    }

    public Object getConcreteInstance()
    {
        return _componentState.getConcreteComponentInstance();
    }

    // Construct new instance ======================================================================

    private void setDependencies(Object inst, Map methodDeps)
    {
        if (methodDeps == null)
            return;

        for (Iterator itr = methodDeps.keySet().iterator(); itr.hasNext();)
        {
            String methodName = (String) itr.next();

            ReflectUtils.invokeMethod(inst, methodName, methodDeps.get(methodName));
        }
    }

    /**
     * Gives {@link gravity.DynamicWeaver}an opportunity to weave the supplied object and then it
     * sets any dependencies via the setter methods with the provided property name/value map.
     */
    private Object initializeService(Object inst, Map methodDeps)
    {
        // This is the hook to let cross-cutting concerns be weaved into the component.
        Object enhInst = DynamicWeaverFactory.getDynamicWeaver().weave(inst);

        setDependencies(enhInst, methodDeps);

        return enhInst;
    }

    /**
     * Builds a service via constructor injection.
     * <p>
     * Instantiates the service from the supplied class and arguments and initializes it.
     * 
     * @return Fully constructed and initialized service.
     */
    private Object constructViaComboInjection(Class implClass, Object[] ctorDeps, Map methodDeps)
    {
        Object instance = ReflectUtils.invokeConstructor(implClass, ctorDeps);

        return initializeService(instance, methodDeps);
    }

    /**
     * Builds a service via setter injection.
     * <p>
     * Instantiates the service from the supplied class and initializes it.
     * 
     * @return Fully constructed and initialized service.
     */
    private Object constructViaMethodInjection(Class implClass, Map methodDeps)
    {
        Object instance = ClassUtils.newInstance(implClass);

        return initializeService(instance, methodDeps);
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
                instance = constructViaMethodInjection(_implementation, _methodDependencies);

            else
                instance = constructViaComboInjection(_implementation, _constructorDependencies,
                    _methodDependencies);

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
        _componentState.collectComponentInstance(inst);
    }

    public String toString()
    {
        return "[Key: " + _key + ", Implementation: " + _implementation + "]";
    }
}