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

import gravity.ComponentFactory;
import gravity.Gravity;
import gravity.GravityTestCase;
import gravity.LazyComponentFactory;
import gravity.Location;
import gravity.mocks.MockComboService;
import gravity.mocks.MockComboServiceImpl;

import java.util.ArrayList;

/**
 * @author Harish Krishnaswamy
 * @version $Id: SingletonComponentFactoryTest.java,v 1.2 2004-05-12 04:45:19 harishkswamy Exp $
 */
public class SingletonComponentFactoryTest extends GravityTestCase
{
    public void setUp()
    {
        Gravity.initialize();
    }

    public void tearDown()
    {
        Gravity.shutdown();
    }

    private SingletonComponentFactory getFactory(Class compIntf, Object compType)
    {
        ComponentKey compKey = new ComponentKey(compIntf, compType);

        LazyComponentFactory factory = new DefaultComponentFactory(compKey);

        return new SingletonComponentFactory(factory);
    }

    public void testGetComponentInstance()
    {
        ComponentFactory factory = getFactory(MockComboService.class, null);

        MockComboService service = (MockComboService) factory.getComponentInstance();

        assertNotNull(service);

        MockComboService service2 = (MockComboService) factory.getComponentInstance();

        assertTrue(service != service2);
    }

    public void testBuildComponentInstance()
    {
        LazyComponentFactory factory = getFactory(MockComboService.class, null);

        Object[] cArgs = {new Integer(2), new ArrayList()};

        factory.registerComponentImplementation(MockComboServiceImpl.class, cArgs, null);

        MockComboService service = (MockComboService) factory.getConcreteComponentInstance();
        MockComboService service2 = (MockComboService) factory.getConcreteComponentInstance();

        assertNotNull(service);
        assertTrue(service instanceof MockComboServiceImpl);
        assertTrue(service.hashCode() == service2.hashCode());
    }

    public void testDecorated()
    {
        LazyComponentFactory factory = getFactory(MockComboService.class, null);

        factory.getComponentInstance();

        factory.decorated(factory);
    }

    public void testRegisterComponentRegistrationLocation()
    {
        Location loc = new Location("Xyz.file", 12);

        ComponentFactory factory = getFactory(MockComboService.class, null);

        factory.registerComponentRegistrationLocation(loc);
    }

    public void testRegisterComponentRetrievalLocation()
    {
        Location loc = new Location("Xyz.file", 12);

        ComponentFactory factory = getFactory(MockComboService.class, null);

        factory.registerComponentRetrievalLocation(loc);
    }

    public void testToString()
    {
        ComponentFactory factory = getFactory(MockComboService.class, null);

        assertEquals(factory.toString(),
            "[Component: [Component Interface: interface gravity.mocks.MockComboService,"
                + " Component Type: default], Component Class: null]");
    }
}