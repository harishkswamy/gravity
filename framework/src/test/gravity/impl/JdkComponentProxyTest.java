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
import gravity.Gravity;
import gravity.GravityTestCase;
import gravity.mocks.MockComboService;
import gravity.mocks.MockComboServiceImpl;
import gravity.mocks.MockService;

import java.util.ArrayList;

/**
 * @author Harish Krishnaswamy
 * @version $Id: JdkComponentProxyTest.java,v 1.2 2004-05-18 04:56:36 harishkswamy Exp $
 */
public class JdkComponentProxyTest extends GravityTestCase
{
    final JdkComponentProxy _proxyFactory = new JdkComponentProxy();

    public void setUp()
    {
        Gravity.getInstance().initialize();
    }

    public void tearDown()
    {
        Gravity.getInstance().shutdown();
    }

    private Component newComponent(Class intf, Object type)
    {
        ComponentKey key = new ComponentKey(intf, type);
        return new DefaultComponent(key);
    }

    public void testInvokeNewProxyMethod()
    {
        MockService service = (MockService) _proxyFactory.newInstance(MockService.class,
            _proxyFactory.newComponentInvocationHandler(newComponent(MockService.class, null),
                false));

        assertNotNull(service);
    }

    public void testMethodInvocationError()
    {
        Component comp = newComponent(MockService.class, null);
        ComponentInvocationHandler handler = _proxyFactory.newComponentInvocationHandler(comp,
            false);
        MockService service = (MockService) _proxyFactory.newInstance(MockService.class, handler);

        comp.registerImplementation(ArrayList.class, null, null);

        try
        {
            service.service();

            unreachable();
        }
        catch (Exception e)
        {
            assertSuperString(e, "Unable to invoke method: public abstract void "
                + "gravity.mocks.MockService.service() on component: " + comp);
        }
    }

    public void testNullFactory()
    {
        MockService service = (MockService) _proxyFactory.newInstance(MockService.class,
            _proxyFactory.newComponentInvocationHandler(null, false));

        try
        {
            service.service();

            unreachable();
        }
        catch (Exception e)
        {
            assertSuperString(e, "Unable to get concrete instance for component: null");
        }
    }

    public void testInvokeComponent()
    {
        Component comp = newComponent(MockComboService.class, null);
        MockComboService service = (MockComboService) _proxyFactory.newInstance(
            MockComboService.class, _proxyFactory.newComponentInvocationHandler(comp, false));

        Object[] cArgs = new Object[]{new Integer(1), new ArrayList()};

        comp.registerImplementation(MockComboServiceImpl.class, cArgs, null);

        service.service();
    }

    public void testNewClassProxy()
    {
        try
        {
            _proxyFactory.newInstance(MockComboServiceImpl.class,
                _proxyFactory.newComponentInvocationHandler(null, false));

            unreachable();
        }
        catch (Exception e)
        {
            assertSuperString(e, "java.lang.IllegalArgumentException:"
                + " Unable to create proxy for: class gravity.mocks.MockComboServiceImpl");
        }
    }

    public void testProxyForNull()
    {
        try
        {
            _proxyFactory.newInstance(null, null);

            unreachable();
        }
        catch (Exception e)
        {
            assertSuperString(e, "java.lang.NullPointerException: Unable to create proxy for: null");
        }
    }

    public void testGetComponentInvocationHandler()
    {
        CglibComponentProxy proxy = new CglibComponentProxy();

        Component comp = newComponent(MockComboService.class, null);

        Object obj = proxy.newInstance(MockComboService.class, proxy.newComponentInvocationHandler(
            comp, false));

        Object[] cArgs = new Object[]{new Integer(1), new ArrayList()};
        comp.registerImplementation(MockComboServiceImpl.class, cArgs, null);

        try
        {
            _proxyFactory.getComponentInvocationHandler(obj);

            unreachable();
        }
        catch (Exception e)
        {
            assertSuperString(e, "java.lang.IllegalArgumentException:"
                + " Unable to get component invocation handler from: " + obj);
        }
    }
}