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
import gravity.util.Pool;

/**
 * @author Harish Krishnaswamy
 * @version $Id: PoolingComponentStrategy.java,v 1.2 2004-06-14 04:15:19 harishkswamy Exp $
 */
public class PoolingComponentStrategy extends LazyLoadingComponentStrategy
{
    private Pool _pool;

    public PoolingComponentStrategy(ComponentStrategy decorator, int poolSize)
    {
        super(decorator);

        _pool = new Pool(poolSize);
    }

    public PoolingComponentStrategy(ComponentStrategy delegate)
    {
        this(delegate, 0);
    }

    public Object getConcreteComponentInstance(ProxyableComponent component)
    {
        Object compInst = _pool.loan();

        if (compInst == null)
        {
            compInst = super.getConcreteComponentInstance(component);

            _pool.loaned(compInst);
        }

        return compInst;
    }

    public void collectComponentInstance(Object compInst)
    {
        _pool.collect(compInst);
    }

    public String toString()
    {
        return " [Pooling" + decoratedStrategyToString() + "] ";
    }
}