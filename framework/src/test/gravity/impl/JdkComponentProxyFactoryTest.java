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
import gravity.LazyComponentFactory;
import gravity.mocks.MockComboService;
import gravity.mocks.MockComboServiceImpl;
import gravity.mocks.MockService;

import java.util.ArrayList;

/**
 * @author Harish Krishnaswamy
 * @version $Id: JdkComponentProxyFactoryTest.java,v 1.1 2004-05-10 17:28:43 harishkswamy Exp $
 */
public class JdkComponentProxyFactoryTest extends GravityTestCase
{
    final JdkComponentProxyFactory _proxyFactory = new JdkComponentProxyFactory();

    public void setUp()
    {
        Gravity.initialize();
    }

    public void tearDown()
    {
        Gravity.shutdown();
    }

    public void testInvokeNewProxyMethod()
    {
        MockService service = (MockService) _proxyFactory.newComponentProxy(MockService.class, null);

        _proxyFactory.registerComponentFactory(service, new DefaultComponentFactory(
            new ComponentKey(MockService.class, null)));

        assertNotNull(service);
    }

    public void testMethodInvocationError()
    {
        MockService service = (MockService) _proxyFactory.newComponentProxy(MockService.class, null);

        LazyComponentFactory compFac = new DefaultComponentFactory(new ComponentKey(
            MockService.class, null));

        compFac.registerComponentImplementation(ArrayList.class, null, null);

        _proxyFactory.registerComponentFactory(service, compFac);

        try
        {
            service.service();

            unreachable();
        }
        catch (Exception e)
        {
            assertSuperString(e, "Unable to invoke method: public abstract void "
                + "gravity.mocks.MockService.service() on component: []");
        }
    }

    public void testNullFactory()
    {
        MockService service = (MockService) _proxyFactory.newComponentProxy(MockService.class, null);

        try
        {
            service.service();

            unreachable();
        }
        catch (Exception e)
        {
            assertSuperString(e, "Unable to get concrete component instance from factory: null");
        }
    }

    public void testInvokeComponent()
    {
        MockComboService service = (MockComboService) _proxyFactory.newComponentProxy(
            MockComboService.class, null);

        LazyComponentFactory compFac = new DefaultComponentFactory(new ComponentKey(
            MockComboService.class, null));

        Object[] cArgs = new Object[]{new Integer(1), new ArrayList()};

        compFac.registerComponentImplementation(MockComboServiceImpl.class, cArgs, null);

        _proxyFactory.registerComponentFactory(service, compFac);

        service.service();
    }

    public void testNewClassProxy()
    {
        try
        {
            _proxyFactory.newComponentProxy(MockComboServiceImpl.class, null);

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
            _proxyFactory.newComponentProxy(null, null);

            unreachable();
        }
        catch (Exception e)
        {
            assertSuperString(e, "java.lang.NullPointerException: Unable to create proxy for: null");
        }
    }

    public void testRegisterComponentFactory()
    {
        LazyComponentFactory compFac = new DefaultComponentFactory(new ComponentKey(
            MockComboService.class, null));

        try
        {
            _proxyFactory.registerComponentFactory(null, compFac);

            unreachable();
        }
        catch (Exception e)
        {
            assertSuperString(e, "java.lang.NullPointerException:"
                + " Unable to register component factory: " + compFac + " with proxy: null");
        }
    }
}