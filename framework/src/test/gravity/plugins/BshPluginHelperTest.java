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
import junit.framework.TestCase;

/**
 * @author Harish Krishnaswamy
 * @version $Id: BshPluginHelperTest.java,v 1.4 2004-06-14 04:24:29 harishkswamy Exp $
 */
public class BshPluginHelperTest extends TestCase
{
    private BshPluginHelper _helper = new BshPluginHelper();

    public void testNewComponentCallbackForEmptyArgs()
    {
        ComponentCallback callback = _helper.newComponentCallback(new Object[0],
            ComponentPhase.INJECTION);
        
        assertNull(callback);
    }

    public void testNewInjectionCallback()
    {
        ComponentCallback mthd = _helper.newInjectionCallback(new Object[]{"someMethod",
            new Object(), new Integer(3)});

        assertTrue(mthd.getName().equals("someMethod"));
        assertTrue(mthd.getArguments().length == 2);
        assertTrue(mthd.getComponentPhase() == ComponentPhase.INJECTION);
    }

    public void testNewStartUpCallback()
    {
        ComponentCallback mthd = _helper.newStartupCallback(new Object[]{"someMethod",
            new Object(), new Integer(3)});

        assertTrue(mthd.getName().equals("someMethod"));
        assertTrue(mthd.getArguments().length == 2);
        assertTrue(mthd.getComponentPhase() == ComponentPhase.START_UP);
    }

    public void testNewShutdownCallback()
    {
        ComponentCallback mthd = _helper.newShutdownCallback(new Object[]{"someMethod",
            new Object(), new Integer(3)});

        assertTrue(mthd.getName().equals("someMethod"));
        assertTrue(mthd.getArguments().length == 2);
        assertTrue(mthd.getComponentPhase() == ComponentPhase.SHUTDOWN);
    }

    public void testToComponentArrayCallback()
    {
        Object[] Objs = new Object[2];

        Objs[0] = new ComponentCallback("name1", new Object[]{"1"}, ComponentPhase.INJECTION);
        Objs[1] = new ComponentCallback("name2", new Object[]{"2"}, ComponentPhase.INJECTION);

        ComponentCallback[] callbacks = _helper.toComponentCallbackArray(Objs);

        assertTrue(callbacks.length == 2);
    }
}