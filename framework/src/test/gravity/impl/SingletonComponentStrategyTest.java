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

import gravity.ComponentStrategy;
import gravity.Gravity;
import gravity.GravityTestCase;
import gravity.ProxyableComponent;
import gravity.mocks.MockComboService;
import gravity.mocks.MockComboServiceImpl;

import java.util.ArrayList;

/**
 * @author Harish Krishnaswamy
 * @version $Id: SingletonComponentStrategyTest.java,v 1.1 2004-05-22 20:19:27 harishkswamy Exp $
 */
public class SingletonComponentStrategyTest extends GravityTestCase
{
    public void setUp()
    {
        Gravity.getInstance().initialize();
    }

    public void tearDown()
    {
        Gravity.getInstance().shutdown();
    }

    private ProxyableComponent newComponent(Class compIntf, Object compType)
    {
        ComponentKey compKey = new ComponentKey(compIntf, compType);

        ProxyableComponent comp = new DefaultComponent(compKey);

        return comp;
    }

    private ComponentStrategy newSingletonComponent(ProxyableComponent comp)
    {
        ComponentStrategy factory = new LazyLoadingComponentStrategy(null, comp);

        return new SingletonComponentStrategy(factory, comp);
    }

    public void testGetComponentInstance()
    {
        ProxyableComponent comp = newComponent(MockComboService.class, null);

        ComponentStrategy factory = newSingletonComponent(comp);

        MockComboService service = (MockComboService) factory.getComponentInstance();

        assertNotNull(service);

        MockComboService service2 = (MockComboService) factory.getComponentInstance();

        assertTrue(service != service2);
    }

    public void testBuildComponentInstance()
    {
        ProxyableComponent comp = newComponent(MockComboService.class, null);

        Object[] cArgs = {new Integer(2), new ArrayList()};

        comp.registerImplementation(MockComboServiceImpl.class, cArgs, null);

        MockComboService service = (MockComboService) comp.getInstance();

        comp.wrapStrategyWithSingleton();

        MockComboService service2 = (MockComboService) comp.getInstance();

        service.service();
        service2.service();

        assertTrue(service.hashCode() == service2.hashCode());
    }

    public void testToString()
    {
        ProxyableComponent comp = newComponent(MockComboService.class, null);
        ComponentStrategy factory = newSingletonComponent(comp);

        assertEquals(factory.toString(),
            "[Singleton: [Key: [Component Interface: interface gravity.mocks.MockComboService,"
                + " Component Type: default], Implementation: null]]");
    }
}