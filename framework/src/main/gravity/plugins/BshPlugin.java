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

import gravity.MutableRegistry;
import gravity.Plugin;
import gravity.WrapperException;
import gravity.util.ClassUtils;
import gravity.util.Utils;

import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import bsh.CallStack;
import bsh.Interpreter;
import bsh.TargetError;

/**
 * @author Harish Krishnaswamy
 * @version $Id: BshPlugin.java,v 1.3 2004-05-13 06:27:04 harishkswamy Exp $
 */
public class BshPlugin implements Plugin
{
    /**
     * This is name of the property that specifies the qualified names of all the modules from which
     * to build the {@link gravity.Registry}. The names are relative to the classpath. Module names
     * are separated by commas and may be optionally enclosed in quotes (").
     */
    public static final String     MODULE_NAMES = "moduleNames";

    private Interpreter            _interpreter;
    private MutableRegistryAdapter _registry;

    private void handleInterpreterException(Exception e, URL url)
    {
        if (e instanceof TargetError && ((TargetError) e).getTarget() instanceof TargetError)
        {
            TargetError te = (TargetError) e;

            throw WrapperException.wrap(te, "Registry configuration execution error in module: "
                + url + "\n\n" + e);
        }
        else
            throw WrapperException.wrap(e, "Registry configuration error in module: " + url);
    }

    private void buildModule(URL url)
    {
        try
        {
            _registry.setCurrentModuleName(url.toString());

            _interpreter.set("$url$", url);
            _interpreter.eval("evalFile($url$, $registry$, $interpreter$, $callstack$)");
        }
        catch (Exception e)
        {
            handleInterpreterException(e, url);
        }
    }

    private void initialize()
    {
        try
        {
            _interpreter = new Interpreter();

            _interpreter.set("$registry$", _registry);

            // Mix in the MutableRegistryAdapter object with the current context/namespace
            _interpreter.eval("importObject($registry$)");

            // Import all commands in package gravity.impl
            _interpreter.eval("importCommands(\"gravity.impl\")");

            _interpreter.set("$interpreter$", _interpreter);
            _interpreter.set("$callstack$", new CallStack(_interpreter.getNameSpace()));
        }
        catch (Exception e)
        {
            handleInterpreterException(e, null);
        }
    }

    protected MutableRegistryAdapter newMutableRegistryAdapter(MutableRegistry registry)
    {
        return new MutableRegistryAdapter(registry);
    }

    public void startup(Properties pluginProps, MutableRegistry registry)
    {
        _registry = newMutableRegistryAdapter(registry);

        String moduleNames = pluginProps.getProperty(MODULE_NAMES);

        List pathStrs = Utils.splitQuoted(moduleNames, ',');

        initialize();

        for (Iterator itr = pathStrs.iterator(); itr.hasNext();)
        {
            String pathStr = (String) itr.next();

            if (Utils.isBlank(pathStr))
                continue;

            pathStr = pluginProps.getProperty(LOCATION_URL_KEY) + pathStr;

            URL url = ClassUtils.newUrl(pathStr);

            buildModule(url);
        }
    }
}