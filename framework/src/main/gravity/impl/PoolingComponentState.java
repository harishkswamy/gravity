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
import gravity.ComponentState;
import gravity.util.Pool;

/**
 * @author Harish Krishnaswamy
 * @version $Id: PoolingComponentState.java,v 1.1 2004-05-17 03:04:01 harishkswamy Exp $
 */
public class PoolingComponentState extends LazyLoadingComponentState
{
    private Pool _pool;

    public PoolingComponentState(ComponentState decorator, Component component, int poolSize)
    {
        super(decorator, component);

        _pool = new Pool(poolSize);
    }

    public PoolingComponentState(ComponentState delegate, Component component)
    {
        this(delegate, component, 0);
    }

    public Object getConcreteComponentInstance()
    {
        Object component = _pool.loan();

        if (component == null)
        {
            component = super.getConcreteComponentInstance();

            _pool.loaned(component);
        }

        return component;
    }

    public void collectComponentInstance(Object comp)
    {
        _pool.collect(comp);
    }

    public String toString()
    {
        return "[Pooled: " + super.toString() + "]";
    }
}