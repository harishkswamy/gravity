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

package gravity.plugins.bsh;

import gravity.Location;
import gravity.MutableContainer;
import gravity.Plugin;
import gravity.WrapperException;
import gravity.plugins.MutableContainerAdapter;
import gravity.util.ClassUtils;
import gravity.util.Message;
import gravity.util.Utils;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import bsh.EvalError;
import bsh.Interpreter;
import bsh.TargetError;

/**
 * @author Harish Krishnaswamy
 * @version $Id: BshPlugin.java,v 1.2 2004-11-17 19:54:47 harishkswamy Exp $
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

    private void handleInterpreterException(Throwable e, URL url)
    {
        while (e instanceof TargetError)
            e = ((TargetError) e).getTarget();

        Location loc = new Location(url.toString(), _container.getCurrentLineNumber());

        if (e instanceof EvalError)
            throw WrapperException.wrap(e, Message.PLUGIN_SPECIFICATION_ERROR, loc);

        else if (e instanceof FileNotFoundException)
            throw WrapperException.wrap(e, Message.CANNOT_FIND_MODULE, url);

        else if (e instanceof InvocationTargetException)
            e = ((InvocationTargetException) e).getTargetException();

        throw WrapperException.wrap(e, Message.PLUGIN_SPECIFICATION_EXECUTION_ERROR, loc);
    }

    private void buildModule(URL url)
    {
        try
        {
            _container.setCurrentModuleName(url.toString());

            _interpreter.set("$url$", url);
            _interpreter.eval("evalFile($url$, $container$)");
        }
        catch (Throwable e)
        {
            handleInterpreterException(e, url);
        }
    }

    private void initialize()
    {
        try
        {
            _interpreter = new Interpreter();

            URL url = ClassUtils.getResource("gravity/plugins/bsh/init.bsh");
            Reader reader = new InputStreamReader(url.openStream());
            _interpreter.eval(reader);

            // Mix in the BshPluginHelper object with the current context/namespace
            _interpreter.set("$helper$", new BshPluginHelper());
            _interpreter.eval("importObject($helper$)");

            // Mix in the MutableContainerAdapter object with the current context/namespace
            _interpreter.set("$container$", _container);
            _interpreter.eval("importObject($container$)");

            // Import all commands in package gravity.plugins
            _interpreter.eval("importCommands(\"gravity.plugins.bsh\")");
        }
        catch (Exception e)
        {
            handleInterpreterException(e, null);
        }
    }

    protected MutableContainerAdapter newMutableContainerAdapter(MutableContainer container)
    {
        return new MutableContainerAdapter(container);
    }

    public void startup(final Properties props, final MutableContainer container)
    {
        _container = newMutableContainerAdapter(container);

        String moduleNames = props.getProperty(MODULE_NAMES);

        List pathStrs = Utils.splitQuoted(moduleNames, ',');

        initialize();

        for (Iterator itr = pathStrs.iterator(); itr.hasNext();)
        {
            String pathStr = (String) itr.next();

            if (Utils.isBlank(pathStr))
                continue;

            pathStr = props.getProperty(LOCATION_URL_KEY) + pathStr;

            URL url = ClassUtils.newUrl(pathStr);

            buildModule(url);
        }
    }
}