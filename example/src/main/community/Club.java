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

package community;

import family.Parent;
import family.Spouse;
import gravity.Container;
import gravity.impl.DefaultContainer;
import gravity.impl.DefaultContext;
import gravity.util.ClassUtils;

import java.net.URL;

/**
 * @author Harish Krishnaswamy
 * @version $Id: Club.java,v 1.2 2004-11-17 20:24:21 harishkswamy Exp $
 */
public class Club
{
    public static void main(String[] args)
    {
        URL url = ClassUtils.getResource("springfield.gravity.properties");

        Container container = new DefaultContainer(new DefaultContext(url));

        Object homerKey = container.getComponentKey(Spouse.class, "homer");
        Spouse homer = (Spouse) container.getComponentInstance(homerKey);

        System.out.println("This is Homer talking ...");
        homer.talk();

        System.out.println("This is Homer's spouse talking ...");
        homer.getSpouse().talk();

        Object margeKey = container.getComponentKey(Parent.class, "marge");
        Parent marge = (Parent) container.getComponentInstance(margeKey);

        System.out.println("This is parent Marge talking ...");
        marge.talk();

        System.out.println("This is parent Marge making her children talk ...");
        marge.makeChildrenTalk();
    }
}