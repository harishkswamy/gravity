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

package gravity.plugins;

import gravity.ComponentPhase;
import gravity.ComponentCallback;
import gravity.Gravity;
import gravity.GravityTestCase;
import gravity.impl.DefaultContainer;
import gravity.mocks.MockComboService;
import gravity.mocks.MockComboServiceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Harish Krishnaswamy
 * @version $Id: MutableContainerAdapterTest.java,v 1.2 2004-05-27 03:36:29 harishkswamy Exp $
 */
public class MutableContainerAdapterTest extends GravityTestCase
{
    private static final ComponentPhase START_UP   = ComponentPhase.START_UP;

    final Map                                    _servReg   = new HashMap();
    final Map                                    _configReg = new HashMap();
    final MutableContainerAdapter                _registry  = new MutableContainerAdapter(
                                                                new DefaultContainer());

    public void setUp()
    {
        Gravity.getInstance().initialize();
    }

    public void tearDown()
    {
        Gravity.getInstance().shutdown();
    }

    public void testRegisterCustomIntfComponentImplViaComboInjection()
    {
        Object[] cArgs = {new Integer(2), new ArrayList()};

        ComponentCallback[] methods = {new ComponentCallback("setObject",
            new Object[]{new Object()}, START_UP)};

        Object key = _registry.componentImpl(MockComboService.class, "variant",
            MockComboServiceImpl.class, cArgs, methods);

        Object key2 = _registry.componentImpl(MockComboService.class, "variant",
            MockComboServiceImpl.class, cArgs, methods);

        assertNotNull(key);
        assertNotNull(key2);
        assertEquals(key, key2);
    }

    public void testRegisterCustomIntfComponentImplViaConstructorInjection()
    {
        Object[] cArgs = {new Integer(2), new ArrayList()};

        Object key = _registry.componentImpl(MockComboService.class, "variant",
            MockComboServiceImpl.class, cArgs);

        Object key2 = _registry.componentImpl(MockComboService.class, "variant",
            MockComboServiceImpl.class, cArgs);

        assertNotNull(key);
        assertNotNull(key2);
        assertEquals(key, key2);
    }

    public void testRegisterCustomIntfComponentImplViaSetterInjection()
    {
        ComponentCallback[] methods = {new ComponentCallback("setObject",
            new Object[]{new Object()}, START_UP)};

        Object key = _registry.componentImpl(MockComboService.class, "variant",
            MockComboServiceImpl.class, methods, null);

        Object key2 = _registry.componentImpl(MockComboService.class, "variant",
            MockComboServiceImpl.class, methods, null);

        assertNotNull(key);
        assertNotNull(key2);
        assertEquals(key, key2);
    }

    public void testRegisterDefaultIntfComponentImplViaComboInjection()
    {
        Object[] cArgs = {new Integer(2), new ArrayList()};

        ComponentCallback[] methods = {new ComponentCallback("setObject",
            new Object[]{new Object()}, START_UP)};

        Object key = _registry.componentImpl(MockComboService.class, MockComboServiceImpl.class,
            cArgs, methods);

        Object key2 = _registry.componentImpl(MockComboService.class, MockComboServiceImpl.class,
            cArgs, methods);

        assertNotNull(key);
        assertNotNull(key2);
        assertEquals(key, key2);
    }

    public void testRegisterDefaultIntfComponentImplViaConstructorInjection()
    {
        Object[] cArgs = {new Integer(2), new ArrayList()};

        Object key = _registry.componentImpl(MockComboService.class, MockComboServiceImpl.class,
            cArgs);

        Object key2 = _registry.componentImpl(MockComboService.class, MockComboServiceImpl.class,
            cArgs);

        assertNotNull(key);
        assertNotNull(key2);
        assertEquals(key, key2);
    }

    public void testRegisterDefaultIntfComponentImplViaSetterInjection()
    {
        ComponentCallback[] methods = {new ComponentCallback("setObject",
            new Object[]{new Object()}, START_UP)};

        Object key = _registry.componentImpl(MockComboService.class, MockComboServiceImpl.class,
            methods, null);

        Object key2 = _registry.componentImpl(MockComboService.class, MockComboServiceImpl.class,
            methods, null);

        assertNotNull(key);
        assertNotNull(key2);
        assertEquals(key, key2);
    }

    public void testRegisterCustomClassComponentImplViaComboInjection()
    {
        Object[] cArgs = {new Integer(2), new ArrayList()};

        ComponentCallback[] methods = {new ComponentCallback("setObject",
            new Object[]{new Object()}, START_UP)};

        Object key = _registry.componentImpl(MockComboServiceImpl.class, "variant", cArgs, methods);

        Object key2 = _registry.componentImpl(MockComboServiceImpl.class, "variant", cArgs, methods);

        assertNotNull(key);
        assertNotNull(key2);
        assertEquals(key, key2);
    }

    public void testRegisterCustomClassComponentImplViaConstructorInjection()
    {
        Object[] cArgs = {new Integer(2), new ArrayList()};

        Object key = _registry.componentImpl(MockComboServiceImpl.class, "variant", cArgs);

        Object key2 = _registry.componentImpl(MockComboServiceImpl.class, "variant", cArgs);

        assertNotNull(key);
        assertNotNull(key2);
        assertEquals(key, key2);
    }

    public void testRegisterCustomClassComponentImplViaSetterInjection()
    {
        ComponentCallback[] methods = {new ComponentCallback("setObject",
            new Object[]{new Object()}, START_UP)};

        Object key = _registry.componentImpl(MockComboServiceImpl.class, "variant", methods, null);

        Object key2 = _registry.componentImpl(MockComboServiceImpl.class, "variant", methods, null);

        assertNotNull(key);
        assertNotNull(key2);
        assertEquals(key, key2);
    }

    public void testRegisterDefaultClassComponentImplViaComboInjection()
    {
        Object[] cArgs = {new Integer(2), new ArrayList()};

        ComponentCallback[] methods = {new ComponentCallback("setObject",
            new Object[]{new Object()}, START_UP)};

        Object key = _registry.componentImpl(MockComboServiceImpl.class, cArgs, methods, null);

        Object key2 = _registry.componentImpl(MockComboServiceImpl.class, cArgs, methods, null);

        assertNotNull(key);
        assertNotNull(key2);
        assertEquals(key, key2);
    }

    public void testRegisterDefaultClassComponentImplViaConstructorInjection()
    {
        Object[] cArgs = {new Integer(2), new ArrayList()};

        Object key = _registry.componentImpl(MockComboServiceImpl.class, cArgs);

        Object key2 = _registry.componentImpl(MockComboServiceImpl.class, cArgs);

        assertNotNull(key);
        assertNotNull(key2);
        assertEquals(key, key2);
    }

    public void testRegisterDefaultClassComponentImplViaSetterInjection()
    {
        ComponentCallback[] methods = {new ComponentCallback("setObject",
            new Object[]{new Object()}, START_UP)};

        Object key = _registry.componentImpl(MockComboServiceImpl.class, methods);

        Object key2 = _registry.componentImpl(MockComboServiceImpl.class, methods);

        assertNotNull(key);
        assertNotNull(key2);
        assertEquals(key, key2);
    }

    // Factory decorator tests ================================

    public void testMakeComponentSingleton()
    {
        Object[] cArgs = {new Integer(2), new ArrayList()};

        Object key = _registry.componentImpl(MockComboServiceImpl.class, cArgs);

        Object key2 = _registry.singleton(key);

        assertNotNull(key);
        assertNotNull(key2);
        assertEquals(key, key2);
    }

    public void testGetComponentInstanceFromKey()
    {
        Object[] cArgs = {new Integer(2), new ArrayList()};

        Object key = _registry.componentImpl(MockComboService.class, cArgs);

        Object comp = _registry.componentInst(key);

        assertNotNull(comp);
    }

    public void testGetComponentInstance()
    {
        Object comp = _registry.componentInst(MockComboService.class, "variant");
        Object comp2 = _registry.componentInst(MockComboService.class);

        assertNotNull(comp);
        assertNotNull(comp2);
        assertTrue(comp != comp2);
    }

    public void testRegisterAndGetConfigurationList()
    {
        _registry.configItem("dbProps", "oracle.jdbc.driver...");
        _registry.configItem("dbProps", "jdbc:oracle:thin:...");
        _registry.configItem("dbProps", "scott");
        _registry.configItem("dbProps", "tiger");

        List dbProps = _registry.configList("dbProps");

        assertTrue(dbProps.size() == 4);
        assertEquals(dbProps.get(2), "scott");
    }

    public void testRegisterAndGetConfigurationMap()
    {
        _registry.configItem("dbProps", "driver", "oracle.jdbc.driver...");
        _registry.configItem("dbProps", "dbString", "jdbc:oracle:thin:...");
        _registry.configItem("dbProps", "userName", "scott");
        _registry.configItem("dbProps", "password", "tiger");

        Map dbProps = _registry.configMap("dbProps");

        assertTrue(dbProps.size() == 4);
        assertEquals(dbProps.get("userName"), "scott");
    }

    public void testGetNonExistentConfig()
    {
        List list = _registry.configList("configKey");

        assertNotNull(list);
        assertTrue(list.size() == 0);
    }
}