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
import gravity.ComponentInstanceBuilder;
import gravity.ComponentStrategyType;
import gravity.Context;
import gravity.Location;
import gravity.RealizableComponent;

/**
 * This is the default component implementation. This implementation delegates instance creation to
 * {@link gravity.impl.DefaultComponentInstanceBuilder DefaultComponentInstanceBuilder}. The idea
 * behind the delegation is to separate the component interface and implementation so this component
 * can reuse the implementation and strategy of another component. When the implementation is
 * reused, the component is simply one facet of the implementation.
 * 
 * @author Harish Krishnaswamy
 * @version $Id: DefaultComponent.java,v 1.14 2004-11-17 19:45:45 harishkswamy Exp $
 */
public final class DefaultComponent implements RealizableComponent
{
    private ComponentKey             _key;
    private ComponentInstanceBuilder _instanceBuilder;
    private Location                 _registrationLocation;
    private Location                 _retrievalLocation;

    public DefaultComponent(Context context, ComponentKey compKey)
    {
        _key = compKey;

        Object[] args = new Object[]{context};

        _instanceBuilder = (ComponentInstanceBuilder) context.newApiInstance(
            ComponentInstanceBuilder.class.getName(), args);
    }

    public Object getInstance()
    {
        return _instanceBuilder.getInstance(this);
    }

    public void registerImplementation(Component comp)
    {
        _instanceBuilder = (ComponentInstanceBuilder) comp.getFactory();
    }

    public void registerImplementation(Class compClass, Object[] ctorArgs,
        ComponentCallback[] callbacks)
    {
        _instanceBuilder.registerImplementation(compClass, ctorArgs, callbacks);
    }

    public void registerFactory(Object compFac, String facMethodName, Object[] facMethodArgs,
        ComponentCallback[] callbacks)
    {
        _instanceBuilder.registerFactoryDelegate(compFac, facMethodName, facMethodArgs, callbacks);
    }

    public void registerConstructorArguments(Object[] args)
    {
        _instanceBuilder.registerConstructorArguments(args);
    }

    public void registerCallbacks(ComponentCallback[] callbacks)
    {
        _instanceBuilder.registerCallbacks(callbacks);
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

    public Object getFactory()
    {
        return _instanceBuilder;
    }

    public boolean isInDispatchingState()
    {
        return _instanceBuilder.isInDispatchingState();
    }

    public void wrapStrategy(ComponentStrategyType strategyType)
    {
        _instanceBuilder.wrapStrategy(strategyType);
    }

    public Object getConcreteInstance()
    {
        return _instanceBuilder.getConcreteInstance(this);
    }

    public Object newInstance()
    {
        return _instanceBuilder.newInstance(this);
    }

    public void collectInstance(Object inst)
    {
        _instanceBuilder.collectInstance(inst);
    }

    public String toString()
    {
        return "[Key: " + _key + ", Instance Builder: " + _instanceBuilder
            + ", Registration Location: " + _registrationLocation + ", Last Retrieval Location: "
            + _retrievalLocation + "]";
    }
}