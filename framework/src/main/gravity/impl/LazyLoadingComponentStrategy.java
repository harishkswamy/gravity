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

/**
 * This is a {@link gravity.impl.ComponentStrategyDecorator}that simply identifies this category of
 * strategies as "lazy loading". Method calls made on components that have this strategy will be
 * dispatched to this strategy only for the very first call to obtain the concrete instance. All
 * subsequent calls will directly go to the initial instance returned which will be cached in the
 * component proxy.
 * 
 * @author Harish Krishnaswamy
 * @version $Id: LazyLoadingComponentStrategy.java,v 1.3 2004-09-02 04:04:18 harishkswamy Exp $
 */
public class LazyLoadingComponentStrategy extends ComponentStrategyDecorator
{
    public LazyLoadingComponentStrategy(ComponentStrategy decorator)
    {
        super(decorator);
    }

    /**
     * @return false, this is a lazy loading strategy.
     */
    public boolean isDispatching()
    {
        return false;
    }

    /**
     * @return Returns " [Lazy Loading" +
     *         {@link ComponentStrategyDecorator#decoratedStrategyToString()}+ "] "
     */
    public String toString()
    {
        return " [Lazy Loading" + decoratedStrategyToString() + "] ";
    }
}