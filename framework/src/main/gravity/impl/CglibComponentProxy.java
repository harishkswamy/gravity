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
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;

/**
 * @author Harish Krishnaswamy
 * @version $Id: CglibComponentProxy.java,v 1.4 2004-05-22 20:19:33 harishkswamy Exp $
 */
public class CglibComponentProxy implements ComponentProxy
{
    /**
     * This method must return a Cglib callback.
     */
    protected ComponentInvocationHandler newComponentInvocationHandler(ProxyableComponent comp)
    {
        return new CglibComponentInvocationHandler(comp);
    }

    public Object newInstance(ProxyableComponent comp)
    {
        try
        {
            Enhancer enhancer = new Enhancer();

            enhancer.setClassLoader(ClassUtils.getClassLoader(comp.getInterface()));

            enhancer.setSuperclass(comp.getInterface());

            enhancer.setCallback((Callback) newComponentInvocationHandler(comp));

            return enhancer.create();
        }
        catch (Exception e)
        {
            throw WrapperException.wrap(e, "Unable to create proxy for: " + comp);
        }
    }
}