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

import gravity.ComponentProxy;
import gravity.Gravity;
import gravity.GravityTestCase;

/**
 * @author Harish Krishnaswamy
 * @version $Id: ComponentProxyFactoryTest.java,v 1.1 2004-05-17 03:03:46 harishkswamy Exp $
 */
public class ComponentProxyFactoryTest extends GravityTestCase
{
    public void setUp()
    {
        Gravity.getInstance().initialize();
    }

    public void tearDown()
    {
        Gravity.getInstance().shutdown();
    }

    public void testGetSingletonServiceBuilder()
    {
        ComponentProxy builder = ComponentProxyFactory.getInstance().getComponentProxy();

        assertNotNull(builder);

        ComponentProxy builder2 = ComponentProxyFactory.getInstance().getComponentProxy();

        assertSame(builder, builder2);
    }

    public void testGetDefaultServiceBuilder()
    {
        ComponentProxy builder = ComponentProxyFactory.getInstance().getComponentProxy();

        assertNotNull(builder);
    }

    public void testGetCustomServiceBuilder()
    {
        ComponentProxyFactory.getInstance().cleanup();

        Gravity.getInstance().setProperty(Gravity.COMPONENT_PROXY_CLASS_NAME,
            "gravity.impl.CglibComponentProxy");

        ComponentProxy builder = ComponentProxyFactory.getInstance().getComponentProxy();

        assertNotNull(builder);
    }

    public void testInvalidServiceBuilderClass()
    {
        ComponentProxyFactory.getInstance().cleanup();

        Gravity.getInstance().setProperty(Gravity.COMPONENT_PROXY_CLASS_NAME,
            "gravity.plugins.BshPlugin");

        try
        {
            ComponentProxyFactory.getInstance().getComponentProxy();

            unreachable();
        }
        catch (Exception e)
        {
            assertSuperString(e, "gravity.plugins.BshPlugin must implement gravity.ComponentProxy");
        }
    }
}