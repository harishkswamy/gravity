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

import gravity.ComponentProxy;
import gravity.Context;
import gravity.RealizableComponent;
import gravity.util.Message;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * This is the JDK version of {@link gravity.ComponentProxy}.
 * 
 * @author Harish Krishnaswamy
 * @version $Id: JdkComponentProxy.java,v 1.6 2005-10-06 21:59:29 harishkswamy Exp $
 */
public class JdkComponentProxy implements ComponentProxy
{
    private Context _context;

    public void initialize(Context context)
    {
        _context = context;
    }

    /**
     * This method can be overriden to provided a custom {@link InvocationHandler}.
     */
    protected InvocationHandler newComponentInvocationHandler(RealizableComponent comp)
    {
        return new JdkComponentInvocationHandler(_context, comp);
    }

    /**
     * This creates a new proxy instance for the provided component.
     */
    public Object newInstance(RealizableComponent comp)
    {
        try
        {
            ClassLoader classLoader = _context.getClassUtils().getClassLoader(comp.getInterface());

            return Proxy.newProxyInstance(classLoader, new Class[]{comp.getInterface()},
                newComponentInvocationHandler(comp));
        }
        catch (Exception e)
        {
            throw _context.getExceptionWrapper().wrap(e, Message.CANNOT_CREATE_PROXY, comp);
        }
    }
}