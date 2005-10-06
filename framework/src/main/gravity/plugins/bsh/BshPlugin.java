// Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package gravity.plugins.bsh;

import gravity.Context;
import gravity.ExceptionWrapper;
import gravity.Location;
import gravity.Plugin;
import gravity.plugins.MutableContainerAdapter;
import gravity.util.Message;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import bsh.EvalError;
import bsh.Interpreter;
import bsh.TargetError;

/**
 * @author Harish Krishnaswamy
 * @version $Id: BshPlugin.java,v 1.3 2005-10-06 21:59:24 harishkswamy Exp $
 */
public class BshPlugin implements Plugin
{
    /**
     * This is name of the property that specifies the qualified names of all the modules to be
     * added to the {@link gravity.Container}. The module paths are relative to the location of the
     * plugin manifest file. Module names are separated by commas and may be optionally enclosed in
     * quotes (").
     */
    public static final String      MODULES_KEY = "plugin.modules.path";

    private Context                 _context;
    private Interpreter             _interpreter;
    private MutableContainerAdapter _containerAdapter;

    private void handleInterpreterException(Throwable e, URL url)
    {
        while (e instanceof TargetError)
            e = ((TargetError) e).getTarget();

        Location loc = new Location(url.toString(), _containerAdapter.getCurrentLineNumber());

        ExceptionWrapper exWrapper = _context.getExceptionWrapper();

        if (e instanceof EvalError)
            throw exWrapper.wrap(e, Message.PLUGIN_SPECIFICATION_ERROR, loc);

        else if (e instanceof FileNotFoundException)
            throw exWrapper.wrap(e, Message.CANNOT_FIND_MODULE, url);

        throw exWrapper.wrap(e, Message.PLUGIN_SPECIFICATION_EXECUTION_ERROR, loc);
    }

    private void buildModule(URL url)
    {
        try
        {
            Context ctx = _context.decorateApplicationContext(url.toString(), null);

            _containerAdapter.setCurrentModuleName(url.toString());

            // Set current context in current namespace
            _interpreter.set("$context$", ctx.getDecoratedContext());

            _interpreter.set("$url$", url);
            _interpreter.eval("evalFile($url$, $container$)");

            ctx.stripApplicationContext();
        }
        catch (Throwable e)
        {
            handleInterpreterException(e, url);
        }
    }

    private void initializeInterpreter()
    {
        try
        {
            _interpreter = new Interpreter();

            URL url = _context.getClassUtils().getResource("gravity/plugins/bsh/init.bsh");
            Reader reader = new InputStreamReader(url.openStream());
            _interpreter.eval(reader);

            // Mix in the BshPluginHelper object with the current context/namespace
            _interpreter.set("$helper$", new BshPluginHelper(_context, _containerAdapter));
            _interpreter.eval("importObject($helper$)");

            // Mix in the MutableContainerAdapter object with the current context/namespace
            _interpreter.set("$container$", _containerAdapter);
            _interpreter.eval("importObject($container$)");

            // Import all commands in package gravity.plugins
            _interpreter.eval("importCommands(\"gravity.plugins.bsh\")");
        }
        catch (Exception e)
        {
            handleInterpreterException(e, null);
        }
    }

    protected MutableContainerAdapter newMutableContainerAdapter()
    {
        Class clazz = MutableContainerAdapter.class;

        MutableContainerAdapter adapter = (MutableContainerAdapter) _context.newApiInstance(clazz);
        adapter.initialize(_context.getMutableContainer());

        return adapter;
    }

    public void startup(Context context)
    {
        _context = context;

        _containerAdapter = newMutableContainerAdapter();

        String modules = (String) context.getContextItem(MODULES_KEY);

        List pathList = _context.getUtils().splitQuoted(modules, ',');

        initializeInterpreter();

        for (Iterator itr = pathList.iterator(); itr.hasNext();)
        {
            String pathStr = (String) itr.next();

            if (_context.getUtils().isBlank(pathStr))
                continue;

            pathStr = context.getContextItem(MANIFEST_LOCATION_URL_KEY) + pathStr;

            URL url = _context.getClassUtils().newUrl(pathStr);

            buildModule(url);
        }
    }
}