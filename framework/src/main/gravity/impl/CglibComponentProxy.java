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
import gravity.Component;
import gravity.WrapperException;
import gravity.util.ClassUtils;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.Factory;

/**
 * @author Harish Krishnaswamy
 * @version $Id: CglibComponentProxy.java,v 1.2 2004-05-18 04:56:27 harishkswamy Exp $
 */
public class CglibComponentProxy implements ComponentProxy
{
    public ComponentInvocationHandler newComponentInvocationHandler(Component comp,
        boolean dispatchMode)
    {
        return new CglibComponentInvocationHandler(comp, dispatchMode);
    }

    public Object newInstance(Class compIntf, ComponentInvocationHandler handler)
    {
        try
        {
            Enhancer enhancer = new Enhancer();

            enhancer.setClassLoader(ClassUtils.getClassLoader(compIntf));

            enhancer.setSuperclass(compIntf);

            enhancer.setCallback((Callback) handler);

            return enhancer.create();
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
            Factory cglibFactory = (Factory) proxy;

            return (ComponentInvocationHandler) cglibFactory.getCallback(0);
        }
        catch (Exception e)
        {
            throw WrapperException.wrap(e, "Unable to get component invocation handler from: "
                + proxy);
        }
    }
}