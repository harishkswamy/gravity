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

import gravity.ComponentPhase;
import gravity.ComponentCallback;
import gravity.Gravity;
import gravity.GravityTestCase;
import gravity.Location;
import gravity.RealizableComponent;
import gravity.mocks.MockComboService;
import gravity.mocks.MockComboServiceImpl;
import gravity.mocks.MockSetterService;
import gravity.mocks.MockSetterServiceImpl;

import java.util.ArrayList;
import java.util.List;

// TODO test location in messages
/**
 * @author Harish Krishnaswamy
 * @version $Id: ComponentTest.java,v 1.7 2004-09-02 04:20:56 harishkswamy Exp $
 */
public class ComponentTest extends GravityTestCase
{
    private static final ComponentPhase START_UP = ComponentPhase.START_UP;

    public void setUp()
    {
        Gravity.getInstance().initialize();
    }

    public void tearDown()
    {
        Gravity.getInstance().shutdown();
    }

    private RealizableComponent newComponent(Class intf, String type, Location intfLoc, Class impl,
        Object[] args, ComponentCallback[] startUpMethods, Location implLoc)
    {
        ComponentKey compKey = new ComponentKey(intf, type);

        RealizableComponent comp = new DefaultComponent(compKey);

        comp.registerImplementation(impl, args, startUpMethods);
        comp.setRetrievalLocation(intfLoc);
        comp.setRegistrationLocation(implLoc);

        return comp;
    }

    public void testUnavailableService()
    {
        RealizableComponent comp = newComponent(List.class, "def", null, null, null, null, null);

        try
        {
            comp.newInstance();

            unreachable();
        }
        catch (Exception e)
        {
            assertSuperString(e, "Neither implementation nor factory registered for component: "
                + comp);
        }
    }

    public void testServiceConfigError()
    {
        RealizableComponent comp = newComponent(List.class, "def", null, List.class, null, null,
            null);

        try
        {
            comp.newInstance();

            unreachable();
        }
        catch (Exception e)
        {
            assertSuperString(e, "Unable to construct new instance for component: " + comp);
        }
    }

    public void testBuildServiceViaCtorInjection()
    {
        Object[] cArgs = {new Integer(6), new ArrayList()};

        RealizableComponent state = newComponent(MockComboService.class, "def", null,
            MockComboServiceImpl.class, cArgs, null, null);

        MockComboServiceImpl obj = (MockComboServiceImpl) state.newInstance();

        assertNotNull(obj);
        assertTrue(obj.getList() instanceof ArrayList);
        assertTrue(obj.getId() == 6);
        assertNull(obj.getObject());
        assertTrue(obj.getPrimitive() == 0);
    }

    public void testBuildServiceViaMethodInjection()
    {
        ComponentCallback mthd1 = new ComponentCallback("setPrimitive",
            new Object[]{new Integer(5)}, START_UP);
        ComponentCallback mthd2 = new ComponentCallback("setObject", new Object[]{new ArrayList()},
            START_UP);
        ComponentCallback[] methods = {mthd1, mthd2};

        RealizableComponent state = newComponent(MockSetterService.class, "def", null,
            MockSetterServiceImpl.class, null, methods, null);

        MockSetterServiceImpl obj = (MockSetterServiceImpl) state.newInstance();

        assertNotNull(obj);
        assertTrue(obj.getPrimitive() == 5);
        assertTrue(obj.getObject() instanceof ArrayList);
    }

    public void testBuildServiceViaComboInjection()
    {
        ComponentCallback mthd1 = new ComponentCallback("setPrimitive",
            new Object[]{new Integer(5)}, START_UP);
        ComponentCallback mthd2 = new ComponentCallback("setObject", new Object[]{new ArrayList()},
            START_UP);
        ComponentCallback[] methods = {mthd1, mthd2};

        Object[] cArgs = {new Integer(6), new ArrayList()};

        RealizableComponent state = newComponent(MockComboService.class, "def", null,
            MockComboServiceImpl.class, cArgs, methods, null);

        MockComboServiceImpl obj = (MockComboServiceImpl) state.newInstance();

        assertNotNull(obj);
        assertTrue(obj.getList() instanceof ArrayList);
        assertTrue(obj.getId() == 6);
        assertTrue(obj.getObject() instanceof ArrayList);
        assertTrue(obj.getPrimitive() == 5);
    }
}