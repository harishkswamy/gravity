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

import gravity.Gravity;
import gravity.GravityTestCase;
import gravity.Location;
import gravity.MutableContainer;
import gravity.mocks.MockComboService;
import gravity.mocks.MockComboServiceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author Harish Krishnaswamy
 * @version $Id: DefaultContainerTest.java,v 1.1 2004-05-18 20:51:55 harishkswamy Exp $
 */
public class DefaultContainerTest extends GravityTestCase
{
    private MutableContainer _registry = new DefaultContainer();

    public void setUp()
    {
        Properties props = new Properties();
        props.setProperty(Gravity.COMPONENT_PROXY_CLASS_NAME_KEY, "gravity.impl.CglibComponentProxy");

        Gravity.getInstance().initialize(props);
    }

    public void tearDown()
    {
        if (_registry != null)
            _registry.cleanup();

        Gravity.getInstance().shutdown();
    }

    public void testRegisterCustomIntfComponentImplViaComboInjection()
    {
        Object[] cArgs = {new Integer(2), new ArrayList()};

        Map sArgs = new HashMap();
        sArgs.put("object", new Object());

        Object key = _registry.registerComponentImplementation(MockComboService.class, "variant",
            MockComboServiceImpl.class, cArgs, sArgs);

        Object key2 = _registry.registerComponentImplementation(MockComboService.class, "variant",
            MockComboServiceImpl.class, cArgs, sArgs);

        assertNotNull(key);
        assertNotNull(key2);
        assertEquals(key, key2);
    }

    public void testRegisterCustomIntfComponentImplViaConstructorInjection()
    {
        Object[] cArgs = {new Integer(2), new ArrayList()};

        Object key = _registry.registerComponentImplementation(MockComboService.class, "variant",
            MockComboServiceImpl.class, cArgs);

        Object key2 = _registry.registerComponentImplementation(MockComboService.class, "variant",
            MockComboServiceImpl.class, cArgs);

        assertNotNull(key);
        assertNotNull(key2);
        assertEquals(key, key2);
    }

    public void testRegisterCustomIntfComponentImplViaSetterInjection()
    {
        Map sArgs = new HashMap();
        sArgs.put("object", new Object());

        Object key = _registry.registerComponentImplementation(MockComboService.class, "variant",
            MockComboServiceImpl.class, sArgs);

        Object key2 = _registry.registerComponentImplementation(MockComboService.class, "variant",
            MockComboServiceImpl.class, sArgs);

        assertNotNull(key);
        assertNotNull(key2);
        assertEquals(key, key2);
    }

    public void testRegisterDefaultIntfComponentImplViaComboInjection()
    {
        Object[] cArgs = {new Integer(2), new ArrayList()};

        Map sArgs = new HashMap();
        sArgs.put("object", new Object());

        Object key = _registry.registerComponentImplementation(MockComboService.class,
            MockComboServiceImpl.class, cArgs, sArgs);

        Object key2 = _registry.registerComponentImplementation(MockComboService.class,
            MockComboServiceImpl.class, cArgs, sArgs);

        assertNotNull(key);
        assertNotNull(key2);
        assertEquals(key, key2);
    }

    public void testRegisterDefaultIntfComponentImplViaConstructorInjection()
    {
        Object[] cArgs = {new Integer(2), new ArrayList()};

        Object key = _registry.registerComponentImplementation(MockComboService.class,
            MockComboServiceImpl.class, cArgs);

        Object key2 = _registry.registerComponentImplementation(MockComboService.class,
            MockComboServiceImpl.class, cArgs);

        assertNotNull(key);
        assertNotNull(key2);
        assertEquals(key, key2);
    }

    public void testRegisterDefaultIntfComponentImplViaSetterInjection()
    {
        Map sArgs = new HashMap();
        sArgs.put("object", new Object());

        Object key = _registry.registerComponentImplementation(MockComboService.class,
            MockComboServiceImpl.class, sArgs);

        Object key2 = _registry.registerComponentImplementation(MockComboService.class,
            MockComboServiceImpl.class, sArgs);

        assertNotNull(key);
        assertNotNull(key2);
        assertEquals(key, key2);
    }

    public void testRegisterCustomClassComponentImplViaComboInjection()
    {
        Object[] cArgs = {new Integer(2), new ArrayList()};

        Map sArgs = new HashMap();
        sArgs.put("object", new Object());

        Object key = _registry.registerComponentImplementation(MockComboServiceImpl.class,
            "variant", cArgs, sArgs);

        Object key2 = _registry.registerComponentImplementation(MockComboServiceImpl.class,
            "variant", cArgs, sArgs);

        assertNotNull(key);
        assertNotNull(key2);
        assertEquals(key, key2);
    }

    public void testRegisterCustomClassComponentImplViaConstructorInjection()
    {
        Object[] cArgs = {new Integer(2), new ArrayList()};

        Object key = _registry.registerComponentImplementation(MockComboServiceImpl.class,
            "variant", cArgs);

        Object key2 = _registry.registerComponentImplementation(MockComboServiceImpl.class,
            "variant", cArgs);

        assertNotNull(key);
        assertNotNull(key2);
        assertEquals(key, key2);
    }

    public void testRegisterCustomClassComponentImplViaSetterInjection()
    {
        Map sArgs = new HashMap();
        sArgs.put("object", new Object());

        Object key = _registry.registerComponentImplementation(MockComboServiceImpl.class,
            "variant", sArgs);

        Object key2 = _registry.registerComponentImplementation(MockComboServiceImpl.class,
            "variant", sArgs);

        assertNotNull(key);
        assertNotNull(key2);
        assertEquals(key, key2);
    }

    public void testRegisterDefaultClassComponentImplViaComboInjection()
    {
        Object[] cArgs = {new Integer(2), new ArrayList()};

        Map sArgs = new HashMap();
        sArgs.put("object", new Object());

        Object key = _registry.registerComponentImplementation(MockComboServiceImpl.class, cArgs,
            sArgs);

        Object key2 = _registry.registerComponentImplementation(MockComboServiceImpl.class, cArgs,
            sArgs);

        assertNotNull(key);
        assertNotNull(key2);
        assertEquals(key, key2);
    }

    public void testRegisterDefaultClassComponentImplViaConstructorInjection()
    {
        Object[] cArgs = {new Integer(2), new ArrayList()};

        Object key = _registry.registerComponentImplementation(MockComboServiceImpl.class, cArgs);

        Object key2 = _registry.registerComponentImplementation(MockComboServiceImpl.class, cArgs);

        assertNotNull(key);
        assertNotNull(key2);
        assertEquals(key, key2);
    }

    public void testRegisterDefaultClassComponentImplViaSetterInjection()
    {
        Map sArgs = new HashMap();
        sArgs.put("object", new Object());

        Object key = _registry.registerComponentImplementation(MockComboServiceImpl.class, sArgs);

        Object key2 = _registry.registerComponentImplementation(MockComboServiceImpl.class, sArgs);

        assertNotNull(key);
        assertNotNull(key2);
        assertEquals(key, key2);
    }

    // Location registration tests ==========================

    public void testRegisterComponentRegistrationLocationFromComponentKey()
    {
        Object[] cArgs = {new Integer(2), new ArrayList()};

        Object key = _registry.registerComponentImplementation(MockComboServiceImpl.class, cArgs);

        Object key2 = _registry.registerComponentRegistrationLocation(key, new Location(
            "Some.file", 3543));

        assertSame(key, key2);
    }

    public void testRegisterComponentRegistrationLocationFromComponentInterfaceAndType()
    {
        Object[] cArgs = {new Integer(2), new ArrayList()};

        Object key = _registry.registerComponentImplementation(MockComboServiceImpl.class, cArgs);

        Object key2 = _registry.registerComponentRegistrationLocation(MockComboServiceImpl.class,
            null, new Location("Some.file", 3543));

        assertEquals(key, key2);
    }

    public void testRegisterComponentRegistrationLocationFromComponentInterface()
    {
        Object[] cArgs = {new Integer(2), new ArrayList()};

        Object key = _registry.registerComponentImplementation(MockComboServiceImpl.class, cArgs);

        Object key2 = _registry.registerComponentRegistrationLocation(MockComboServiceImpl.class,
            new Location("Some.file", 3543));

        assertEquals(key, key2);
    }

    public void testRegisterComponentRetrievalLocationFromComponentKey()
    {
        Object[] cArgs = {new Integer(2), new ArrayList()};

        Object key = _registry.registerComponentImplementation(MockComboServiceImpl.class, cArgs);

        Object key2 = _registry.registerComponentRetrievalLocation(key, new Location("Some.file",
            3543));

        assertSame(key, key2);
    }

    public void testRegisterComponentRetrievalLocationFromComponentInterfaceAndType()
    {
        Object[] cArgs = {new Integer(2), new ArrayList()};

        Object key = _registry.registerComponentImplementation(MockComboServiceImpl.class, cArgs);

        Object key2 = _registry.registerComponentRetrievalLocation(MockComboServiceImpl.class,
            null, new Location("Some.file", 3543));

        assertEquals(key, key2);
    }

    public void testRegisterComponentRetrievalLocationFromComponentInterface()
    {
        Object[] cArgs = {new Integer(2), new ArrayList()};

        Object key = _registry.registerComponentImplementation(MockComboServiceImpl.class, cArgs);

        Object key2 = _registry.registerComponentRetrievalLocation(MockComboServiceImpl.class,
            new Location("Some.file", 3543));

        assertEquals(key, key2);
    }

    // Factory decorator tests ================================

    public void testMakeComponentSingleton()
    {
        Object[] cArgs = {new Integer(2), new ArrayList()};

        Object key = _registry.registerComponentImplementation(MockComboService.class,
            MockComboServiceImpl.class, cArgs);

        Object key2 = _registry.wrapComponentStateWithSingleton(key);

        assertNotNull(key);
        assertEquals(key, key2);

        Object serv = _registry.getComponentInstance(MockComboService.class);
        Object serv2 = _registry.getComponentInstance(MockComboService.class);

        assertTrue(serv.toString().equals(serv2.toString()));
        assertTrue(serv.hashCode() == serv2.hashCode());
    }

    public void testMakeComponentPooled()
    {
        Object[] cArgs = {new Integer(2), new ArrayList()};

        Object key = _registry.registerComponentImplementation(MockComboService.class,
            MockComboServiceImpl.class, cArgs);

        Object key2 = _registry.wrapComponentStateWithPooling(key);

        assertNotNull(key);
        assertEquals(key, key2);

        Object serv = _registry.getComponentInstance(MockComboService.class);

        // Return unrealized proxy
        _registry.collectComponentInstance(MockComboService.class, serv);

        serv = _registry.getComponentInstance(MockComboService.class);
        Object serv2 = _registry.getComponentInstance(MockComboService.class);

        assertTrue(!serv.toString().equals(serv2.toString()));
        assertTrue(serv.hashCode() != serv2.hashCode());

        // Return realized proxy
        _registry.collectComponentInstance(MockComboService.class, serv);

        Object serv3 = _registry.getComponentInstance(MockComboService.class);

        assertEquals(serv.toString(), serv3.toString());
        assertTrue(serv.hashCode() == serv3.hashCode());
    }

    public void testMakeComponentThreadLocal()
    {
        Object[] cArgs = {new Integer(2), new ArrayList()};

        Object key = _registry.registerComponentImplementation(MockComboService.class,
            MockComboServiceImpl.class, cArgs);

        Object key2 = _registry.wrapComponentStateWithThreadLocal(key);

        assertNotNull(key);
        assertEquals(key, key2);

        Object serv = _registry.getComponentInstance(MockComboService.class);
        Object serv2 = _registry.getComponentInstance(MockComboService.class);

        assertTrue(serv.hashCode() == serv2.hashCode());

        Object serv3 = _registry.getComponentInstance(MockComboService.class);

        assertTrue(serv.hashCode() == serv3.hashCode());
    }

    public void testDecoratePooledComponent()
    {
        Object[] cArgs = {new Integer(2), new ArrayList()};

        Object key = _registry.registerComponentImplementation(MockComboService.class,
            MockComboServiceImpl.class, cArgs);

        Object key2 = _registry.wrapComponentStateWithPooling(key);

        Object serv = _registry.getComponentInstance(MockComboService.class);

        _registry.wrapComponentStateWithThreadLocal(key2);

        Object serv2 = _registry.getComponentInstance(MockComboService.class);

        assertTrue(serv.hashCode() == serv2.hashCode());
    }

    // Test getComponentInstance

    public void testGetComponentInstance()
    {
        Object[] cArgs = {new Integer(2), new ArrayList()};

        Object key = _registry.registerComponentImplementation(MockComboService.class,
            MockComboServiceImpl.class, cArgs);

        MockComboService comp = (MockComboService) _registry.getComponentInstance(key);
        MockComboService comp2 = (MockComboService) _registry.getComponentInstance(key);

        assertTrue(comp.hashCode() != comp2.hashCode());
        assertFalse(comp.toString().equals(comp2.toString()));

        comp.setObject("abc");
        assertEquals(comp.getObject(), "abc");
    }

    public void testGetComponentInstanceFromMultipleImplementations()
    {
        Object comp = _registry.getComponentInstance(MockComboService.class, "variant");
        Object comp2 = _registry.getComponentInstance(MockComboService.class);
        Object comp3 = _registry.getComponentInstance(MockComboService.class);
        Object comp4 = _registry.getComponentInstance(MockComboService.class, "variant");

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
        _registry.registerConfiguration("dbProps", "oracle.jdbc.driver...");
        _registry.registerConfiguration("dbProps", "jdbc:oracle:thin:...");
        _registry.registerConfiguration("dbProps", "scott");
        _registry.registerConfiguration("dbProps", "tiger");

        List dbProps = _registry.getConfigurationList("dbProps");

        assertTrue(dbProps.size() == 4);
        assertEquals(dbProps.get(2), "scott");
    }

    public void testRegisterAndGetConfigurationMap()
    {
        _registry.registerConfiguration("dbProps", "driver", "oracle.jdbc.driver...");
        _registry.registerConfiguration("dbProps", "dbString", "jdbc:oracle:thin:...");
        _registry.registerConfiguration("dbProps", "userName", "scott");
        _registry.registerConfiguration("dbProps", "password", "tiger");

        Map dbProps = _registry.getConfigurationMap("dbProps");

        assertTrue(dbProps.size() == 4);
        assertEquals(dbProps.get("userName"), "scott");
    }

    public void testGetNonExistentConfig()
    {
        List list = _registry.getConfigurationList("configKey");

        assertNotNull(list);
        assertTrue(list.size() == 0);
    }
}