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
import gravity.ProxyableComponent;
import gravity.WrapperException;
import gravity.util.ClassUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @author Harish Krishnaswamy
 * @version $Id: JdkComponentProxy.java,v 1.4 2004-05-22 20:19:35 harishkswamy Exp $
 */
public class JdkComponentProxy implements ComponentProxy
{
    protected ComponentInvocationHandler newComponentInvocationHandler(ProxyableComponent comp)
    {
        return new JdkComponentInvocationHandler(comp);
    }

    public Object newInstance(ProxyableComponent comp)
    {
        try
        {
            ClassLoader classLoader = ClassUtils.getClassLoader(comp.getInterface());

            return Proxy.newProxyInstance(classLoader, new Class[]{comp.getInterface()},
                (InvocationHandler) newComponentInvocationHandler(comp));
        }
        catch (Exception e)
        {
            throw WrapperException.wrap(e, "Unable to create proxy for: " + comp);
        }
    }
}