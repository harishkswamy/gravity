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
import gravity.Location;
import gravity.mocks.MockComboService;
import gravity.mocks.MockComboServiceImpl;

import java.util.ArrayList;

/**
 * @author Harish Krishnaswamy
 * @version $Id: DefaultComponentFactoryTest.java,v 1.1 2004-05-10 17:28:42 harishkswamy Exp $
 */
public class DefaultComponentFactoryTest extends GravityTestCase
{
    public void setUp()
    {
        Gravity.initialize();
    }

    public void tearDown()
    {
        Gravity.shutdown();
    }

    public void testGetComponentInstance()
    {
        ComponentKey compKey = new ComponentKey(MockComboService.class, null);

        DefaultComponentFactory factory = new DefaultComponentFactory(compKey);

        MockComboService service = (MockComboService) factory.getComponentInstance();

        assertNotNull(service);

        MockComboService service2 = (MockComboService) factory.getComponentInstance();

        assertTrue(service != service2);
    }

    public void testBuildComponentInstance()
    {
        ComponentKey compKey = new ComponentKey(MockComboService.class, null);

        DefaultComponentFactory factory = new DefaultComponentFactory(compKey);

        Object[] cArgs = {new Integer(2), new ArrayList()};

        factory.registerComponentImplementation(MockComboServiceImpl.class, cArgs, null);

        MockComboService service = (MockComboService) factory.getConcreteComponentInstance();
        MockComboService service2 = (MockComboService) factory.getConcreteComponentInstance();

        assertNotNull(service);
        assertTrue(service instanceof MockComboServiceImpl);
        assertNotSame(service, service2);
    }

    public void testDecorated()
    {
        ComponentKey compKey = new ComponentKey(MockComboService.class, null);

        DefaultComponentFactory factory = new DefaultComponentFactory(compKey);

        factory.getComponentInstance();

        factory.decorated(factory);
    }

    public void testRegisterComponentRegistrationLocation()
    {
        Location loc = new Location("Xyz.file", 12);

        ComponentKey compKey = new ComponentKey(MockComboService.class, null);

        DefaultComponentFactory factory = new DefaultComponentFactory(compKey);

        factory.registerComponentRegistrationLocation(loc);
    }

    public void testRegisterComponentRetrievalLocation()
    {
        Location loc = new Location("Xyz.file", 12);

        ComponentKey compKey = new ComponentKey(MockComboService.class, null);

        DefaultComponentFactory factory = new DefaultComponentFactory(compKey);

        factory.registerComponentRetrievalLocation(loc);
    }

    public void testToString()
    {
        ComponentKey compKey = new ComponentKey(MockComboService.class, null);

        DefaultComponentFactory factory = new DefaultComponentFactory(compKey);

        assertEquals(factory.toString(),
            "[Component: [Component Interface: interface gravity.mocks.MockComboService,"
                + " Component Type: default], Component Class: null]");
    }
}