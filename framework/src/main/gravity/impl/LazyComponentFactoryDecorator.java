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

import gravity.LazyComponentFactory;
import gravity.Location;

import java.util.Map;

/**
 * @author Harish Krishnaswamy
 * @version $Id: LazyComponentFactoryDecorator.java,v 1.1 2004-05-10 17:28:55 harishkswamy Exp $
 */
public class LazyComponentFactoryDecorator implements LazyComponentFactory
{
    private LazyComponentFactory _delegate;

    protected LazyComponentFactoryDecorator(LazyComponentFactory delegate)
    {
        _delegate = delegate;

        _delegate.decorated(this);
    }

    public void proxyRealized(Object proxy)
    {
        _delegate.proxyRealized(proxy);
    }

    public Object getConcreteComponentInstance()
    {
        return _delegate.getConcreteComponentInstance();
    }

    public Object getConcreteComponentInstance(Object proxy)
    {
        return _delegate.getConcreteComponentInstance(proxy);
    }

    public void decorated(LazyComponentFactory decoratorCompFac)
    {
        _delegate.decorated(decoratorCompFac);
    }

    public Object getComponentInstance()
    {
        return _delegate.getComponentInstance();
    }

    public void registerComponentImplementation(Class compClass, Object[] compCtorArgs,
        Map compSetrArgs)
    {
        _delegate.registerComponentImplementation(compClass, compCtorArgs, compSetrArgs);
    }

    public void registerComponentRegistrationLocation(Location location)
    {
        _delegate.registerComponentRegistrationLocation(location);
    }

    public void registerComponentRetrievalLocation(Location location)
    {
        _delegate.registerComponentRetrievalLocation(location);
    }

    public void returnComponentInstance(Object comp)
    {
        _delegate.returnComponentInstance(comp);
    }

    public String toString()
    {
        return _delegate.toString();
    }
}