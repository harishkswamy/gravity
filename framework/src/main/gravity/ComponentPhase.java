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

/**
 * This is an enumerated type. It enumerates the stages of component lifecycle.
 * 
 * @author Harish Krishnaswamy
 * @version $Id: ComponentPhase.java,v 1.1 2004-05-27 03:36:29 harishkswamy Exp $
 */
public class ComponentPhase
{
    public static final ComponentPhase INJECTION = new ComponentPhase("Injection");
    public static final ComponentPhase START_UP  = new ComponentPhase("Start-Up");
    public static final ComponentPhase SHUTDOWN  = new ComponentPhase("Shutdown");

    private String                     _name;

    private ComponentPhase(String name)
    {
        _name = name;
    }

    public String toString()
    {
        return "[Name: " + _name + "]";
    }
}