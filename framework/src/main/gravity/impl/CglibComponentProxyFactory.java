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
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.Factory;
import net.sf.cglib.proxy.LazyLoader;

/**
 * @author Harish Krishnaswamy
 * @version $Id: CglibComponentProxyFactory.java,v 1.1 2004-05-10 17:28:54 harishkswamy Exp $
 */
public class CglibComponentProxyFactory implements ComponentProxyFactory
{
    private static class ComponentLoader implements LazyLoader
    {
        private LazyComponentFactory _componentFactory;

        private ComponentLoader(LazyComponentFactory compFac)
        {
            _componentFactory = compFac;
        }

        public Object loadObject()
        {
            try
            {
                Object comp = _componentFactory.getConcreteComponentInstance(this);

                _componentFactory = null;

                return comp;
            }
            catch (Exception e)
            {
                throw WrapperException.wrap(e,
                    "Unable to get concrete component instance from factory: " + _componentFactory);
            }
        }
    }

    public Object newComponentProxy(Class compIntf, LazyComponentFactory compFac)
    {
        try
        {
            Enhancer enhancer = new Enhancer();

            enhancer.setClassLoader(ClassUtils.getClassLoader(compIntf));

            enhancer.setSuperclass(compIntf);

            ComponentLoader dispatcher = new ComponentLoader(compFac);

            enhancer.setCallback(dispatcher);

            return enhancer.create();
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
            Factory cglibFactory = (Factory) compProxy;

            ComponentLoader loader = (ComponentLoader) cglibFactory.getCallback(0);

            loader._componentFactory = compFac;
        }
        catch (Exception e)
        {
            throw WrapperException.wrap(e, "Unable to register component factory: " + compFac
                + " with proxy: " + compProxy);
        }
    }
}