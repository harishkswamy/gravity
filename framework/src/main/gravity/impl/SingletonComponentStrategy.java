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

import gravity.ComponentStrategy;
import gravity.ProxyableComponent;

/**
 * @author Harish Krishnaswamy
 * @version $Id: SingletonComponentStrategy.java,v 1.1 2004-05-22 20:19:35 harishkswamy Exp $
 */
public class SingletonComponentStrategy extends LazyLoadingComponentStrategy
{
    private Object _componentInstance;

    public SingletonComponentStrategy(ComponentStrategy delegate, ProxyableComponent component)
    {
        super(delegate, component);
    }

    private synchronized void cacheComponent(Object componentInstance)
    {
        if (_componentInstance == null)
            _componentInstance = componentInstance;
    }

    public Object getConcreteComponentInstance()
    {
        if (_componentInstance == null)
            cacheComponent(super.getConcreteComponentInstance());

        return _componentInstance;
    }

    public String toString()
    {
        return "[Singleton: " + super.toString() + "]";
    }
}