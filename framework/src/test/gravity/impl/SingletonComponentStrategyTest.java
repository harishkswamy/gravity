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

import gravity.ComponentStrategy;
import gravity.Gravity;
import gravity.GravityTestCase;
import gravity.RealizableComponent;
import gravity.mocks.MockComboService;
import gravity.mocks.MockComboServiceImpl;

import java.util.ArrayList;

/**
 * @author Harish Krishnaswamy
 * @version $Id: SingletonComponentStrategyTest.java,v 1.3 2004-09-02 04:20:56 harishkswamy Exp $
 */
public class SingletonComponentStrategyTest extends GravityTestCase
{
    public void setUp()
    {
        Gravity.getInstance().initialize();
    }

    public void tearDown()
    {
        Gravity.getInstance().shutdown();
    }

    private RealizableComponent newComponent(Class compIntf, Object compType)
    {
        ComponentKey compKey = new ComponentKey(compIntf, compType);

        RealizableComponent comp = new DefaultComponent(compKey);

        return comp;
    }

    private ComponentStrategy newSingletonComponent()
    {
        ComponentStrategy factory = new LazyLoadingComponentStrategy(null);

        return new SingletonComponentStrategy(factory);
    }

    public void testBuildComponentInstance()
    {
        RealizableComponent comp = newComponent(MockComboService.class, null);

        Object[] cArgs = {new Integer(2), new ArrayList()};

        comp.registerImplementation(MockComboServiceImpl.class, cArgs, null);

        MockComboService service = (MockComboService) comp.getInstance();

        comp.wrapStrategyWithSingleton();

        MockComboService service2 = (MockComboService) comp.getInstance();

        service.service();
        service2.service();

        assertTrue(service.hashCode() == service2.hashCode());
    }

    public void testToString()
    {
        //RealizableComponent comp = newComponent(MockComboService.class, null);
        ComponentStrategy factory = newSingletonComponent();

        assertEquals(factory.toString(), " [Singleton [Lazy Loading] ] ");
    }
}