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

package family.impl;

import family.Child;
import family.Parent;

/**
 * @author Harish Krishnaswamy
 * @version $Id: ChildImpl.java,v 1.1 2004-06-14 04:26:02 harishkswamy Exp $
 */
public abstract class ChildImpl extends PersonImpl implements Child
{
    protected Parent _father;
    protected Parent _mother;

    public ChildImpl(String name, Parent father, Parent mother)
    {
        _name = name;
        _father = father;
        _mother = mother;
    }

    public String toString()
    {
        return "[Name: " + _name + ", Father: " + _father + ", Mother: " + _mother + ".]";
    }
}