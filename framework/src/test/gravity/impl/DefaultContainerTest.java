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

import gravity.ComponentPhase;
import gravity.ComponentCallback;
import gravity.Gravity;
import gravity.GravityTestCase;
import gravity.Location;
import gravity.MutableContainer;
import gravity.mocks.MockComboService;
import gravity.mocks.MockComboServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author Harish Krishnaswamy
 * @version $Id: DefaultContainerTest.java,v 1.5 2004-06-14 04:24:26 harishkswamy Exp $
 */
public class DefaultContainerTest extends GravityTestCase
{
    private MutableContainer _container = new DefaultContainer();

    public void setUp()
    {
        Properties props = new Properties();
        props.setProperty(Gravity.COMPONENT_PROXY_CLASS_NAME_KEY,
            "gravity.impl.CglibComponentProxy");

        Gravity.getInstance().initialize(props);
    }

    public void tearDown()
    {
        if (_container != null)
            _container.cleanup();

        Gravity.getInstance().shutdown();
    }

    public void testGetComponentKey()
    {
        Object key = _container.getComponentKey(List.class, "someType");
        Object key2 = _container.getComponentKey(List.class, "someType");
        Object key3 = _container.getComponentKey(List.class);

        assertNotNull(key);
        assertSame(key, key2);
        assertNotSame(key, key3);
    }

    public void testRegisterComponentImplementation()
    {
        Object[] cArgs = {new Integer(2), new ArrayList()};

        ComponentCallback[] methods = {new ComponentCallback("setObject",
            new Object[]{new Object()}, ComponentPhase.START_UP)};

        Object compKey = _container.getComponentKey(MockComboService.class, "variant");

        Object key = _container.registerComponentImplementation(compKey,
            MockComboServiceImpl.class, cArgs, methods);

        Object key2 = _container.registerComponentImplementation(compKey,
            MockComboServiceImpl.class, cArgs, methods);

        assertNotNull(key);
        assertNotNull(key2);
        assertEquals(key, key2);
    }

    public void testRegisterComponentConstructorArgs()
    {
        Object compKey = _container.getComponentKey(MockComboService.class, "variant");
        Object[] cArgs = {new Integer(2), new ArrayList()};

        Object key = _container.registerComponentConstructorArguments(compKey, cArgs);

        assertSame(key, compKey);
    }

    public void testRegisterComponentCallbacks()
    {
        Object compKey = _container.getComponentKey(MockComboService.class, "variant");

        ComponentCallback[] callbacks = {};

        Object key = _container.registerComponentCallbacks(compKey, callbacks);

        assertSame(key, compKey);
    }

    // Location registration tests ==========================

    public void testRegisterComponentRegistrationLocation()
    {
        Object[] cArgs = {new Integer(2), new ArrayList()};

        Object compKey = _container.getComponentKey(MockComboServiceImpl.class);

        Object key = _container.registerComponentImplementation(compKey,
            MockComboServiceImpl.class, cArgs, null);

        Object key2 = _container.registerComponentRegistrationLocation(key, new Location(
            "Some.file", 3543));

        assertSame(key, key2);
    }

    public void testRegisterComponentRetrievalLocation()
    {
        Object[] cArgs = {new Integer(2), new ArrayList()};

        Object compKey = _container.getComponentKey(MockComboServiceImpl.class);

        Object key = _container.registerComponentImplementation(compKey,
            MockComboServiceImpl.class, cArgs, null);

        Object key2 = _container.registerComponentRetrievalLocation(key, new Location("Some.file",
            3543));

        assertSame(key, key2);
    }

    // Factory decorator tests ================================

    public void testMakeComponentSingleton()
    {
        Object[] cArgs = {new Integer(2), new ArrayList()};

        Object compKey = _container.getComponentKey(MockComboService.class);

        Object key = _container.registerComponentImplementation(compKey,
            MockComboServiceImpl.class, cArgs, null);

        Object key2 = _container.wrapComponentStrategyWithSingleton(key);

        assertNotNull(key);
        assertEquals(key, key2);

        Object serv = _container.getComponentInstance(compKey);
        Object serv2 = _container.getComponentInstance(compKey);

        assertTrue(serv.toString().equals(serv2.toString()));
        assertTrue(serv.hashCode() == serv2.hashCode());
    }

    public void testMakeComponentPooled()
    {
        Object[] cArgs = {new Integer(2), new ArrayList()};

        Object compKey = _container.getComponentKey(MockComboService.class);

        Object key = _container.registerComponentImplementation(compKey,
            MockComboServiceImpl.class, cArgs, null);

        Object key2 = _container.wrapComponentStrategyWithPooling(key);

        assertNotNull(key);
        assertEquals(key, key2);

        Object serv = _container.getComponentInstance(compKey);

        // Return unrealized proxy
        _container.collectComponentInstance(compKey, serv);

        serv = _container.getComponentInstance(compKey);
        Object serv2 = _container.getComponentInstance(compKey);

        assertTrue(!serv.toString().equals(serv2.toString()));
        assertTrue(serv.hashCode() != serv2.hashCode());

        // Return realized proxy
        _container.collectComponentInstance(compKey, serv);

        Object serv3 = _container.getComponentInstance(compKey);

        assertEquals(serv.toString(), serv3.toString());
        assertTrue(serv.hashCode() == serv3.hashCode());
    }

    public void testMakeComponentThreadLocal()
    {
        Object[] cArgs = {new Integer(2), new ArrayList()};

        Object compKey = _container.getComponentKey(MockComboService.class);

        Object key = _container.registerComponentImplementation(compKey,
            MockComboServiceImpl.class, cArgs, null);

        Object key2 = _container.wrapComponentStrategyWithThreadLocal(key);

        assertNotNull(key);
        assertEquals(key, key2);

        Object serv = _container.getComponentInstance(compKey);
        Object serv2 = _container.getComponentInstance(compKey);

        assertTrue(serv.hashCode() == serv2.hashCode());

        Object serv3 = _container.getComponentInstance(compKey);

        assertTrue(serv.hashCode() == serv3.hashCode());
    }

    public void testDecoratePooledComponent()
    {
        Object[] cArgs = {new Integer(2), new ArrayList()};

        Object compKey = _container.getComponentKey(MockComboService.class);

        Object key = _container.registerComponentImplementation(compKey,
            MockComboServiceImpl.class, cArgs, null);

        Object key2 = _container.wrapComponentStrategyWithPooling(key);

        Object serv = _container.getComponentInstance(compKey);

        _container.wrapComponentStrategyWithThreadLocal(key2);

        Object serv2 = _container.getComponentInstance(compKey);

        assertTrue(serv.hashCode() == serv2.hashCode());
    }

    // Test getComponentInstance

    public void testGetComponentInstance()
    {
        Object[] cArgs = {new Integer(2), new ArrayList()};

        Object compKey = _container.getComponentKey(MockComboService.class);

        Object key = _container.registerComponentImplementation(compKey,
            MockComboServiceImpl.class, cArgs, null);

        MockComboService comp = (MockComboService) _container.getComponentInstance(key);
        MockComboService comp2 = (MockComboService) _container.getComponentInstance(key);

        assertTrue(comp.hashCode() != comp2.hashCode());
        assertFalse(comp.toString().equals(comp2.toString()));

        comp.setObject("abc");
        assertEquals(comp.getObject(), "abc");
    }

    public void testGetComponentInstanceFromMultipleImplementations()
    {
        Object compKey1 = _container.getComponentKey(MockComboService.class, "variant");
        Object compKey2 = _container.getComponentKey(MockComboService.class);

        Object comp = _container.getComponentInstance(compKey1);
        Object comp2 = _container.getComponentInstance(compKey2);
        Object comp3 = _container.getComponentInstance(compKey2);
        Object comp4 = _container.getComponentInstance(compKey1);

        assertNotNull(comp);
        assertNotNull(comp2);
        assertNotNull(comp3);
        assertNotNull(comp4);

        assertTrue(comp != comp4);
        assertTrue(comp2 != comp3);
        assertTrue(comp != comp2);
        assertTrue(comp3 != comp4);
    }

    // Test configuration

    public void testRegisterAndGetConfigurationList()
    {
        _container.registerConfiguration("dbProps", "oracle.jdbc.driver...");
        _container.registerConfiguration("dbProps", "jdbc:oracle:thin:...");
        _container.registerConfiguration("dbProps", "scott");
        _container.registerConfiguration("dbProps", "tiger");

        List dbProps = _container.getConfigurationList("dbProps");

        assertTrue(dbProps.size() == 4);
        assertEquals(dbProps.get(2), "scott");
    }

    public void testRegisterAndGetConfigurationMap()
    {
        _container.registerConfiguration("dbProps", "driver", "oracle.jdbc.driver...");
        _container.registerConfiguration("dbProps", "dbString", "jdbc:oracle:thin:...");
        _container.registerConfiguration("dbProps", "userName", "scott");
        _container.registerConfiguration("dbProps", "password", "tiger");

        Map dbProps = _container.getConfigurationMap("dbProps");

        assertTrue(dbProps.size() == 4);
        assertEquals(dbProps.get("userName"), "scott");
    }

    public void testGetNonExistentConfig()
    {
        List list = _container.getConfigurationList("configKey");

        assertNotNull(list);
        assertTrue(list.size() == 0);
    }
}