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
 * @version $Id: ComponentBuilderTest.java,v 1.1 2004-05-10 17:28:42 harishkswamy Exp $
 */
public class ComponentBuilderTest extends GravityTestCase
{
    public void setUp()
    {
        Gravity.initialize();
    }

    public void tearDown()
    {
        Gravity.shutdown();
    }

    public void testUnavailableService()
    {
        ComponentKey compKey = new ComponentKey(List.class, "def");

        try
        {
            Object[] args = null;

            ComponentBuilder.build(compKey, null, null, args, null, null);

            unreachable();
        }
        catch (Exception e)
        {
            assertSuperString(e, "Implementation not registered for component: " + compKey);
        }
    }

    public void testServiceConfigError()
    {
        ComponentKey compKey = new ComponentKey(List.class, "def");

        try
        {
            ComponentBuilder.build(compKey, null, ArrayList.class, new Object[]{""}, null, null);

            unreachable();
        }
        catch (Exception e)
        {
            assertSuperString(e, "Build error for component: " + compKey);
        }
    }

    public void testBuildServiceViaCtorInjection()
    {
        Object[] cArgs = {new Integer(6), new ArrayList()};

        ComponentKey compKey = new ComponentKey(MockComboService.class, "def");

        MockComboServiceImpl obj = (MockComboServiceImpl) ComponentBuilder.build(compKey, null,
            MockComboServiceImpl.class, cArgs, null, null);

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

        MockSetterServiceImpl obj = (MockSetterServiceImpl) ComponentBuilder.build(
            new ComponentKey(MockSetterService.class, "def"), null, MockSetterServiceImpl.class,
            null, args, null);

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

        MockComboServiceImpl obj = (MockComboServiceImpl) ComponentBuilder.build(new ComponentKey(
            MockComboService.class, "def"), null, MockComboServiceImpl.class, cArgs, args, null);

        assertNotNull(obj);
        assertTrue(obj.getList() instanceof ArrayList);
        assertTrue(obj.getId() == 6);
        assertTrue(obj.getObject() instanceof ArrayList);
        assertTrue(obj.getPrimitive() == 5);
    }
}