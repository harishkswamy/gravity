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

import family.Child;
import family.Parent;
import family.Spouse;
import gravity.ComponentKey;
import gravity.ComponentProxy;
import gravity.Container;
import gravity.MutableContainer;
import gravity.impl.CglibComponentProxy;
import gravity.impl.DefaultApplicationContext;

/**
 * @author Harish Krishnaswamy
 * @version $Id: Club.java,v 1.3 2006-06-21 16:51:04 harishkswamy Exp $
 */
public class Club
{
    public static void main(String[] args)
    {
        DefaultApplicationContext ctx = new DefaultApplicationContext("Simpsons Context", null);

        ctx.initializeFramework();

        ctx.putContextItem(ComponentProxy.class, CglibComponentProxy.class);
        ctx.putContextItem(Container.PLUGINS_AUTOLOAD_KEY, Boolean.FALSE);
        ctx.putContextItem(Container.PLUGINS_MANIFEST_CLASSPATH_KEY,
            "META-INF/simpsons-gravity-plugin.properties");

        MutableContainer container = ctx.getMutableContainer();

        container.load();

        ComponentKey homerKey = container.getComponentKey(Spouse.class, "homer");
        Spouse homer = (Spouse) container.getComponentInstance(homerKey);

        System.out.println("This is Homer talking ...");
        homer.talk();

        System.out.println("This is Homer's spouse talking ...");
        homer.getSpouse().talk();

        ComponentKey margeKey = container.getComponentKey(Parent.class, "marge");
        Parent marge = (Parent) container.getComponentInstance(margeKey);

        System.out.println("This is parent Marge talking ...");
        marge.talk();

        System.out.println("This is parent Marge making her children talk ...");
        marge.makeChildrenTalk();

        System.out.println("Thread Local test ...");

        ComponentKey tlMaggieKey = container.getComponentKey(Child.class, "tlMaggie");

        Child tlMaggie = (Child) container.getComponentInstance(tlMaggieKey);
        System.out.println(tlMaggie);

        tlMaggie = (Child) container.getComponentInstance(tlMaggieKey);
        System.out.println(tlMaggie);

        container.handlePreThreadTermination();

        tlMaggie = (Child) container.getComponentInstance(tlMaggieKey);
        System.out.println(tlMaggie);

        System.out.println("Pooling test ...");

        ComponentKey pMaggieKey = container.getComponentKey(Child.class, "pMaggie");

        Child pMaggie = (Child) container.getComponentInstance(pMaggieKey);
        System.out.println(pMaggie);

        pMaggie = (Child) container.getComponentInstance(pMaggieKey);
        System.out.println(pMaggie);

        container.collectComponentInstance(pMaggieKey, pMaggie);
        pMaggie = (Child) container.getComponentInstance(pMaggieKey);
        System.out.println(pMaggie);
    }
}