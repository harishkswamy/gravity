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

import gravity.ComponentProxyFactory;
import gravity.Gravity;
import gravity.GravityTestCase;

/**
 * @author Harish Krishnaswamy
 * @version $Id: ComponentProxyFactoryAgentTest.java,v 1.1 2004-05-10 17:28:45 harishkswamy Exp $
 */
public class ComponentProxyFactoryAgentTest extends GravityTestCase
{
    public void setUp()
    {
        Gravity.initialize();
    }

    public void tearDown()
    {
        Gravity.shutdown();
    }

    public void testGetSingletonServiceBuilder()
    {
        ComponentProxyFactory builder = ComponentProxyFactoryAgent.getComponentProxyFactory();

        assertNotNull(builder);

        ComponentProxyFactory builder2 = ComponentProxyFactoryAgent.getComponentProxyFactory();

        assertSame(builder, builder2);
    }

    public void testGetDefaultServiceBuilder()
    {
        ComponentProxyFactory builder = ComponentProxyFactoryAgent.getComponentProxyFactory();

        assertNotNull(builder);
    }

    public void testGetCustomServiceBuilder()
    {
        ComponentProxyFactoryAgent.cleanup();

        Gravity.setProperty(Gravity.COMPONENT_PROXY_FACTORY_CLASS_NAME,
            "gravity.impl.CglibComponentProxyFactory");

        ComponentProxyFactory builder = ComponentProxyFactoryAgent.getComponentProxyFactory();

        assertNotNull(builder);
    }

    public void testInvalidServiceBuilderClass()
    {
        ComponentProxyFactoryAgent.cleanup();

        Gravity.setProperty(Gravity.COMPONENT_PROXY_FACTORY_CLASS_NAME, "gravity.plugins.BshPlugin");

        try
        {
            ComponentProxyFactoryAgent.getComponentProxyFactory();

            unreachable();
        }
        catch (Exception e)
        {
            assertSuperString(e,
                "gravity.plugins.BshPlugin must implement gravity.ComponentProxyFactory");
        }
    }
}