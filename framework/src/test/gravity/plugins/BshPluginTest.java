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

package gravity.plugins;

import gravity.Gravity;
import gravity.GravityTestCase;
import gravity.MutableContainer;
import gravity.Plugin;
import gravity.impl.DefaultContainer;
import gravity.util.ClassUtils;

import java.util.Enumeration;
import java.util.Properties;

/**
 * @author Harish Krishnaswamy
 * @version $Id: BshPluginTest.java,v 1.5 2004-06-14 04:24:29 harishkswamy Exp $
 */
public class BshPluginTest extends GravityTestCase
{
    private MutableContainer _container;

    public void setUp()
    {
        Gravity.getInstance().initialize();
        _container = new DefaultContainer();
    }

    public void tearDown()
    {
        writePluginFile("");
        Gravity.getInstance().shutdown();
    }

    private String getPluginUrlStr()
    {
        Enumeration urls = ClassUtils.getResources(Gravity.PLUGIN_MANIFEST_FILE_PATH);

        String urlStr = null;

        while (urls.hasMoreElements())
        {
            urlStr = urls.nextElement().toString();

            if (urlStr.indexOf("test/META-INF") > -1)
                break;

            urlStr = null;
        }

        return urlStr.substring(0, urlStr.lastIndexOf(Gravity.PLUGIN_MANIFEST_FILE_PATH));
    }

    private void startup(String moduleNames)
    {
        String urlStr = getPluginUrlStr();

        Properties props = new Properties();
        props.setProperty(Plugin.LOCATION_URL_KEY, urlStr);
        props.setProperty(BshPlugin.MODULE_NAMES, moduleNames);

        new BshPlugin().startup(props, _container);
    }

    public void testStartupEmptyModule()
    {
        startup("gravity/impl/empty.mdl.bsh");

        assertNotNull(_container);
    }

    public void testBuildMultipleModules()
    {
        startup("gravity/impl/empty.mdl.bsh, gravity.mdl.bsh");

        assertNotNull(_container);
    }

    public void testBuildNonExistentModule()
    {
        String urlStr = getPluginUrlStr();
        String modPath = "gravity/impl/non-existent.mdl.bsh";

        try
        {
            startup(modPath);

            unreachable();
        }
        catch (Exception e)
        {
            assertSuperString(e, "Unable to find module: " + urlStr + modPath);
        }
    }

    public void testBlankModuleName()
    {
        startup("\t, , \n, gravity.mdl.bsh");

        assertNotNull(_container);
    }

    public void testConfigExecError()
    {
        String fPath = "gravity/mocks/runtime/";
        String fName = "test$temp.mdl.bsh";

        String absPath = ClassUtils.getResource(fPath).getFile() + fName;

        writeFile(absPath, "System.out.println(componentInst(List.class));");

        try
        {
            startup(fPath + fName);

            unreachable();
        }
        catch (Exception e)
        {
            assertSuperString(e, "Plugin specification execution error at: [Resource: "
                + ClassUtils.getResource(fPath + fName) + ", Line: 1]");
        }
    }

    public void testConfigError()
    {
        String fPath = "gravity/mocks/runtime/";
        String fName = "test$temp.mdl.bsh";

        String absPath = ClassUtils.getResource("gravity/mocks/runtime/").getFile() + fName;

        writeFile(absPath, "componentInst(NonExistent.class);");

        try
        {
            startup(fPath + fName);

            unreachable();
        }
        catch (Exception e)
        {
            assertSuperString(e, "Plugin specification error at: [Resource: "
                + ClassUtils.getResource(fPath + fName) + ", Line: 1]");
        }
    }
}