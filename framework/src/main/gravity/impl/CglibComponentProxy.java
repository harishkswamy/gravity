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
import gravity.RealizableComponent;
import gravity.WrapperException;
import gravity.util.ClassUtils;
import gravity.util.Message;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;

/**
 * This is the CgLib version of {@link gravity.ComponentProxy}.
 * 
 * @author Harish Krishnaswamy
 * @version $Id: CglibComponentProxy.java,v 1.5 2004-09-02 04:04:49 harishkswamy Exp $
 */
public class CglibComponentProxy implements ComponentProxy
{
    /**
     * This method can be overriden to provided a custom {@link Callback}.
     */
    protected Callback newComponentInvocationHandler(RealizableComponent comp)
    {
        return new CglibComponentInvocationHandler(comp);
    }

    /**
     * This creates a new proxy instance for the provided component.
     */
    public Object newInstance(RealizableComponent comp)
    {
        try
        {
            Enhancer enhancer = new Enhancer();

            enhancer.setClassLoader(ClassUtils.getClassLoader(comp.getInterface()));

            enhancer.setSuperclass(comp.getInterface());

            enhancer.setCallback(newComponentInvocationHandler(comp));

            return enhancer.create();
        }
        catch (Exception e)
        {
            throw WrapperException.wrap(e, Message.CANNOT_CREATE_PROXY, comp);
        }
    }
}