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
 * @author Harish Krishnaswamy
 * @version $Id: ComponentCallbackTest.java,v 1.1 2004-06-14 04:24:28 harishkswamy Exp $
 */
public class ComponentCallbackTest extends GravityTestCase
{
    public void testToString()
    {
        ComponentCallback callback = new ComponentCallback("name", new Object[]{"1"},
            ComponentPhase.INJECTION);
        
        assertSuperString("[Name: name, Component Phase: [Name: Injection]]", callback.toString());
    }
}