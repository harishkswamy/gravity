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

import gravity.ProxyableComponent;
import gravity.WrapperException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author Harish Krishnaswamy
 * @version $Id: JdkComponentInvocationHandler.java,v 1.3 2004-05-18 20:52:03 harishkswamy Exp $
 */
public class JdkComponentInvocationHandler extends AbstractComponentInvocationHandler implements
    InvocationHandler
{
    public JdkComponentInvocationHandler(ProxyableComponent comp)
    {
        super(comp);
    }

    public Object invoke(Object proxy, Method method, Object[] args)
    {
        Object instance = getConcreteComponentInstance();

        try
        {
            return method.invoke(instance, args);
        }
        catch (Exception e)
        {
            throw WrapperException.wrap(e, "Unable to invoke method: " + method + " on component: "
                + _component);
        }
    }
}