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

package gravity.util;

import gravity.GravityTestCase;

import java.util.List;

/**
 * @author  Harish Krishnaswamy
 * @version $Id: UtilsTest.java,v 1.1 2004-05-10 17:29:12 harishkswamy Exp $
 */
public class UtilsTest extends GravityTestCase
{
    public void testSplitQuoted()
    {
        List tokens = Utils.splitQuoted("\"quoted text\", \"another quoted text\"", ',');
        
        assertTrue(tokens.size() == 2);
        assertEquals(tokens.get(0), "\"quoted text\"");
        assertEquals(tokens.get(1), "\"another quoted text\"");
    }

    public void testSplitNullQuoted()
    {
        List tokens = Utils.splitQuoted(null, ',');
        
        assertTrue(tokens.size() == 0);
    }
    
    public void testIsBlank()
    {
        boolean flag = Utils.isBlank("");
        
        assertTrue(flag);
        
        flag = Utils.isBlank(null);

        assertTrue(flag);
    }
    
    public void testNotBlank()
    {
        boolean flag = Utils.isBlank("xyz");
        
        assertFalse(flag);
    }
}
