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
import gravity.Gravity;
import gravity.GravityTestCase;
import gravity.Location;
import gravity.mocks.MockComboService;
import gravity.mocks.MockComboServiceImpl;
import gravity.mocks.MockSetterService;
import gravity.mocks.MockSetterServiceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO test location in messages
/**
 * @author Harish Krishnaswamy
 * @version $Id: ComponentTest.java,v 1.1 2004-05-17 03:03:49 harishkswamy Exp $
 */
public class ComponentTest extends GravityTestCase
{
    public void setUp()
    {
        Gravity.getInstance().initialize();
    }

    public void tearDown()
    {
        Gravity.getInstance().shutdown();
    }

    private Component newComponent(Class intf, String type, Location intfLoc, Class impl,
        Object[] args, Map params, Location implLoc)
    {
        ComponentKey compKey = new ComponentKey(intf, type);

        Component state = new DefaultComponent(compKey);

        state.registerImplementation(impl, args, params);
        state.setRetrievalLocation(intfLoc);
        state.setRegistrationLocation(implLoc);

        return state;
    }

    public void testUnavailableService()
    {
        Component state = newComponent(List.class, "def", null, null, null, null, null);

        try
        {
            state.newInstance();

            unreachable();
        }
        catch (Exception e)
        {
            assertSuperString(e, "Implementation not registered for component: " + state);
        }
    }

    public void testServiceConfigError()
    {
        Component comp = newComponent(List.class, "def", null, List.class, null, null, null);

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

        Component state = newComponent(MockComboService.class, "def", null,
            MockComboServiceImpl.class, cArgs, null, null);

        MockComboServiceImpl obj = (MockComboServiceImpl) state.newInstance();

        assertNotNull(obj);
        assertTrue(obj.getList() instanceof ArrayList);
        assertTrue(obj.getId() == 6);
        assertNull(obj.getObject());
        assertTrue(obj.getPrimitive() == 0);
    }

    public void testBuildServiceViaSetrInjection()
    {
        Map args = new HashMap();
        args.put("primitive", new Integer(5));
        args.put("object", new ArrayList());

        Component state = newComponent(MockSetterService.class, "def", null,
            MockSetterServiceImpl.class, null, args, null);

        MockSetterServiceImpl obj = (MockSetterServiceImpl) state.newInstance();

        assertNotNull(obj);
        assertTrue(obj instanceof MockSetterService);
        assertTrue(obj.getPrimitive() == 5);
        assertTrue(obj.getObject() instanceof ArrayList);
    }

    public void testBuildServiceViaComboInjection()
    {
        Map args = new HashMap();
        args.put("primitive", new Integer(5));
        args.put("object", new ArrayList());

        Object[] cArgs = {new Integer(6), new ArrayList()};

        Component state = newComponent(MockComboService.class, "def", null,
            MockComboServiceImpl.class, cArgs, args, null);

        MockComboServiceImpl obj = (MockComboServiceImpl) state.newInstance();

        assertNotNull(obj);
        assertTrue(obj.getList() instanceof ArrayList);
        assertTrue(obj.getId() == 6);
        assertTrue(obj.getObject() instanceof ArrayList);
        assertTrue(obj.getPrimitive() == 5);
    }
}