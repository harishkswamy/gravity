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
import gravity.LazyComponentFactory;
import gravity.WrapperException;
import gravity.util.ClassUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author Harish Krishnaswamy
 * @version $Id: JdkComponentProxyFactory.java,v 1.1 2004-05-10 17:28:54 harishkswamy Exp $
 */
public class JdkComponentProxyFactory implements ComponentProxyFactory
{
    private static class ComponentInvocationHandler implements InvocationHandler
    {
        private LazyComponentFactory _componentFactory;
        private Object               _component;

        private ComponentInvocationHandler(LazyComponentFactory compFac)
        {
            _componentFactory = compFac;
        }

        private void obtainConcreteComponentInstance(Object proxy)
        {
            try
            {
                _component = _componentFactory.getConcreteComponentInstance(proxy);

                _componentFactory = null;
            }
            catch (Exception e)
            {
                throw WrapperException.wrap(e,
                    "Unable to get concrete component instance from factory: " + _componentFactory);
            }
        }

        public Object invoke(Object proxy, Method method, Object[] args)
        {
            if (_component == null)
                obtainConcreteComponentInstance(proxy);

            try
            {
                return method.invoke(_component, args);
            }
            catch (Exception e)
            {
                throw WrapperException.wrap(e, "Unable to invoke method: " + method
                    + " on component: " + _component);
            }
        }
    }

    public Object newComponentProxy(Class compIntf, LazyComponentFactory compFac)
    {
        try
        {
            InvocationHandler handler = new ComponentInvocationHandler(compFac);

            ClassLoader classLoader = ClassUtils.getClassLoader(compIntf);

            return Proxy.newProxyInstance(classLoader, new Class[]{compIntf}, handler);
        }
        catch (Exception e)
        {
            throw WrapperException.wrap(e, "Unable to create proxy for: " + compIntf);
        }
    }

    /**
     * This method accepts a factory for the component that the proxy will use to create the
     * component. This method is not thread safe because it is expected that the factory
     * registration will happen in a single thread at startup.
     */
    public void registerComponentFactory(Object compProxy, LazyComponentFactory compFac)
    {
        try
        {
            ComponentInvocationHandler handler = (ComponentInvocationHandler) Proxy.getInvocationHandler(compProxy);

            handler._componentFactory = compFac;
        }
        catch (Exception e)
        {
            throw WrapperException.wrap(e, "Unable to register component factory: " + compFac
                + " with proxy: " + compProxy);
        }
    }
}