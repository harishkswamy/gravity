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
import gravity.WrapperException;
import gravity.util.ClassUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @author Harish Krishnaswamy
 * @version $Id: JdkComponentProxy.java,v 1.1 2004-05-17 03:04:05 harishkswamy Exp $
 */
public class JdkComponentProxy implements ComponentProxy
{
    public ComponentInvocationHandler newLazyLoader(Component comp)
    {
        return new JdkComponentInvocationHandler(comp, false);
    }

    public ComponentInvocationHandler newDispatcher(Component comp)
    {
        return new JdkComponentInvocationHandler(comp, true);
    }

    public Object newInstance(Class compIntf, ComponentInvocationHandler handler)
    {
        try
        {
            ClassLoader classLoader = ClassUtils.getClassLoader(compIntf);

            return Proxy.newProxyInstance(classLoader, new Class[]{compIntf},
                (InvocationHandler) handler);
        }
        catch (Exception e)
        {
            throw WrapperException.wrap(e, "Unable to create proxy for: " + compIntf);
        }
    }

    public ComponentInvocationHandler getComponentInvocationHandler(Object proxy)
    {
        try
        {
            return (ComponentInvocationHandler) Proxy.getInvocationHandler(proxy);
        }
        catch (Exception e)
        {
            throw WrapperException.wrap(e, "Unable to get component invocation handler from: "
                + proxy);
        }
    }
}