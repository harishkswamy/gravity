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
 * @version $Id: GravityTest.java,v 1.1 2004-05-10 17:29:08 harishkswamy Exp $
 */
public class GravityTest extends GravityTestCase
{
    public void tearDown()
    {
        Gravity.shutdown();
    }

    public void testInitialize()
    {
        Gravity.initialize();

        String someVal = Gravity.getProperty("some prop");

        assertNull(someVal);
    }

    public void testReinitialize()
    {
        Gravity.initialize();
        Gravity.initialize();
    }
    
    public void testStartup()
    {
        Registry registry = Gravity.startup();

        assertNotNull(registry);

        registry.cleanup();
    }
    
    public void testCustomPlugin()
    {
        writePluginFile("pluginClassName=gravity.plugins.BshPlugin");
        
        Registry registry = Gravity.startup();
        
        assertNotNull(registry);
        
        writePluginFile("");
    }

    public void testGlobalPlugin()
    {
        String fPath = ClassUtils.getResource("gravity").getFile() + "/gravity.properties";
        
        writeFile(fPath, "pluginClassName=gravity.plugins.BshPlugin");
        
        Registry registry = Gravity.startup("gravity/gravity.properties");
        
        assertNotNull(registry);
        
        writeFile(fPath, "");
    }

    public void testInitializeFromNonExistentURL()
    {
        URL url = null;

        try
        {
            url = new URL("file:/C:/eclipse/workspace/gravity/Non-existent.properties");

            Gravity.initialize(url);

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
            Gravity.initialize("Non-existent.properties");

            unreachable();
        }
        catch (Exception e)
        {
            assertSuperString(e, "Cannot find resource: Non-existent.properties");
        }
    }

    public void testGravityProperties()
    {
        Gravity.initialize();

        Gravity.setProperty("myProp", "myVal");

        String myVal = Gravity.getProperty("myProp");

        assertEquals("myVal", myVal);

        myVal = Gravity.getProperty("non-existent val");

        assertNull(myVal);
    }

    public void testAccessUninitializedGravityProperties()
    {
        try
        {
            Gravity.setProperty("myprop", "myval");

            unreachable();
        }
        catch (Exception e)
        {
            assertSuperString(e, "Gravity should be initialized prior to accessing its properties");
        }

        try
        {
            Gravity.getProperty("myprop");

            unreachable();
        }
        catch (Exception e)
        {
            assertSuperString(e, "Gravity should be initialized prior to accessing its properties");
        }
    }
}