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
import gravity.MutableRegistry;
import gravity.impl.DefaultRegistry;
import gravity.plugins.BshPlugin;
import gravity.util.ClassUtils;

import java.util.Properties;

/**
 * @author Harish Krishnaswamy
 * @version $Id: BshPluginTest.java,v 1.1 2004-05-10 17:29:13 harishkswamy Exp $
 */
public class BshPluginTest extends GravityTestCase
{
    private MutableRegistry _registry;

    public void setUp()
    {
        Gravity.initialize();
        _registry = new DefaultRegistry();
    }

    public void tearDown()
    {
        writePluginFile("");
        Gravity.shutdown();
    }

    private void startup(String moduleNames)
    {
        Properties props = new Properties();
        props.setProperty(BshPlugin.MODULE_NAMES, moduleNames);

        new BshPlugin().startup(props, _registry);
    }

    public void testStartupEmptyModule()
    {
        startup("gravity/impl/empty.mdl.bsh");

        assertNotNull(_registry);
    }

    public void testBuildMultipleModules()
    {
        startup("gravity/impl/empty.mdl.bsh, gravity.mdl.bsh");

        assertNotNull(_registry);
    }

    public void testBuildNonExistentModule()
    {
        try
        {
            startup("gravity/impl/non-existent.mdl.bsh");

            unreachable();
        }
        catch (Exception e)
        {
            assertSuperString(e, "Cannot find resource: gravity/impl/non-existent.mdl.bsh");
        }
    }

    public void testBlankModuleName()
    {
        startup("\t, , \n, gravity.mdl.bsh");

        assertNotNull(_registry);
    }

    public void testConfigExecError()
    {
        String fPath = "gravity/mocks/runtime/";
        String fName = "test$temp.mdl.bsh";

        String absPath = ClassUtils.getResource(fPath).getFile() + fName;

        writeFile(absPath, "System.out.println(component(List.class));");

        try
        {
            startup(fPath + fName);

            unreachable();
        }
        catch (Exception e)
        {
            assertSuperString(e, "Registry configuration execution error in module: "
                + ClassUtils.getResource(fPath + fName));
        }
    }

    public void testConfigError()
    {
        String fPath = "gravity/mocks/runtime/";
        String fName = "test$temp.mdl.bsh";

        String absPath = ClassUtils.getResource("gravity/mocks/runtime/").getFile() + fName;

        writeFile(absPath, "component(NonExistent.class);");

        try
        {
            startup(fPath + fName);

            unreachable();
        }
        catch (Exception e)
        {
            assertSuperString(e, "Registry configuration error in module: "
                + ClassUtils.getResource(fPath + fName));
        }
    }
}