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

import gravity.impl.CglibComponentProxyFactoryTest;
import gravity.impl.ComponentBuilderTest;
import gravity.impl.ComponentKeyTest;
import gravity.impl.ComponentProxyFactoryAgentTest;
import gravity.impl.DefaultComponentFactoryTest;
import gravity.impl.DefaultRegistryTest;
import gravity.impl.DynamicWeaverFactoryTest;
import gravity.impl.JdkComponentProxyFactoryTest;
import gravity.impl.PooledComponentFactoryTest;
import gravity.impl.SingletonComponentFactoryTest;
import gravity.plugins.BshPluginTest;
import gravity.plugins.MutableRegistryAdapterTest;
import gravity.util.ClassUtilsTest;
import gravity.util.ReflectUtilsTest;
import gravity.util.UtilsTest;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Harish Krishnaswamy
 * @version $Id: GravityTestSuite.java,v 1.1 2004-05-10 17:29:09 harishkswamy Exp $
 */
public class GravityTestSuite
{

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(GravityTestSuite.suite());
    }

    public static Test suite()
    {
        TestSuite suite = new TestSuite("Tests for gravity framework");

        //$JUnit-BEGIN$

        // gravity package tests
        suite.addTestSuite(GravityTest.class);
        suite.addTestSuite(WrapperExceptionTest.class);

        // gravity.impl package tests
        suite.addTestSuite(CglibComponentProxyFactoryTest.class);
        suite.addTestSuite(ComponentBuilderTest.class);
        suite.addTestSuite(ComponentKeyTest.class);
        suite.addTestSuite(ComponentProxyFactoryAgentTest.class);
        suite.addTestSuite(DefaultComponentFactoryTest.class);
        suite.addTestSuite(DefaultRegistryTest.class);
        suite.addTestSuite(DynamicWeaverFactoryTest.class);
        suite.addTestSuite(JdkComponentProxyFactoryTest.class);
        suite.addTestSuite(PooledComponentFactoryTest.class);
        suite.addTestSuite(SingletonComponentFactoryTest.class);

        // gravity.plugins package tests
        suite.addTestSuite(BshPluginTest.class);
        suite.addTestSuite(MutableRegistryAdapterTest.class);

        // gravity.util package tests
        suite.addTestSuite(ClassUtilsTest.class);
        suite.addTestSuite(ReflectUtilsTest.class);
        suite.addTestSuite(UtilsTest.class);

        //$JUnit-END$

        return suite;
    }
}