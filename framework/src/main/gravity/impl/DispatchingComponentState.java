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
import gravity.ComponentInvocationHandler;
import gravity.ComponentProxy;
import gravity.ComponentState;
import gravity.util.Cache;

/**
 * @author Harish Krishnaswamy
 * @version $Id: DispatchingComponentState.java,v 1.2 2004-05-18 04:56:29 harishkswamy Exp $
 */
public class DispatchingComponentState extends ComponentStateDecorator
{
    public DispatchingComponentState(ComponentState decorator, Component component,
        Cache proxyInstanceCache)
    {
        super(decorator, component, proxyInstanceCache, true);
    }

    public Object getComponentInstance()
    {
        ComponentProxy proxy = ComponentProxyFactory.getInstance().getComponentProxy();

        ComponentInvocationHandler handler = proxy.newComponentInvocationHandler(_component, true);

        return proxy.newInstance(_component.getInterface(), handler);
    }
}