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
import gravity.WrapperException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author Harish Krishnaswamy
 * @version $Id: JdkComponentInvocationHandler.java,v 1.1 2004-05-17 03:04:02 harishkswamy Exp $
 */
public class JdkComponentInvocationHandler extends AbstractComponentInvocationHandler implements InvocationHandler
{
    private Object  _componentInstance;
    private boolean _dispatch;

    public JdkComponentInvocationHandler(Component comp)
    {
        super(comp);
    }

    public JdkComponentInvocationHandler(Component comp, boolean dispatch)
    {
        super(comp);
        _dispatch = dispatch;
    }

    private Object obtainConcreteComponentInstance()
    {
        try
        {
            Object comp = _component.getConcreteInstance();

            if (!_dispatch)
                _componentInstance = comp;

            return comp;
        }
        catch (Exception e)
        {
            throw WrapperException.wrap(e, "Unable to get concrete instance for component: "
                + _component);
        }
    }

    public Object invoke(Object proxy, Method method, Object[] args)
    {
        Object comp = _componentInstance;

        if (_dispatch || comp == null)
            comp = obtainConcreteComponentInstance();

        try
        {
            return method.invoke(comp, args);
        }
        catch (Exception e)
        {
            throw WrapperException.wrap(e, "Unable to invoke method: " + method + " on component: "
                + comp);
        }
    }
}