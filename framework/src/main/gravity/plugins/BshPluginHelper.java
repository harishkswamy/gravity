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

import gravity.ComponentLifeCycleMethod;
import gravity.ComponentLifeCyclePhase;

import java.util.Arrays;
import java.util.List;

/**
 * @author Harish Krishnaswamy
 * @version $Id: BshPluginHelper.java,v 1.1 2004-05-24 00:38:41 harishkswamy Exp $
 */
public class BshPluginHelper
{
    public ComponentLifeCycleMethod newComponentMethod(Object[] args, ComponentLifeCyclePhase stage)
    {
        if (args == null || args.length == 0)
            return null;

        String methodName = (String) args[0];

        Object[] mthdArgs = new Object[args.length - 1];

        for (int i = 1; i < args.length; i++)
            mthdArgs[i - 1] = args[i];

        return new ComponentLifeCycleMethod(methodName, mthdArgs, stage);
    }

    public ComponentLifeCycleMethod newInjectionMethod(Object[] args)
    {
        return newComponentMethod(args, ComponentLifeCyclePhase.INJECTION);
    }

    public ComponentLifeCycleMethod newStartUpMethod(Object[] args)
    {
        return newComponentMethod(args, ComponentLifeCyclePhase.START_UP);
    }

    public ComponentLifeCycleMethod newShutdownMethod(Object[] args)
    {
        return newComponentMethod(args, ComponentLifeCyclePhase.SHUTDOWN);
    }

    public ComponentLifeCycleMethod[] toComponentLifeCycleMethodArray(Object[] methods)
    {
        List methodList = Arrays.asList(methods);
        
        return(ComponentLifeCycleMethod[]) methodList.toArray(new ComponentLifeCycleMethod[0]);
    }
}