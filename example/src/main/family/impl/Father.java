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

import java.util.List;

import family.Parent;
import family.Spouse;

/**
 * @author Harish Krishnaswamy
 * @version $Id: Father.java,v 1.1 2004-06-14 04:26:03 harishkswamy Exp $
 */
public class Father extends SpouseImpl implements Parent
{
    private List _bars;

    public Father()
    {
        // This is an empty constructor
    }

    public Father(String name, Spouse wife, List bars)
    {
        _name = name;
        _spouse = wife;
        _bars = bars;
    }

    public void talk()
    {
        System.out.println("===================================================");
        System.out.println(this + " " + introduceChildren());
        System.out.println("My ID is: " + super.toString());
        System.out.println("===================================================");
    }

    public String introduceChildren()
    {
        return "My children are ... uhh ... zzz";
    }

    public void makeChildrenTalk()
    {
        // Does nothing
    }

    public String toString()
    {
        return "My name is " + _name + ". My wife is " + _spouse.getName() + ". I love these bars: "
            + _bars + ".";
    }
}