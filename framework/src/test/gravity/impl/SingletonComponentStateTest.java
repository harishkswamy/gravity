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
import gravity.ComponentState;
import gravity.Gravity;
import gravity.GravityTestCase;
import gravity.mocks.MockComboService;
import gravity.mocks.MockComboServiceImpl;
import gravity.util.Cache;

import java.util.ArrayList;

/**
 * @author Harish Krishnaswamy
 * @version $Id: SingletonComponentStateTest.java,v 1.2 2004-05-18 04:56:35 harishkswamy Exp $
 */
public class SingletonComponentStateTest extends GravityTestCase
{
    public void setUp()
    {
        Gravity.getInstance().initialize();
    }

    public void tearDown()
    {
        Gravity.getInstance().shutdown();
    }

    private Component newComponent(Class compIntf, Object compType)
    {
        ComponentKey compKey = new ComponentKey(compIntf, compType);

        Component comp = new DefaultComponent(compKey);

        return comp;
    }

    private ComponentState newSingletonComponent(Component comp)
    {
        ComponentState factory = new LazyLoadingComponentState(null, comp, new Cache());

        return new SingletonComponentState(factory, comp, new Cache());
    }

    public void testGetComponentInstance()
    {
        Component comp = newComponent(MockComboService.class, null);

        ComponentState factory = newSingletonComponent(comp);

        MockComboService service = (MockComboService) factory.getComponentInstance();

        assertNotNull(service);

        MockComboService service2 = (MockComboService) factory.getComponentInstance();

        assertTrue(service != service2);
    }

    public void testBuildComponentInstance()
    {
        Component comp = newComponent(MockComboService.class, null);

        Object[] cArgs = {new Integer(2), new ArrayList()};

        comp.registerImplementation(MockComboServiceImpl.class, cArgs, null);

        MockComboService service = (MockComboService) comp.getInstance();

        comp.wrapStateWithSingleton();

        MockComboService service2 = (MockComboService) comp.getInstance();

        service.service();
        service2.service();

        assertTrue(service.hashCode() == service2.hashCode());
    }

    public void testToString()
    {
        Component comp = newComponent(MockComboService.class, null);
        ComponentState factory = newSingletonComponent(comp);

        assertEquals(factory.toString(),
            "[Singleton: [Key: [Component Interface: interface gravity.mocks.MockComboService,"
                + " Component Type: default], Implementation: null]]");
    }
}