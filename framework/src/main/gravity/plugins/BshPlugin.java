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

import gravity.MutableContainer;
import gravity.Plugin;
import gravity.WrapperException;
import gravity.util.ClassUtils;
import gravity.util.Utils;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import bsh.Interpreter;
import bsh.TargetError;

/**
 * @author Harish Krishnaswamy
 * @version $Id: BshPlugin.java,v 1.6 2004-05-24 00:38:41 harishkswamy Exp $
 */
public class BshPlugin implements Plugin
{
    /**
     * This is name of the property that specifies the qualified names of all the modules from which
     * to build the {@link gravity.Container}. The names are relative to the classpath. Module
     * names are separated by commas and may be optionally enclosed in quotes (").
     */
    public static final String      MODULE_NAMES = "moduleNames";

    private Interpreter             _interpreter;
    private MutableContainerAdapter _container;

    private void handleInterpreterException(Exception e, URL url)
    {
        if (e instanceof TargetError)
        {
            TargetError te = (TargetError) e;

            if (te.getTarget() instanceof FileNotFoundException)
                throw WrapperException.wrap(te, "Unable to find module: " + url + "\n\n" + e);

            else if (te.getTarget() instanceof TargetError)
                throw WrapperException.wrap(te,
                    "Container configuration execution error in module: " + url + "\n\n" + e);

            else
                throw WrapperException.wrap(e, "Container configuration error in module: " + url);
        }
        else
            throw WrapperException.wrap(e, "Container configuration error in module: " + url);
    }

    private void buildModule(URL url)
    {
        try
        {
            _container.setCurrentModuleName(url.toString());

            _interpreter.set("$url$", url);
            _interpreter.eval("evalFile($url$, $container$)");
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

            URL url = ClassUtils.getResource("gravity/plugins/init.bsh");
            Reader reader = new InputStreamReader(url.openStream());
            _interpreter.eval(reader);

            _interpreter.set("$helper$", new BshPluginHelper());
            _interpreter.eval("importObject($helper$)");

            _interpreter.set("$container$", _container);

            // Mix in the MutableContainerAdapter object with the current context/namespace
            _interpreter.eval("importObject($container$)");

            // Import all commands in package gravity.plugins
            _interpreter.eval("importCommands(\"gravity.plugins\")");
        }
        catch (Exception e)
        {
            handleInterpreterException(e, null);
        }
    }

    protected MutableContainerAdapter newMutableContainerAdapter(MutableContainer registry)
    {
        return new MutableContainerAdapter(registry);
    }

    public void startup(Properties pluginProps, MutableContainer registry)
    {
        _container = newMutableContainerAdapter(registry);

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