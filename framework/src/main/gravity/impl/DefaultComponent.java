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
 * @version $Id: DefaultComponent.java,v 1.3 2004-05-18 20:52:02 harishkswamy Exp $
 */
public class DefaultComponent implements ProxyableComponent
{
    private ComponentKey   _key;
    private Location       _retrievalLocation;
    private Class          _implementation;
    private Object[]       _constructorArgs;
    private Map            _setterArgs;
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

    public synchronized void registerImplementation(Class compClass, Object[] ctorArgs, Map setrArgs)
    {
        _implementation = compClass;
        _constructorArgs = ctorArgs;
        _setterArgs = setrArgs;
    }

    public synchronized void setRegistrationLocation(Location location)
    {
        _registrationLocation = location;
    }

    public synchronized void setRetrievalLocation(Location location)
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

    public synchronized void wrapStateWithSingleton()
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

    public synchronized void wrapStateWithPooling()
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

    public synchronized void wrapStateWithThreadLocal()
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

    private void setDependencies(Object impl, Map implSetrArgs)
    {
        if (implSetrArgs == null)
            return;

        for (Iterator itr = implSetrArgs.keySet().iterator(); itr.hasNext();)
        {
            String propertyName = (String) itr.next();

            ReflectUtils.invokeSetter(impl, propertyName, implSetrArgs.get(propertyName));
        }
    }

    /**
     * Gives {@link gravity.DynamicWeaver}an opportunity to weave the supplied object and then it
     * sets any dependencies via the setter methods with the provided property name/value map.
     */
    private Object initializeService(Object impl, Map implSetrArgs)
    {
        // This is the hook to let cross-cutting concerns be weaved into the component.
        Object enhImpl = DynamicWeaverFactory.getDynamicWeaver().weave(impl);

        setDependencies(enhImpl, implSetrArgs);

        return enhImpl;
    }

    /**
     * Builds a service via constructor injection.
     * <p>
     * Instantiates the service from the supplied class and arguments and initializes it.
     * 
     * @return Fully constructed and initialized service.
     */
    private Object constructViaComboInjection(Class implClass, Object[] implCtorArgs,
        Map implSetrArgs)
    {
        Object impl = ReflectUtils.invokeConstructor(implClass, implCtorArgs);

        return initializeService(impl, implSetrArgs);
    }

    /**
     * Builds a service via setter injection.
     * <p>
     * Instantiates the service from the supplied class and initializes it.
     * 
     * @return Fully constructed and initialized service.
     */
    private Object constructViaSetterInjection(Class implClass, Map implSetrArgs)
    {
        Object impl = ClassUtils.newInstance(implClass);

        return initializeService(impl, implSetrArgs);
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
            Object impl;

            if (_constructorArgs == null)
                impl = constructViaSetterInjection(_implementation, _setterArgs);

            else
                impl = constructViaComboInjection(_implementation, _constructorArgs, _setterArgs);

            return impl;
        }
        catch (Exception e)
        {
            throw WrapperException.wrap(e, "Unable to construct new instance for component: "
                + this + " at location: " + _registrationLocation);
        }
    }

    // End - Construct new instance ================================================================

    public void collectInstance(Object comp)
    {
        _componentState.collectComponentInstance(comp);
    }

    public String toString()
    {
        return "[Key: " + _key + ", Implementation: " + _implementation + "]";
    }
}