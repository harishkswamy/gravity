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

import gravity.Gravity;
import gravity.GravityTestCase;
import gravity.ProxyableComponent;
import gravity.mocks.MockComboService;
import gravity.mocks.MockComboServiceImpl;
import gravity.mocks.MockService;

import java.util.ArrayList;

/**
 * @author Harish Krishnaswamy
 * @version $Id: CglibComponentProxyTest.java,v 1.5 2004-06-14 04:24:26 harishkswamy Exp $
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
        MockService service = (MockService) _proxyFactory.newInstance(newComponent(
            MockService.class, null));

        assertNotNull(service);
    }

    public void testNullFactory()
    {
        MockService service = (MockService) _proxyFactory.newInstance(newComponent(
            MockService.class, null));

        try
        {
            service.service();

            unreachable();
        }
        catch (Exception e)
        {
            assertSuperString(e, "Unable to get concrete instance for component: [Key: "
                + "[Component Interface: interface gravity.mocks.MockService, Component Type: "
                + "default], Component Factory: [Class: null, Strategy: [Lazy Loading] ], "
                + "Registration Location: null, Last Retrieval Location: null]");
        }
    }

    public void testInvokeComponent()
    {
        ProxyableComponent comp = newComponent(MockComboService.class, null);

        MockComboService service = (MockComboService) _proxyFactory.newInstance(comp);

        Object[] cArgs = new Object[]{new Integer(1), new ArrayList()};

        comp.registerImplementation(MockComboServiceImpl.class, cArgs, null);

        service.service();
    }

    public void testNewClassProxy()
    {
        Object obj = _proxyFactory.newInstance(newComponent(ArrayList.class, null));

        assertNotNull(obj);
        assertTrue(obj instanceof ArrayList);
    }

    public void testProxyForNull()
    {
        try
        {
            _proxyFactory.newInstance(null);

            unreachable();
        }
        catch (Exception e)
        {
            assertSuperString(e, "java.lang.NullPointerException: Unable to create proxy for: null");
        }
    }
}