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
import family.Spouse;

/**
 * @author Harish Krishnaswamy
 * @version $Id: Mother.java,v 1.1 2004-06-14 04:26:02 harishkswamy Exp $
 */
public class Mother extends SpouseImpl implements Parent
{
    private Child[] _children;

    public Mother()
    {
        // This is an empty constructor.
    }

    public Mother(String name, Spouse husband, Child[] children)
    {
        _name = name;
        _spouse = husband;
        _children = children;
    }

    public void talk()
    {
        System.out.println("===================================================");
        System.out.println(this);
        System.out.println("My ID is: " + super.toString());
        System.out.println("===================================================");
    }

    public String introduceChildren()
    {
        StringBuffer buf = new StringBuffer();

        for (int i = 0; i < _children.length - 1; i++)
        {
            buf.append(_children[i].getName() + ", ");
        }

        return buf.append(_children[_children.length - 1].getName()).toString();
    }

    public void makeChildrenTalk()
    {
        System.out.println("===================================================");
        for (int i = 0; i < _children.length; i++)
        {
            System.out.print(_children[i].getName() + ": ");
            _children[i].talk();
        }
        System.out.println("===================================================");
    }

    public String toString()
    {
        return "My name is " + _name + ". My husband is " + _spouse.getName()
            + ". My children are: " + introduceChildren();
    }
}