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

import gravity.ComponentCallback;
import gravity.ComponentPhase;

import java.util.Arrays;
import java.util.List;

/**
 * @author Harish Krishnaswamy
 * @version $Id: BshPluginHelper.java,v 1.2 2004-05-27 03:33:48 harishkswamy Exp $
 */
public class BshPluginHelper
{
    public ComponentCallback newComponentMethod(Object[] args, ComponentPhase phase)
    {
        if (args == null || args.length == 0)
            return null;

        String methodName = (String) args[0];

        Object[] mthdArgs = new Object[args.length - 1];

        for (int i = 1; i < args.length; i++)
            mthdArgs[i - 1] = args[i];

        return new ComponentCallback(methodName, mthdArgs, phase);
    }

    public ComponentCallback newInjectionMethod(Object[] args)
    {
        return newComponentMethod(args, ComponentPhase.INJECTION);
    }

    public ComponentCallback newStartupMethod(Object[] args)
    {
        return newComponentMethod(args, ComponentPhase.START_UP);
    }

    public ComponentCallback newShutdownMethod(Object[] args)
    {
        return newComponentMethod(args, ComponentPhase.SHUTDOWN);
    }

    public ComponentCallback[] toComponentCallbackArray(Object[] methods)
    {
        List methodList = Arrays.asList(methods);
        
        return(ComponentCallback[]) methodList.toArray(new ComponentCallback[0]);
    }
}