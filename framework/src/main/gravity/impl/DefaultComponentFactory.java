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

import gravity.ComponentProxyFactory;
import gravity.LazyComponentFactory;
import gravity.Location;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Harish Krishnaswamy
 * @version $Id: DefaultComponentFactory.java,v 1.1 2004-05-10 17:28:56 harishkswamy Exp $
 */
public class DefaultComponentFactory implements LazyComponentFactory
{
    private ComponentKey         _componentKey;
    private Location             _componentRetrievalLocation;
    private Class                _componentClass;
    private Object[]             _componentConstructorArgs;
    private Map                  _componentSetterArgs;
    private Location             _componentRegistrationLocation;
    private LazyComponentFactory _decoratorComponentFactory;
    private List                 _componentProxies = new ArrayList();

    public DefaultComponentFactory(ComponentKey compKey)
    {
        _componentKey = compKey;

        decorated(this);
    }

    public void registerComponentImplementation(Class compClass, Object[] ctorArgs, Map setrArgs)
    {
        _componentClass = compClass;
        _componentConstructorArgs = ctorArgs;
        _componentSetterArgs = setrArgs;
    }

    public void registerComponentRegistrationLocation(Location location)
    {
        _componentRegistrationLocation = location;
    }

    public void registerComponentRetrievalLocation(Location location)
    {
        _componentRetrievalLocation = location;
    }

    public Object getComponentInstance()
    {
        ComponentProxyFactory proxyFac = ComponentProxyFactoryAgent.getComponentProxyFactory();

        Object proxy = proxyFac.newComponentProxy(_componentKey.getComponentInterface(),
            _decoratorComponentFactory);

        synchronized (_componentProxies)
        {
            _componentProxies.add(proxy);
        }

        return proxy;
    }

    public void proxyRealized(Object proxy)
    {
        if (proxy == null)
            return;

        synchronized (_componentProxies)
        {
            for (Iterator itr = _componentProxies.iterator(); itr.hasNext();)
            {
                if (itr.next() == proxy)
                {
                    itr.remove();

                    return;
                }
            }
        }

    }

    public Object getConcreteComponentInstance()
    {
        return ComponentBuilder.build(_componentKey, _componentRetrievalLocation, _componentClass,
            _componentConstructorArgs, _componentSetterArgs, _componentRegistrationLocation);
    }

    public Object getConcreteComponentInstance(Object proxy)
    {
        Object comp = getConcreteComponentInstance();

        proxyRealized(proxy);

        return comp;
    }

    public void decorated(LazyComponentFactory decoratorCompFac)
    {
        _decoratorComponentFactory = decoratorCompFac;

        ComponentProxyFactory proxyFac = ComponentProxyFactoryAgent.getComponentProxyFactory();

        synchronized (_componentProxies)
        {
            for (Iterator itr = _componentProxies.iterator(); itr.hasNext();)
                proxyFac.registerComponentFactory(itr.next(), decoratorCompFac);
        }
    }
    
    public void returnComponentInstance(Object comp)
    {
        // This implementation simply ignores the returned component. This is primarily for a
        // PooledComponentFactory to reclaim the returned component.
    }

    public String toString()
    {
        return "[Component: " + _componentKey + ", Component Class: " + _componentClass + "]";
    }
}