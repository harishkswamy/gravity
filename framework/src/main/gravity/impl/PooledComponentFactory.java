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
import gravity.util.Pool;

/**
 * @author Harish Krishnaswamy
 * @version $Id: PooledComponentFactory.java,v 1.1 2004-05-10 17:28:57 harishkswamy Exp $
 */
public class PooledComponentFactory extends LazyComponentFactoryDecorator
{
    private Pool _pool;

    public PooledComponentFactory(LazyComponentFactory delegate, int poolSize)
    {
        super(delegate);

        _pool = new Pool(poolSize);
    }

    public PooledComponentFactory(LazyComponentFactory delegate)
    {
        this(delegate, 0);
    }

    private Object obtainComponentInstance()
    {
        Object component = super.getConcreteComponentInstance();

        _pool.loaned(component);

        return component;
    }

    public Object getConcreteComponentInstance(Object proxy)
    {
        Object component = _pool.loan();

        if (component == null)
            component = obtainComponentInstance();

        proxyRealized(proxy);

        return component;
    }

    public Object getConcreteComponentInstance()
    {
        return getConcreteComponentInstance(null);
    }

    public void returnComponentInstance(Object comp)
    {
        _pool.collect(comp);
    }
}