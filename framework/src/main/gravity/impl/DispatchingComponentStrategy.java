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

/**
 * This is a {@link gravity.impl.ComponentStrategyDecorator}that simply identifies this category of
 * strategies as "dispatching". Method calls made on components that have this strategy will be
 * dispatched to this strategy for every call to obtain the concrete instance.
 * 
 * @author Harish Krishnaswamy
 * @version $Id: DispatchingComponentStrategy.java,v 1.4 2005-10-06 21:59:29 harishkswamy Exp $
 */
public class DispatchingComponentStrategy extends ComponentStrategyDecorator
{
    /**
     * @return true, this is a dispatching strategy.
     */
    public boolean isDispatching()
    {
        return true;
    }

    /**
     * @return Returns " [Dispatching" +
     *         {@link ComponentStrategyDecorator#decoratedStrategyToString()}+ "] "
     */
    public String toString()
    {
        return " [Dispatching" + decoratedStrategyToString() + "] ";
    }
}