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
 * @version $Id: DefaultComponentTest.java,v 1.2 2004-06-14 04:24:26 harishkswamy Exp $
 */
public class DefaultComponentTest extends GravityTestCase
{
    public void setUp()
    {
        Gravity.getInstance().initialize();
    }

    public void tearDown()
    {
        Gravity.getInstance().shutdown();
    }

    public void testGetComponentInstance()
    {
        ComponentKey compKey = new ComponentKey(MockComboService.class, null);

        DefaultComponent factory = new DefaultComponent(compKey);

        MockComboService service = (MockComboService) factory.getInstance();

        assertNotNull(service);

        MockComboService service2 = (MockComboService) factory.getInstance();

        assertTrue(service != service2);
    }

    public void testBuildComponentInstance()
    {
        ComponentKey compKey = new ComponentKey(MockComboService.class, null);

        DefaultComponent factory = new DefaultComponent(compKey);

        Object[] cArgs = {new Integer(2), new ArrayList()};

        factory.registerImplementation(MockComboServiceImpl.class, cArgs, null);

        MockComboService service = (MockComboService) factory.getInstance();
        MockComboService service2 = (MockComboService) factory.getInstance();

        service.service();
        service2.service();

        assertNotNull(service);
        assertNotSame(service, service2);
    }

    public void testRegisterComponentRegistrationLocation()
    {
        Location loc = new Location("Xyz.file", 12);

        ComponentKey compKey = new ComponentKey(MockComboService.class, null);

        DefaultComponent factory = new DefaultComponent(compKey);

        factory.setRegistrationLocation(loc);
    }

    public void testRegisterComponentRetrievalLocation()
    {
        Location loc = new Location("Xyz.file", 12);

        ComponentKey compKey = new ComponentKey(MockComboService.class, null);

        DefaultComponent factory = new DefaultComponent(compKey);

        factory.setRetrievalLocation(loc);
    }

    public void testToString()
    {
        ComponentKey compKey = new ComponentKey(MockComboService.class, null);

        DefaultComponent factory = new DefaultComponent(compKey);

        assertEquals(factory.toString(),
            "[Key: [Component Interface: interface gravity.mocks.MockComboService,"
                + " Component Type: default], Component Factory: [Class: null, Strategy:"
                + " [Lazy Loading] ], Registration Location: null,"
                + " Last Retrieval Location: null]");
    }
}