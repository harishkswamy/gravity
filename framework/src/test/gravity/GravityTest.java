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

package gravity;

import gravity.util.ClassUtils;

import java.net.URL;

/**
 * @author Harish Krishnaswamy
 * @version $Id: GravityTest.java,v 1.4 2004-06-14 04:24:28 harishkswamy Exp $
 */
public class GravityTest extends GravityTestCase
{
    public void tearDown()
    {
        Gravity.getInstance().shutdown();
    }

    public void testInitialize()
    {
        Gravity.getInstance().initialize();

        String someVal = Gravity.getInstance().getProperty("some prop");

        assertNull(someVal);
    }

    public void testReinitialize()
    {
        Gravity.getInstance().initialize();

        try
        {
            Gravity.getInstance().initialize();
        }
        catch (Exception e)
        {
            assertSuperString(e, "Gravity is already initialized, use setProperty() to add "
                + "properties to Gravity.");
        }
    }

    public void testStartup()
    {
        Container registry = Gravity.getInstance().startup();

        assertNotNull(registry);

        registry.cleanup();
    }

    public void testCustomPlugin()
    {
        writePluginFile("pluginClassName=gravity.plugins.BshPlugin");

        Container registry = Gravity.getInstance().startup();

        assertNotNull(registry);

        writePluginFile("");
    }

    public void testGlobalPlugin()
    {
        String fPath = ClassUtils.getResource("gravity").getFile() + "/gravity.properties";

        writeFile(fPath, "pluginClassName=gravity.plugins.BshPlugin");

        Container registry = Gravity.getInstance().startup("gravity/gravity.properties");

        assertNotNull(registry);

        writeFile(fPath, "");
    }

    public void testInitializeFromNonExistentURL()
    {
        URL url = null;

        try
        {
            url = new URL("file:/C:/eclipse/workspace/gravity/Non-existent.properties");

            Gravity.getInstance().initialize(url);

            unreachable();
        }
        catch (Exception e)
        {
            assertSuperString(e, "Cannot load properties from " + url);
        }
    }

    public void testInitializeFromNonExistentPath()
    {
        try
        {
            Gravity.getInstance().initialize("Non-existent.properties");

            unreachable();
        }
        catch (Exception e)
        {
            assertSuperString(e, "Cannot find resource: Non-existent.properties");
        }
    }
    
    public void testGetContainer()
    {
        Container container = Gravity.getInstance().startup();
        
        Container container2 = Gravity.getInstance().getContainer();
        
        assertSame(container, container2);
    }

    public void testGravityProperties()
    {
        Gravity.getInstance().initialize();

        Gravity.getInstance().setProperty("myProp", "myVal");

        String myVal = Gravity.getInstance().getProperty("myProp");

        assertEquals("myVal", myVal);

        myVal = Gravity.getInstance().getProperty("non-existent val");

        assertNull(myVal);
    }

    public void testAccessUninitializedGravityProperties()
    {
        try
        {
            Gravity.getInstance().setProperty("myprop", "myval");

            unreachable();
        }
        catch (Exception e)
        {
            assertSuperString(e, "Gravity should be initialized prior to accessing its properties");
        }

        try
        {
            Gravity.getInstance().getProperty("myprop");

            unreachable();
        }
        catch (Exception e)
        {
            assertSuperString(e, "Gravity should be initialized prior to accessing its properties");
        }
    }
}