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

import gravity.ComponentPhase;
import gravity.ComponentCallback;
import junit.framework.TestCase;

/**
 * @author Harish Krishnaswamy
 * @version $Id: BshPluginHelperTest.java,v 1.3 2004-05-27 05:32:26 harishkswamy Exp $
 */
public class BshPluginHelperTest extends TestCase
{
    private BshPluginHelper _helper = new BshPluginHelper();

    public void testNewDependencyMethod()
    {
        ComponentCallback mthd = _helper.newInjectionMethod(new Object[]{"someMethod", new Object(),
            new Integer(3)});

        assertTrue(mthd.getName().equals("someMethod"));
        assertTrue(mthd.getArguments().length == 2);
        assertTrue(mthd.getComponentPhase() == ComponentPhase.INJECTION);
    }

    public void testNewStartUpMethod()
    {
        ComponentCallback mthd = _helper.newStartupMethod(new Object[]{"someMethod", new Object(),
            new Integer(3)});

        assertTrue(mthd.getName().equals("someMethod"));
        assertTrue(mthd.getArguments().length == 2);
        assertTrue(mthd.getComponentPhase() == ComponentPhase.START_UP);
    }

    public void testNewShutdownMethod()
    {
        ComponentCallback mthd = _helper.newShutdownMethod(new Object[]{"someMethod", new Object(),
            new Integer(3)});

        assertTrue(mthd.getName().equals("someMethod"));
        assertTrue(mthd.getArguments().length == 2);
        assertTrue(mthd.getComponentPhase() == ComponentPhase.SHUTDOWN);
    }
}