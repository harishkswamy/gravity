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
 * @version $Id: LocationTest.java,v 1.2 2004-09-02 04:20:57 harishkswamy Exp $
 */
public class LocationTest extends GravityTestCase
{
    public void testEqualsAndHashcode()
    {
        Location loc1 = new Location("rsrc", 10);
        Location loc2 = new Location("rsrc", 10);

        assertTrue(loc1.equals(loc1));
        assertTrue(loc1.equals(loc2));
        assertFalse(loc1.equals(new Object()));
        assertFalse(loc1.equals(new Location("rsrc", 20)));

        assertEquals(loc1.hashCode(), loc2.hashCode());
        assertFalse(loc1.hashCode() == new Location(null, 10).hashCode());
    }

    public void testToString()
    {
        Location loc1 = new Location("rsrc", 10);

        assertEquals(loc1.toString(), "[Resource: rsrc, Line: 10]");
    }
}