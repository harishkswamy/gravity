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

import gravity.DynamicWeaver;
import gravity.Gravity;
import gravity.GravityTestCase;

/**
 * @author Harish Krishnaswamy
 * @version $Id: DynamicWeaverFactoryTest.java,v 1.3 2004-05-18 20:51:55 harishkswamy Exp $
 */
public class DynamicWeaverFactoryTest extends GravityTestCase
{
    public void setUp()
    {
        Gravity.getInstance().initialize();
    }

    public void tearDown()
    {
        Gravity.getInstance().shutdown();
    }

    public void testGetSingletonDynamicWeaver()
    {
        DynamicWeaver weaver = DynamicWeaverFactory.getDynamicWeaver();

        assertNotNull(weaver);

        DynamicWeaver weaver2 = DynamicWeaverFactory.getDynamicWeaver();

        assertSame(weaver, weaver2);
    }

    public void testGetDefaultDynamicWeaver()
    {
        DynamicWeaver weaver = DynamicWeaverFactory.getDynamicWeaver();

        assertNotNull(weaver);
    }

    public static class TestDynamicWeaver implements DynamicWeaver
    {
        public Object weave(Object obj)
        {
            return obj;
        }
    }

    public void testGetCustomDynamicWeaver()
    {
        DynamicWeaverFactory.cleanup();

        Gravity.getInstance().setProperty(Gravity.DYNAMIC_WEAVER_CLASS_NAME_KEY,
            "gravity.impl.DynamicWeaverFactoryTest$TestDynamicWeaver");

        DynamicWeaver weaver = DynamicWeaverFactory.getDynamicWeaver();

        assertNotNull(weaver);
        assertTrue(weaver instanceof TestDynamicWeaver);
    }

    public void testInvalidDynamicWeaverClass()
    {
        DynamicWeaverFactory.cleanup();

        Gravity.getInstance().setProperty(Gravity.DYNAMIC_WEAVER_CLASS_NAME_KEY,
            "gravity.plugins.BshPlugin");

        try
        {
            DynamicWeaverFactory.getDynamicWeaver();

            unreachable();
        }
        catch (Exception e)
        {
            assertSuperString(e, "gravity.plugins.BshPlugin must implement gravity.DynamicWeaver");
        }
    }
}