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
import gravity.Gravity;
import gravity.WrapperException;
import gravity.util.ClassUtils;

/**
 * @author Harish Krishnaswamy
 * @version $Id: ComponentProxyFactory.java,v 1.3 2004-05-22 20:19:34 harishkswamy Exp $
 */
public class ComponentProxyFactory
{
    private static final ComponentProxyFactory INSTANCE = new ComponentProxyFactory();
    private ComponentProxy                     _proxy;

    protected ComponentProxyFactory()
    {
        // Singleton
    }

    public static ComponentProxyFactory getInstance()
    {
        return INSTANCE;
    }

    private ComponentProxy getDefaultComponentProxy()
    {
        return new JdkComponentProxy();
    }

    private ComponentProxy newComponentProxy()
    {
        String className = Gravity.getInstance().getProperty(Gravity.COMPONENT_PROXY_CLASS_NAME_KEY);

        if (className == null)
            return getDefaultComponentProxy();

        try
        {
            return (ComponentProxy) ClassUtils.newInstance(className);
        }
        catch (ClassCastException e)
        {
            throw WrapperException.wrap(e, className + " must implement gravity.ComponentProxy.");
        }
    }

    public ComponentProxy getComponentProxy()
    {
        if (_proxy == null)
            _proxy = newComponentProxy();

        return _proxy;
    }

    public void cleanup()
    {
        _proxy = null;
    }
}