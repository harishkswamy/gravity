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
import gravity.util.Message;

/**
 * This class is basically a template for various proxy-invocation-handler-implementations to use.
 * This class defines the algorithm to obtain a concrete component instance that subclasses must
 * use.
 * 
 * @author Harish Krishnaswamy
 * @version $Id: AbstractComponentInvocationHandler.java,v 1.5 2005-10-06 21:59:27 harishkswamy Exp $
 */
public abstract class AbstractComponentInvocationHandler
{
    protected Context             _context;
    protected RealizableComponent _component;

    /**
     * This instance is cached here so if the component is not in dispatching state, the calls to
     * the component will always go to the originally returned instance.
     */
    protected Object              _componentInstance;

    protected AbstractComponentInvocationHandler(Context context, RealizableComponent comp)
    {
        _context = context;
        _component = comp;
    }

    /**
     * This is the algorithm to obtain a concrete component instance that subclasses must use.
     * <p>
     * The state of the proxyable component is checked for every call here to enable the dynamic
     * behavior. This way a component can change its strategy even after the instance was returned
     * to the caller and yet it will always obey the strategy (ThreadLocal/Lazy/Pooled ...)
     * prevalent at the time of the call.
     */
    protected final Object getConcreteComponentInstance()
    {
        try
        {
            Object instance;

            if (_component.isInDispatchingState())
            {
                instance = _component.getConcreteInstance();

                /*
                 * The cached instance should be cleared here so it can be garbage-collected and if
                 * ever the component returns to a non-dispatching state, the first call after, will
                 * get a new instance - handled in the else block.
                 */
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
            throw _context.getExceptionWrapper().wrap(e,
                Message.CANNOT_GET_CONCRETE_COMPONENT_INSTANCE, _component);
        }
    }
}