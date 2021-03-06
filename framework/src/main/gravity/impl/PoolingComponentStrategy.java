// Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package gravity.impl;

import gravity.ComponentStrategy;
import gravity.Context;
import gravity.RealizableComponent;
import gravity.util.Pool;

/**
 * This is a lazy loading strategy that will pool the generated concrete component instances. When
 * the use for the returned instance is over, it can be returned back to the pool via
 * {@link #collectComponentInstance(Object)}. The size of the pool can be configured by overriding
 * {@link gravity.impl.DefaultComponentInstanceBuilder#newPoolingStrategy(ComponentStrategy)}and
 * creating this strategy via {@link PoolingComponentStrategy(ComponentStrategy, int)}.
 * 
 * @see gravity.util.Pool
 * @author Harish Krishnaswamy
 * @version $Id: PoolingComponentStrategy.java,v 1.5 2005-10-06 21:59:27 harishkswamy Exp $
 */
public class PoolingComponentStrategy extends LazyLoadingComponentStrategy
{
    public static final String POOL_SIZE_KEY = "gravity.pool.size";

    private Pool               _pool;

    public void initialize(Context context, ComponentStrategy strategy)
    {
        super.initialize(context, strategy);

        int poolSize = ((Integer) context.getContextItem(POOL_SIZE_KEY)).intValue();

        _pool = new Pool(poolSize);
    }

    public Object getComponentInstance(RealizableComponent component)
    {
        Object compInst = _pool.loan();

        if (compInst == null)
        {
            compInst = super.getComponentInstance(component);

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