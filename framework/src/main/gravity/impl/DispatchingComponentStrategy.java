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

import gravity.ComponentProxy;
import gravity.ComponentStrategy;
import gravity.ProxyableComponent;

/**
 * @author Harish Krishnaswamy
 * @version $Id: DispatchingComponentStrategy.java,v 1.2 2004-06-14 04:15:19 harishkswamy Exp $
 */
public class DispatchingComponentStrategy extends ComponentStrategyDecorator
{
    public DispatchingComponentStrategy(ComponentStrategy decorator)
    {
        super(decorator);
    }

    public Object getComponentInstance(ProxyableComponent component)
    {
        ComponentProxy proxy = ComponentProxyFactory.getInstance().getComponentProxy();

        return proxy.newInstance(component);
    }

    public boolean isDispatching()
    {
        return true;
    }

    public String toString()
    {
        return " [Dispatching" + decoratedStrategyToString() + "] ";
    }
}