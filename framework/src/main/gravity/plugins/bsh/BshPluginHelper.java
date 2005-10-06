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

import gravity.ComponentCallback;
import gravity.ComponentPhase;
import gravity.Context;
import gravity.plugins.MutableContainerAdapter;

import java.util.Arrays;
import java.util.List;

/**
 * @author Harish Krishnaswamy
 * @version $Id: BshPluginHelper.java,v 1.2 2005-10-06 21:59:23 harishkswamy Exp $
 */
public class BshPluginHelper
{
    private Context                 _context;
    private MutableContainerAdapter _containerAdapter;

    public BshPluginHelper(Context context, MutableContainerAdapter adapter)
    {
        _context = context;
        _containerAdapter = adapter;
    }

    public ComponentCallback newComponentCallback(Object[] args, ComponentPhase phase)
    {
        if (args == null || args.length == 0)
            return null;

        String methodName = (String) args[0];

        Object[] mthdArgs = new Object[args.length - 1];

        for (int i = 1; i < args.length; i++)
            mthdArgs[i - 1] = args[i];

        return new ComponentCallback(methodName, mthdArgs, phase);
    }

    public ComponentCallback newInjectionCallback(Object[] args)
    {
        return newComponentCallback(args, ComponentPhase.INJECTION);
    }

    public ComponentCallback newStartupCallback(Object[] args)
    {
        return newComponentCallback(args, ComponentPhase.START_UP);
    }

    public ComponentCallback newShutdownCallback(Object[] args)
    {
        return newComponentCallback(args, ComponentPhase.SHUTDOWN);
    }

    public ComponentCallback[] toComponentCallbackArray(Object[] callbacks)
    {
        List methodList = Arrays.asList(callbacks);

        return (ComponentCallback[]) methodList.toArray(new ComponentCallback[callbacks.length]);
    }

    public Object registerConfigListItems(Object[] args)
    {
        if (args == null || args.length == 0)
            return null;

        Object configKey = args[0];

        Class valueClass = (Class) args[1];

        for (int i = 2; i < args.length; i++)
        {
            Object config = newObject(valueClass, args[i]);

            _containerAdapter.configItem(configKey, config);
        }

        return configKey;
    }

    public Object registerConfigMapItems(Object[] args)
    {
        if (args == null || args.length == 0)
            return null;

        Object configKey = args[0];

        Class keyClass = (Class) args[1];
        Class valueClass = (Class) args[2];

        for (int i = 3; i < args.length; i++)
        {
            Object key = newObject(keyClass, args[i]);

            Object value = newObject(valueClass, args[++i]);

            _containerAdapter.configItem(configKey, key, value);
        }

        return configKey;
    }

    private Object newObject(Class type, Object arg)
    {
        Object[] keyArgs;

        if (arg instanceof Object[])
            keyArgs = (Object[]) arg;
        else
            keyArgs = new Object[]{arg};

        return _context.getReflectUtils().invokeConstructor(type, keyArgs);
    }
}