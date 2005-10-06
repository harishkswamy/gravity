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

import gravity.Context;
import gravity.RealizableComponent;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * This is the JDK version of component invocation handler.
 * 
 * @author Harish Krishnaswamy
 * @version $Id: JdkComponentInvocationHandler.java,v 1.6 2005-10-06 21:59:28 harishkswamy Exp $
 */
public class JdkComponentInvocationHandler extends AbstractComponentInvocationHandler implements
    InvocationHandler
{
    public JdkComponentInvocationHandler(Context context, RealizableComponent comp)
    {
        super(context, comp);
    }

    public Object invoke(Object proxy, Method method, Object[] args)
    {
        Object instance = getConcreteComponentInstance();

        return _context.getReflectUtils().invokeMethod(instance, method, args, _component);
    }
}