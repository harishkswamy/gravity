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

import gravity.ComponentInvocationHandler;
import gravity.ComponentProxy;
import gravity.ComponentState;
import gravity.ProxyableComponent;

/**
 * @author Harish Krishnaswamy
 * @version $Id: LazyLoadingComponentState.java,v 1.3 2004-05-18 20:52:05 harishkswamy Exp $
 */
public class LazyLoadingComponentState extends ComponentStateDecorator
{
    public LazyLoadingComponentState(ComponentState decorator, ProxyableComponent component)
    {
        super(decorator, component);
    }

    public Object getComponentInstance()
    {
        ComponentProxy proxy = ComponentProxyFactory.getInstance().getComponentProxy();

        ComponentInvocationHandler handler = proxy.newComponentInvocationHandler(_component);

        return proxy.newInstance(_component.getInterface(), handler);
    }

    public boolean isDispatching()
    {
        return false;
    }
}