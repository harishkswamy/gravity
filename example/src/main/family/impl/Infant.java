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

import family.Parent;

/**
 * @author Harish Krishnaswamy
 * @version $Id: Infant.java,v 1.1 2004-06-14 04:26:00 harishkswamy Exp $
 */
public class Infant extends ChildImpl
{
    public Infant(String name, Parent father, Parent mother)
    {
        super(name, father, mother);
    }

    public void talk()
    {
        StringBuffer speech = new StringBuffer("pchh... pch pch...");

        System.out.println(speech);
    }
}