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

package gravity;

/**
 * This is a callback that users can use to plugin some code at different phases in the life-cycle
 * of the component. The various phases at which callbacks can be plugged are defined in
 * {@link gravity.ComponentPhase}.
 * 
 * @see gravity.ComponentPhase
 * @author Harish Krishnaswamy
 * @version $Id: ComponentCallback.java,v 1.5 2005-10-06 21:59:19 harishkswamy Exp $
 */
public class ComponentCallback
{
    /**
     * This is the name of the callback.
     */
    private String         _name;

    /**
     * This is an array of arguments to be passed to the callback.
     */
    private Object[]       _arguments;

    /**
     * This is the component phase in which this callback is applicable.
     */
    private ComponentPhase _componentPhase;

    public ComponentCallback()
    {
        // Constructor to be used only for testing to create CgLib class proxy.
    }
    
    public ComponentCallback(String name, Object[] args, ComponentPhase phase)
    {
        _name = name;
        _arguments = args;
        _componentPhase = phase;
    }

    public String getName()
    {
        return _name;
    }

    public Object[] getArguments()
    {
        return _arguments;
    }

    public ComponentPhase getComponentPhase()
    {
        return _componentPhase;
    }

    public String toString()
    {
        // Arguments are not returned cause if any of them are proxies, they will be realized
        // inappropriately
        return "[Name: " + _name + ", Component Phase: " + _componentPhase + "]";
    }
}