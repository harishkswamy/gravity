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
import gravity.WrapperException;

/**
 * @author Harish Krishnaswamy
 * @version $Id: AbstractComponentInvocationHandler.java,v 1.2 2004-05-18 04:56:27 harishkswamy Exp $
 */
public abstract class AbstractComponentInvocationHandler implements ComponentInvocationHandler
{
    protected Component _component;
    protected Object    _componentInstance;
    protected boolean   _dispatchMode;

    protected AbstractComponentInvocationHandler(Component comp, boolean dispatchMode)
    {
        _component = comp;
        _dispatchMode = dispatchMode;
    }

    protected Object obtainConcreteComponentInstance()
    {
        try
        {
            Object instance;

            if (_dispatchMode)
                instance = _component.getConcreteInstance();

            else if (_componentInstance == null)
            {
                _componentInstance = _component.getConcreteInstance();

                instance = _componentInstance;
            }
            else
                instance = _componentInstance;

            return instance;
        }
        catch (Exception e)
        {
            throw WrapperException.wrap(e, "Unable to get concrete instance for component: "
                + _component);
        }
    }

    public void setDispatchMode(boolean mode)
    {
        _dispatchMode = mode;
    }
}