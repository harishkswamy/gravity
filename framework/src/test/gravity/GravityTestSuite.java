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

import gravity.impl.CglibComponentProxyTest;
import gravity.impl.ComponentKeyTest;
import gravity.impl.ComponentProxyFactoryTest;
import gravity.impl.ComponentTest;
import gravity.impl.DefaultComponentTest;
import gravity.impl.DefaultContainerTest;
import gravity.impl.DynamicWeaverFactoryTest;
import gravity.impl.JdkComponentProxyTest;
import gravity.impl.PoolingComponentStrategyTest;
import gravity.impl.SingletonComponentStrategyTest;
import gravity.impl.ThreadLocalComponentStrategyTest;
import gravity.plugins.BshPluginTest;
import gravity.plugins.MutableRegistryAdapterTest;
import gravity.util.ClassUtilsTest;
import gravity.util.CleanableThreadLocalTest;
import gravity.util.ReflectUtilsTest;
import gravity.util.UtilsTest;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Harish Krishnaswamy
 * @version $Id: GravityTestSuite.java,v 1.5 2004-05-22 20:19:29 harishkswamy Exp $
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
        suite.addTestSuite(CglibComponentProxyTest.class);
        suite.addTestSuite(ComponentTest.class);
        suite.addTestSuite(ComponentKeyTest.class);
        suite.addTestSuite(ComponentProxyFactoryTest.class);
        suite.addTestSuite(DefaultComponentTest.class);
        suite.addTestSuite(DefaultContainerTest.class);
        suite.addTestSuite(DynamicWeaverFactoryTest.class);
        suite.addTestSuite(JdkComponentProxyTest.class);
        suite.addTestSuite(PoolingComponentStrategyTest.class);
        suite.addTestSuite(SingletonComponentStrategyTest.class);
        suite.addTestSuite(ThreadLocalComponentStrategyTest.class);

        // gravity.plugins package tests
        suite.addTestSuite(BshPluginTest.class);
        suite.addTestSuite(MutableRegistryAdapterTest.class);

        // gravity.util package tests
        suite.addTestSuite(ClassUtilsTest.class);
        suite.addTestSuite(CleanableThreadLocalTest.class);
        suite.addTestSuite(ReflectUtilsTest.class);
        suite.addTestSuite(UtilsTest.class);

        //$JUnit-END$

        return suite;
    }
}