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
import gravity.Gravity;
import gravity.GravityTestCase;
import gravity.ProxyableComponent;
import gravity.mocks.MockComboService;
import gravity.mocks.MockComboServiceImpl;
import gravity.mocks.MockService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Harish Krishnaswamy
 * @version $Id: CglibComponentProxyTest.java,v 1.3 2004-05-18 20:51:56 harishkswamy Exp $
 */
public class CglibComponentProxyTest extends GravityTestCase
{
    CglibComponentProxy _proxyFactory = new CglibComponentProxy();

    public void setUp()
    {
        Gravity.getInstance().initialize();
    }

    public void tearDown()
    {
        Gravity.getInstance().shutdown();
    }

    private ProxyableComponent newComponent(Class intf, Object type)
    {
        ComponentKey key = new ComponentKey(intf, type);
        ProxyableComponent comp = new DefaultComponent(key);

        return comp;
    }

    public void testNewComponentProxy()
    {
        ComponentInvocationHandler handler = _proxyFactory.newComponentInvocationHandler(newComponent(
            MockService.class, null));

        MockService service = (MockService) _proxyFactory.newInstance(MockService.class, handler);

        assertNotNull(service);
    }

    public void testNullFactory()
    {
        MockService service = (MockService) _proxyFactory.newInstance(MockService.class,
            _proxyFactory.newComponentInvocationHandler(null));

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
        ProxyableComponent comp = newComponent(MockComboService.class, null);

        MockComboService service = (MockComboService) _proxyFactory.newInstance(
            MockComboService.class, _proxyFactory.newComponentInvocationHandler(comp));

        Object[] cArgs = new Object[]{new Integer(1), new ArrayList()};

        comp.registerImplementation(MockComboServiceImpl.class, cArgs, null);

        service.service();
    }

    public void testNewClassProxy()
    {
        Object obj = _proxyFactory.newInstance(ArrayList.class,
            _proxyFactory.newComponentInvocationHandler(null));

        assertNotNull(obj);
        assertTrue(obj instanceof ArrayList);
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
        JdkComponentProxy proxy = new JdkComponentProxy();

        ProxyableComponent comp = newComponent(MockComboService.class, null);
        Object obj = proxy.newInstance(List.class, proxy.newComponentInvocationHandler(comp));

        Object[] cArgs = new Object[]{new Integer(1), new ArrayList()};
        comp.registerImplementation(MockComboServiceImpl.class, cArgs, null);

        try
        {
            _proxyFactory.getComponentInvocationHandler(obj);

            unreachable();
        }
        catch (Exception e)
        {
            assertSuperString(e, "java.lang.ClassCastException:"
                + " Unable to get component invocation handler from: " + obj);
        }
    }
}