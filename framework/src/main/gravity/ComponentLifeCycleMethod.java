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

import gravity.util.Utils;

/**
 * @author Harish Krishnaswamy
 * @version $Id: ComponentLifeCycleMethod.java,v 1.1 2004-05-24 00:38:43 harishkswamy Exp $
 */
public class ComponentLifeCycleMethod
{
    private String                  _name;
    private Object[]                _arguments;
    private ComponentLifeCyclePhase _lifeCyclePhase;

    public ComponentLifeCycleMethod(String name, Object[] args, ComponentLifeCyclePhase phase)
    {
        _name = name;
        _arguments = args;
        _lifeCyclePhase = phase;
    }

    public String getName()
    {
        return _name;
    }

    public Object[] getArguments()
    {
        return _arguments;
    }

    public ComponentLifeCyclePhase getLifeCyclePhase()
    {
        return _lifeCyclePhase;
    }

    public String toString()
    {
        return "[Name: " + _name + ", Arguments: " + Utils.arrayToString(_arguments)
            + ", Life Cycle Phase: " + _lifeCyclePhase + "]";
    }
}