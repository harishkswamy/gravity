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

import gravity.Component;
import gravity.ComponentCallback;
import gravity.Location;
import gravity.ProxyableComponent;

/**
 * This is the default component implementation. This implementation delegates instance creation to
 * {@link gravity.impl.ComponentFactory ComponentFactory}. The idea behind the delegation is to
 * separate the component interface and implementation so this component can reuse the
 * implementation and strategy of another component. When the implementation is reused, the
 * component is simply one facet of the implementation.
 * 
 * @author Harish Krishnaswamy
 * @version $Id: DefaultComponent.java,v 1.11 2004-06-14 04:23:45 harishkswamy Exp $
 */
public class DefaultComponent implements ProxyableComponent
{
    private ComponentKey     _key;
    private ComponentFactory _factory;
    private Location         _registrationLocation;
    private Location         _retrievalLocation;

    public DefaultComponent(ComponentKey compKey)
    {
        _key = compKey;

        _factory = new ComponentFactory();
    }

    public Object getInstance()
    {
        return _factory.getInstance(this);
    }

    public void registerImplementation(Component comp)
    {
        ProxyableComponent pComp = (ProxyableComponent) comp;

        _factory = (ComponentFactory) pComp.getImplementation();
    }

    public void registerImplementation(Class compClass, Object[] ctorArgs,
        ComponentCallback[] callbacks)
    {
        _factory.registerImplementation(compClass, ctorArgs, callbacks);
    }

    public void registerFactory(Object compFac, String facMethodName, Object[] facMethodArgs,
        ComponentCallback[] callbacks)
    {
        _factory.registerFactoryDelegate(compFac, facMethodName, facMethodArgs, callbacks);
    }

    public void registerConstructorArguments(Object[] args)
    {
        _factory.registerConstructorArguments(args);
    }

    public void registerCallbacks(ComponentCallback[] callbacks)
    {
        _factory.registerCallbacks(callbacks);
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

    public Object getImplementation()
    {
        return _factory;
    }

    public boolean isInDispatchingState()
    {
        return _factory.isInDispatchingState();
    }

    public void wrapStrategyWithSingleton()
    {
        _factory.wrapStrategyWithSingleton();
    }

    public void wrapStrategyWithPooling()
    {
        _factory.wrapStrategyWithPooling();
    }

    public void wrapStrategyWithThreadLocal()
    {
        _factory.wrapStrategyWithThreadLocal();
    }

    public Object getConcreteInstance()
    {
        return _factory.getConcreteInstance(this);
    }

    public Object newInstance()
    {
        return _factory.newInstance(this);
    }

    // End - Construct new instance ================================================================

    public void collectInstance(Object inst)
    {
        _factory.collectInstance(inst);
    }

    public String toString()
    {
        return "[Key: " + _key + ", Component Factory: " + _factory + ", Registration Location: "
            + _registrationLocation + ", Last Retrieval Location: " + _retrievalLocation + "]";
    }
}