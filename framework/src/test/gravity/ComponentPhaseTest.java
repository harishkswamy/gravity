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

import java.util.HashMap;
import java.util.Map;

/**
 * @author Harish Krishnaswamy
 * @version $Id: ComponentPhaseTest.java,v 1.1 2004-09-02 04:20:57 harishkswamy Exp $
 */
public class ComponentPhaseTest extends GravityTestCase
{
    public void testEquals()
    {
        assertTrue(ComponentPhase.INJECTION.equals(ComponentPhase.INJECTION));
        assertFalse(ComponentPhase.SHUTDOWN.equals(new Object()));

        ComponentPhase phase = new ComponentPhase("Injection");
        assertTrue(ComponentPhase.INJECTION.equals(phase));

        phase = new ComponentPhase(null);
        assertFalse(ComponentPhase.INJECTION.equals(phase));

        ComponentPhase phase2 = new ComponentPhase(null);
        assertTrue(phase.equals(phase2));
    }
    
    public void testHashCode()
    {
        Map map = new HashMap();
        
        map.put(ComponentPhase.START_UP, "starting up");
        assertTrue(map.get(ComponentPhase.START_UP).equals("starting up"));
        
        ComponentPhase phase = new ComponentPhase(null);
        map.put(phase, "null phase");
        assertTrue(map.get(phase).equals("null phase"));
    }
}
