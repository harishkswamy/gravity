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

import gravity.ComponentProxyFactory;
import gravity.Gravity;
import gravity.WrapperException;
import gravity.util.ClassUtils;

/**
 * @author Harish Krishnaswamy
 * @version $Id: ComponentProxyFactoryAgent.java,v 1.1 2004-05-10 17:28:56 harishkswamy Exp $
 */
public class ComponentProxyFactoryAgent
{
    private static ComponentProxyFactory _proxyFactory;

    private ComponentProxyFactoryAgent()
    {
        // Static class
    }

    private static ComponentProxyFactory getDefaultComponentProxyFactory()
    {
        return new JdkComponentProxyFactory();
    }

    private static ComponentProxyFactory newComponentProxyFactory()
    {
        String className = Gravity.getProperty(Gravity.COMPONENT_PROXY_FACTORY_CLASS_NAME);

        if (className == null)
            return getDefaultComponentProxyFactory();

        try
        {
            return (ComponentProxyFactory) ClassUtils.newInstance(className);
        }
        catch (ClassCastException e)
        {
            throw WrapperException.wrap(e, className
                + " must implement gravity.ComponentProxyFactory.");
        }
    }

    public static ComponentProxyFactory getComponentProxyFactory()
    {
        if (_proxyFactory == null)
            _proxyFactory = newComponentProxyFactory();

        return _proxyFactory;
    }

    public static void cleanup()
    {
        _proxyFactory = null;
    }
}