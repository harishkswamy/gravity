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
import gravity.ProxyableComponent;
import gravity.WrapperException;

/**
 * @author Harish Krishnaswamy
 * @version $Id: AbstractComponentInvocationHandler.java,v 1.3 2004-05-18 20:52:04 harishkswamy Exp $
 */
public abstract class AbstractComponentInvocationHandler implements ComponentInvocationHandler
{
    protected ProxyableComponent _component;
    protected Object             _componentInstance;

    protected AbstractComponentInvocationHandler(ProxyableComponent comp)
    {
        _component = comp;
    }

    protected Object getConcreteComponentInstance()
    {
        try
        {
            Object instance;

            if (_component.isInDispatchingState())
            {
                instance = _component.getConcreteInstance();

                _componentInstance = null;
            }
            else
            {
                if (_componentInstance == null)
                    _componentInstance = _component.getConcreteInstance();

                instance = _componentInstance;
            }

            return instance;
        }
        catch (Exception e)
        {
            throw WrapperException.wrap(e, "Unable to get concrete instance for component: "
                + _component);
        }
    }
}