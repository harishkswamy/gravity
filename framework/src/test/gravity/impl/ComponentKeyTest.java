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

package gravity.impl;

import gravity.GravityTestCase;

import java.util.List;

/**
 * @author Harish Krishnaswamy
 * @version $Id: ComponentKeyTest.java,v 1.2 2004-05-17 03:03:53 harishkswamy Exp $
 */
public class ComponentKeyTest extends GravityTestCase
{
    public void testNewComponentKey()
    {
        ComponentKey key1 = new ComponentKey(List.class, "def");
        ComponentKey key2 = new ComponentKey(List.class, "def");

        assertEquals(key1, key2);

        key1 = new ComponentKey(List.class, null);

        assertNotNull(key1);
    }

    public void testNullComponentInterface()
    {
        try
        {
            new ComponentKey(null, "def");

            unreachable();
        }
        catch (Exception e)
        {
            assertSuperString(e, "Component interface must not be null.");
        }
    }

    public void testEquals()
    {
        ComponentKey key1 = new ComponentKey(List.class, "def");
        ComponentKey key2 = new ComponentKey(List.class, "def");

        assertTrue(key1.equals(key1));
        assertFalse(key1.equals(null));
        assertTrue(key1.equals(key2));

        ComponentKey key3 = new ComponentKey(List.class, null);
        assertFalse(key1.equals(key3));
    }
}