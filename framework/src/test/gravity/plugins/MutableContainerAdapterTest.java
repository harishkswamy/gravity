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

import gravity.ComponentCallback;
import gravity.ComponentPhase;
import gravity.GravityTestCase;
import gravity.Location;
import gravity.MutableContainer;
import gravity.mocks.MockComboService;
import gravity.mocks.MockComboServiceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easymock.MockControl;

/**
 * @author Harish Krishnaswamy
 * @version $Id: MutableContainerAdapterTest.java,v 1.3 2004-06-14 04:24:29 harishkswamy Exp $
 */
public class MutableContainerAdapterTest extends GravityTestCase
{
    private MutableContainerAdapter _containerAdapter;
    private MockControl             _containerControl;
    private MutableContainer        _mockContainer;

    public void setUp()
    {
        _containerControl = MockControl.createStrictControl(MutableContainer.class);
        _mockContainer = (MutableContainer) _containerControl.getMock();

        _containerAdapter = new MutableContainerAdapter(_mockContainer);
    }

    public void testComponentImplFromAnotherComponent()
    {
        Object compKey = new Object();
        Object srcCompKey = new Object();

        _containerControl.expectAndReturn(_mockContainer.registerComponentImplementation(compKey,
            srcCompKey), compKey, 1);
        _containerControl.expectAndReturn(_mockContainer.registerComponentRegistrationLocation(
            compKey, new Location(null, 0)), compKey, 1);
        _containerControl.replay();

        Object rtnCompKey = _containerAdapter.componentImpl(compKey, srcCompKey);

        _containerControl.verify();

        assertSame(compKey, rtnCompKey);
    }

    public void testComponentKeyWithInterface()
    {
        Object key = new Object();
        _containerControl.expectAndReturn(_mockContainer.getComponentKey(List.class), key, 1);
        _containerControl.replay();

        Object rtnKey = _containerAdapter.componentKey(List.class);

        assertSame(key, rtnKey);
    }

    public void testComponentKeyWithInterfaceAndType()
    {
        Object key = new Object();
        _containerControl.expectAndReturn(_mockContainer.getComponentKey(List.class, "variant"),
            key, 1);
        _containerControl.replay();

        Object rtnKey = _containerAdapter.componentKey(List.class, "variant");

        assertSame(key, rtnKey);
    }

    public void testComponentImplWithKey()
    {
        Object key = new Object();
        Object[] cArgs = new Object[0];
        ComponentCallback[] callbacks = new ComponentCallback[0];

        _containerControl.expectAndReturn(_mockContainer.registerComponentImplementation(key,
            MockComboServiceImpl.class, cArgs, callbacks), key, 1);
        _containerControl.expectAndReturn(_mockContainer.registerComponentRegistrationLocation(key,
            new Location(null, 0)), key, 1);
        _containerControl.expectAndReturn(_mockContainer.registerComponentImplementation(key,
            MockComboServiceImpl.class, cArgs, null), key, 1);
        _containerControl.expectAndReturn(_mockContainer.registerComponentRegistrationLocation(key,
            new Location(null, 0)), key, 1);
        _containerControl.expectAndReturn(_mockContainer.registerComponentImplementation(key,
            MockComboServiceImpl.class, null, callbacks), key, 1);
        _containerControl.expectAndReturn(_mockContainer.registerComponentRegistrationLocation(key,
            new Location(null, 0)), key, 1);
        _containerControl.expectAndReturn(_mockContainer.registerComponentImplementation(key,
            MockComboServiceImpl.class, null, null), key, 1);
        _containerControl.expectAndReturn(_mockContainer.registerComponentRegistrationLocation(key,
            new Location(null, 0)), key, 1);
        _containerControl.replay();

        Object rtnVal1 = _containerAdapter.componentImpl(key, MockComboServiceImpl.class, cArgs,
            callbacks);
        Object rtnVal2 = _containerAdapter.componentImpl(key, MockComboServiceImpl.class, cArgs);
        Object rtnVal3 = _containerAdapter.componentImpl(key, MockComboServiceImpl.class, callbacks);
        Object rtnVal4 = _containerAdapter.componentImpl(key, MockComboServiceImpl.class);

        _containerControl.verify();

        assertSame(rtnVal1, key);
        assertSame(rtnVal2, key);
        assertSame(rtnVal3, key);
        assertSame(rtnVal4, key);
    }

    private void setUpMockControl(Object compKey, String compType, Object[] cArgs,
        ComponentCallback[] methods)
    {
        _containerControl.expectAndReturn(_mockContainer.getComponentKey(MockComboService.class,
            compType), compKey, 1);
        _containerControl.expectAndReturn(_mockContainer.registerComponentImplementation(compKey,
            MockComboServiceImpl.class, cArgs, methods), compKey, 1);
        _containerControl.expectAndReturn(_mockContainer.registerComponentRegistrationLocation(
            compKey, new Location(null, 0)), compKey, 1);

        _containerControl.expectAndReturn(_mockContainer.getComponentKey(MockComboService.class,
            compType), compKey, 1);
        _containerControl.expectAndReturn(_mockContainer.registerComponentImplementation(compKey,
            MockComboServiceImpl.class, cArgs, null), compKey, 1);
        _containerControl.expectAndReturn(_mockContainer.registerComponentRegistrationLocation(
            compKey, new Location(null, 0)), compKey, 1);

        _containerControl.expectAndReturn(_mockContainer.getComponentKey(MockComboService.class,
            compType), compKey, 1);
        _containerControl.expectAndReturn(_mockContainer.registerComponentImplementation(compKey,
            MockComboServiceImpl.class, null, methods), compKey, 1);
        _containerControl.expectAndReturn(_mockContainer.registerComponentRegistrationLocation(
            compKey, new Location(null, 0)), compKey, 1);

        _containerControl.expectAndReturn(_mockContainer.getComponentKey(MockComboService.class,
            compType), compKey, 1);
        _containerControl.expectAndReturn(_mockContainer.registerComponentImplementation(compKey,
            MockComboServiceImpl.class, null, null), compKey, 1);
        _containerControl.expectAndReturn(_mockContainer.registerComponentRegistrationLocation(
            compKey, new Location(null, 0)), compKey, 1);

        _containerControl.replay();
    }

    public void testCustomComponentImplWithInterface()
    {
        Object[] cArgs = new Object[0];

        ComponentCallback[] methods = new ComponentCallback[0];

        Object compKey = new Object();

        setUpMockControl(compKey, "variant", cArgs, methods);

        Object rtnKey1 = _containerAdapter.componentImpl(MockComboService.class, "variant",
            MockComboServiceImpl.class, cArgs, methods);
        Object rtnKey2 = _containerAdapter.componentImpl(MockComboService.class, "variant",
            MockComboServiceImpl.class, cArgs);
        Object rtnKey3 = _containerAdapter.componentImpl(MockComboService.class, "variant",
            MockComboServiceImpl.class, methods);
        Object rtnKey4 = _containerAdapter.componentImpl(MockComboService.class, "variant",
            MockComboServiceImpl.class);

        _containerControl.verify();

        assertEquals(rtnKey1, compKey);
        assertEquals(rtnKey2, compKey);
        assertEquals(rtnKey3, compKey);
        assertEquals(rtnKey4, compKey);
    }

    public void testDefaultComponentImplWithInterface()
    {
        Object[] cArgs = new Object[0];

        ComponentCallback[] methods = new ComponentCallback[0];

        Object compKey = new Object();

        setUpMockControl(compKey, null, cArgs, methods);

        Object rtnKey1 = _containerAdapter.componentImpl(MockComboService.class,
            MockComboServiceImpl.class, cArgs, methods);
        Object rtnKey2 = _containerAdapter.componentImpl(MockComboService.class,
            MockComboServiceImpl.class, cArgs);
        Object rtnKey3 = _containerAdapter.componentImpl(MockComboService.class,
            MockComboServiceImpl.class, methods);
        Object rtnKey4 = _containerAdapter.componentImpl(MockComboService.class,
            MockComboServiceImpl.class);

        _containerControl.verify();

        assertEquals(rtnKey1, compKey);
        assertEquals(rtnKey2, compKey);
        assertEquals(rtnKey3, compKey);
        assertEquals(rtnKey4, compKey);
    }

    private void setUpImplMockControl(Object compKey, String compType, Object[] cArgs,
        ComponentCallback[] methods)
    {
        _containerControl.expectAndReturn(_mockContainer.getComponentKey(
            MockComboServiceImpl.class, compType), compKey, 1);
        _containerControl.expectAndReturn(_mockContainer.registerComponentImplementation(compKey,
            MockComboServiceImpl.class, cArgs, methods), compKey, 1);
        _containerControl.expectAndReturn(_mockContainer.registerComponentRegistrationLocation(
            compKey, new Location(null, 0)), compKey, 1);

        _containerControl.expectAndReturn(_mockContainer.getComponentKey(
            MockComboServiceImpl.class, compType), compKey, 1);
        _containerControl.expectAndReturn(_mockContainer.registerComponentImplementation(compKey,
            MockComboServiceImpl.class, cArgs, null), compKey, 1);
        _containerControl.expectAndReturn(_mockContainer.registerComponentRegistrationLocation(
            compKey, new Location(null, 0)), compKey, 1);

        _containerControl.expectAndReturn(_mockContainer.getComponentKey(
            MockComboServiceImpl.class, compType), compKey, 1);
        _containerControl.expectAndReturn(_mockContainer.registerComponentImplementation(compKey,
            MockComboServiceImpl.class, null, methods), compKey, 1);
        _containerControl.expectAndReturn(_mockContainer.registerComponentRegistrationLocation(
            compKey, new Location(null, 0)), compKey, 1);

        _containerControl.expectAndReturn(_mockContainer.getComponentKey(
            MockComboServiceImpl.class, compType), compKey, 1);
        _containerControl.expectAndReturn(_mockContainer.registerComponentImplementation(compKey,
            MockComboServiceImpl.class, null, null), compKey, 1);
        _containerControl.expectAndReturn(_mockContainer.registerComponentRegistrationLocation(
            compKey, new Location(null, 0)), compKey, 1);

        _containerControl.replay();
    }

    public void testRegisterCustomComponentImplWithClass()
    {
        Object[] cArgs = new Object[0];

        ComponentCallback[] methods = new ComponentCallback[0];

        Object compKey = new Object();

        setUpImplMockControl(compKey, "variant", cArgs, methods);

        Object rtnKey1 = _containerAdapter.componentImpl(MockComboServiceImpl.class, "variant",
            cArgs, methods);
        Object rtnKey2 = _containerAdapter.componentImpl(MockComboServiceImpl.class, "variant",
            cArgs);
        Object rtnKey3 = _containerAdapter.componentImpl(MockComboServiceImpl.class, "variant",
            methods);
        Object rtnKey4 = _containerAdapter.componentImpl(MockComboServiceImpl.class, "variant");

        _containerControl.verify();

        assertEquals(rtnKey1, compKey);
        assertEquals(rtnKey2, compKey);
        assertEquals(rtnKey3, compKey);
        assertEquals(rtnKey4, compKey);
    }

    public void testRegisterDefaultComponentImplWithClass()
    {
        Object[] cArgs = new Object[0];

        ComponentCallback[] methods = new ComponentCallback[0];

        Object compKey = new Object();

        setUpImplMockControl(compKey, null, cArgs, methods);

        Object rtnKey1 = _containerAdapter.componentImpl(MockComboServiceImpl.class, cArgs, methods);
        Object rtnKey2 = _containerAdapter.componentImpl(MockComboServiceImpl.class, cArgs);
        Object rtnKey3 = _containerAdapter.componentImpl(MockComboServiceImpl.class, methods);
        Object rtnKey4 = _containerAdapter.componentImpl(MockComboServiceImpl.class);

        _containerControl.verify();

        assertEquals(rtnKey1, compKey);
        assertEquals(rtnKey2, compKey);
        assertEquals(rtnKey3, compKey);
        assertEquals(rtnKey4, compKey);
    }

    public void testComponentFacWithKey()
    {
        Object key = new Object();
        Object fac = new Object();
        Object[] mArgs = new Object[0];
        ComponentCallback[] callbacks = new ComponentCallback[0];

        _containerControl.expectAndReturn(_mockContainer.registerComponentFactory(key, fac,
            "method", mArgs, callbacks), key, 1);
        _containerControl.expectAndReturn(_mockContainer.registerComponentRegistrationLocation(key,
            new Location(null, 0)), key, 1);

        _containerControl.expectAndReturn(_mockContainer.registerComponentFactory(key, fac,
            "method", mArgs, null), key, 1);
        _containerControl.expectAndReturn(_mockContainer.registerComponentRegistrationLocation(key,
            new Location(null, 0)), key, 1);

        _containerControl.expectAndReturn(_mockContainer.registerComponentFactory(key, fac,
            "method", null, callbacks), key, 1);
        _containerControl.expectAndReturn(_mockContainer.registerComponentRegistrationLocation(key,
            new Location(null, 0)), key, 1);

        _containerControl.expectAndReturn(_mockContainer.registerComponentFactory(key, fac,
            "method", null, null), key, 1);
        _containerControl.expectAndReturn(_mockContainer.registerComponentRegistrationLocation(key,
            new Location(null, 0)), key, 1);

        _containerControl.replay();

        Object rtnVal1 = _containerAdapter.componentFac(key, fac, "method", mArgs, callbacks);
        Object rtnVal2 = _containerAdapter.componentFac(key, fac, "method", mArgs);
        Object rtnVal3 = _containerAdapter.componentFac(key, fac, "method", callbacks);
        Object rtnVal4 = _containerAdapter.componentFac(key, fac, "method");

        _containerControl.verify();

        assertSame(rtnVal1, key);
        assertSame(rtnVal2, key);
        assertSame(rtnVal3, key);
        assertSame(rtnVal4, key);
    }

    private void setUpMockControlForComponentFac(Class intf, Object type, Object key, Object fac,
        Object[] mArgs, ComponentCallback[] callbacks)
    {
        _containerControl.expectAndReturn(_mockContainer.getComponentKey(intf, type), key, 1);
        _containerControl.expectAndReturn(_mockContainer.registerComponentFactory(key, fac,
            "method", mArgs, callbacks), key, 1);
        _containerControl.expectAndReturn(_mockContainer.registerComponentRegistrationLocation(key,
            new Location(null, 0)), key, 1);

        _containerControl.expectAndReturn(_mockContainer.getComponentKey(intf, type), key, 1);
        _containerControl.expectAndReturn(_mockContainer.registerComponentFactory(key, fac,
            "method", mArgs, null), key, 1);
        _containerControl.expectAndReturn(_mockContainer.registerComponentRegistrationLocation(key,
            new Location(null, 0)), key, 1);

        _containerControl.expectAndReturn(_mockContainer.getComponentKey(intf, type), key, 1);
        _containerControl.expectAndReturn(_mockContainer.registerComponentFactory(key, fac,
            "method", null, callbacks), key, 1);
        _containerControl.expectAndReturn(_mockContainer.registerComponentRegistrationLocation(key,
            new Location(null, 0)), key, 1);

        _containerControl.expectAndReturn(_mockContainer.getComponentKey(intf, type), key, 1);
        _containerControl.expectAndReturn(_mockContainer.registerComponentFactory(key, fac,
            "method", null, null), key, 1);
        _containerControl.expectAndReturn(_mockContainer.registerComponentRegistrationLocation(key,
            new Location(null, 0)), key, 1);

        _containerControl.replay();

    }

    // Factory decorator tests ================================

    public void testComponentFacWithInterfaceAndType()
    {
        Class intf = MockComboService.class;
        Object type = "variant";
        Object key = new Object();
        Object fac = new Object();
        Object[] mArgs = new Object[0];
        ComponentCallback[] callbacks = new ComponentCallback[0];

        setUpMockControlForComponentFac(intf, type, key, fac, mArgs, callbacks);

        Object rtnVal1 = _containerAdapter.componentFac(intf, type, fac, "method", mArgs, callbacks);
        Object rtnVal2 = _containerAdapter.componentFac(intf, type, fac, "method", mArgs);
        Object rtnVal3 = _containerAdapter.componentFac(intf, type, fac, "method", callbacks);
        Object rtnVal4 = _containerAdapter.componentFac(intf, type, fac, "method");

        _containerControl.verify();

        assertSame(rtnVal1, key);
        assertSame(rtnVal2, key);
        assertSame(rtnVal3, key);
        assertSame(rtnVal4, key);
    }

    public void testComponentFacWithInterface()
    {
        Class intf = MockComboService.class;
        Object key = new Object();
        Object fac = new Object();
        Object[] mArgs = new Object[0];
        ComponentCallback[] callbacks = new ComponentCallback[0];

        setUpMockControlForComponentFac(intf, null, key, fac, mArgs, callbacks);

        Object rtnVal1 = _containerAdapter.componentFac(intf, fac, "method", mArgs, callbacks);
        Object rtnVal2 = _containerAdapter.componentFac(intf, fac, "method", mArgs);
        Object rtnVal3 = _containerAdapter.componentFac(intf, fac, "method", callbacks);
        Object rtnVal4 = _containerAdapter.componentFac(intf, fac, "method");

        _containerControl.verify();

        assertSame(rtnVal1, key);
        assertSame(rtnVal2, key);
        assertSame(rtnVal3, key);
        assertSame(rtnVal4, key);
    }

    // add constructor args tests

    public void testAddWithKey()
    {
        Object key = new Object();
        Object[] cArgs = new Object[0];
        ComponentCallback callback = new ComponentCallback("name", cArgs, ComponentPhase.INJECTION);
        ComponentCallback[] callbacks = {callback};

        _containerControl.expectAndReturn(_mockContainer.registerComponentConstructorArguments(key,
            cArgs), key, 1);
        _containerControl.expectAndReturn(
            _mockContainer.registerComponentCallbacks(key, callbacks), key, 1);
        _containerControl.replay();

        Object rtnVal1 = _containerAdapter.add(key, cArgs);
        //Object rtnVal2 = _containerAdapter.add(key, callback);
        Object rtnVal3 = _containerAdapter.add(key, callbacks);

        _containerControl.verify();

        assertSame(rtnVal1, key);
        //assertSame(rtnVal2, key);
        assertSame(rtnVal3, key);
    }

    public void testAddWithInterfaceAndType()
    {
        Class intf = MockComboService.class;
        Object type = "variant";
        Object key = new Object();
        Object[] cArgs = new Object[0];
        ComponentCallback callback = new ComponentCallback("name", cArgs, ComponentPhase.INJECTION);
        ComponentCallback[] callbacks = {callback};

        _containerControl.expectAndReturn(_mockContainer.getComponentKey(intf, type), key, 1);
        _containerControl.expectAndReturn(_mockContainer.registerComponentConstructorArguments(key,
            cArgs), key, 1);
        _containerControl.expectAndReturn(_mockContainer.getComponentKey(intf, type), key, 1);
        _containerControl.expectAndReturn(
            _mockContainer.registerComponentCallbacks(key, callbacks), key, 1);
        _containerControl.replay();

        Object rtnVal1 = _containerAdapter.add(intf, type, cArgs);
        //Object rtnVal2 = _containerAdapter.add(key, callback);
        Object rtnVal3 = _containerAdapter.add(intf, type, callbacks);

        _containerControl.verify();

        assertSame(rtnVal1, key);
        //assertSame(rtnVal2, key);
        assertSame(rtnVal3, key);
    }

    public void testAddWithInterface()
    {
        Class intf = MockComboService.class;
        Object key = new Object();
        Object[] cArgs = new Object[0];
        ComponentCallback callback = new ComponentCallback("name", cArgs, ComponentPhase.INJECTION);
        ComponentCallback[] callbacks = {callback};

        _containerControl.expectAndReturn(_mockContainer.getComponentKey(intf), key, 1);
        _containerControl.expectAndReturn(_mockContainer.registerComponentConstructorArguments(key,
            cArgs), key, 1);
        _containerControl.expectAndReturn(_mockContainer.getComponentKey(intf), key, 1);
        _containerControl.expectAndReturn(
            _mockContainer.registerComponentCallbacks(key, callbacks), key, 1);
        _containerControl.replay();

        Object rtnVal1 = _containerAdapter.add(intf, cArgs);
        //Object rtnVal2 = _containerAdapter.add(key, callback);
        Object rtnVal3 = _containerAdapter.add(intf, callbacks);

        _containerControl.verify();

        assertSame(rtnVal1, key);
        //assertSame(rtnVal2, key);
        assertSame(rtnVal3, key);
    }

    public void testSingleton()
    {
        Object key = new Object();

        _containerControl.expectAndReturn(_mockContainer.wrapComponentStrategyWithSingleton(key),
            key, 1);
        _containerControl.replay();

        Object key2 = _containerAdapter.singleton(key);

        _containerControl.verify();

        assertEquals(key, key2);
    }

    public void testPooling()
    {
        Object key = new Object();

        _containerControl.expectAndReturn(_mockContainer.wrapComponentStrategyWithPooling(key),
            key, 1);
        _containerControl.replay();

        Object key2 = _containerAdapter.pooling(key);

        _containerControl.verify();

        assertEquals(key, key2);
    }

    public void testThreadLocal()
    {
        Object key = new Object();

        _containerControl.expectAndReturn(_mockContainer.wrapComponentStrategyWithThreadLocal(key),
            key, 1);
        _containerControl.replay();

        Object key2 = _containerAdapter.threadLocal(key);

        _containerControl.verify();

        assertEquals(key, key2);
    }

    public void testGetComponentInstanceFromKey()
    {
        Object key = new Object();
        Object comp = new Object();

        _containerControl.expectAndReturn(_mockContainer.getComponentInstance(key), comp, 1);
        _containerControl.expectAndReturn(_mockContainer.registerComponentRetrievalLocation(key,
            new Location(null, 0)), key, 1);
        _containerControl.replay();

        Object rtnComp = _containerAdapter.componentInst(key);

        _containerControl.verify();

        assertSame(comp, rtnComp);
    }

    public void testGetComponentInstance()
    {
        Object key = new Object();
        Object comp = new Object();

        _containerControl.expectAndReturn(_mockContainer.getComponentKey(MockComboService.class,
            null), key, 1);
        _containerControl.expectAndReturn(_mockContainer.getComponentInstance(key), comp, 1);
        _containerControl.expectAndReturn(_mockContainer.registerComponentRetrievalLocation(key,
            new Location(null, 0)), key, 1);
        _containerControl.replay();

        Object rtnComp = _containerAdapter.componentInst(MockComboService.class);

        _containerControl.verify();

        assertSame(comp, rtnComp);
    }

    public void testRegisterAndGetConfigurationList()
    {
        _containerControl.expectAndReturn(_mockContainer.registerConfiguration("dbProps",
            "oracle.jdbc.driver..."), "dbProps");

        List list = new ArrayList();
        _containerControl.expectAndReturn(_mockContainer.getConfigurationList("dbProps"), list, 1);
        _containerControl.replay();

        _containerAdapter.configItem("dbProps", "oracle.jdbc.driver...");

        List dbProps = _containerAdapter.configList("dbProps");

        _containerControl.verify();

        assertSame(dbProps, list);
    }

    public void testRegisterAndGetConfigurationMap()
    {
        _containerControl.expectAndReturn(_mockContainer.registerConfiguration("dbProps", "driver",
            "oracle.jdbc.driver..."), "dbProps");

        Map map = new HashMap();
        _containerControl.expectAndReturn(_mockContainer.getConfigurationMap("dbProps"), map, 1);
        _containerControl.replay();

        _containerAdapter.configItem("dbProps", "driver", "oracle.jdbc.driver...");

        Map dbProps = _containerAdapter.configMap("dbProps");

        _containerControl.verify();

        assertSame(dbProps, map);
    }

    public void testGetNonExistentConfig()
    {
        List list = new ArrayList();
        _containerControl.expectAndReturn(_mockContainer.getConfigurationList("NonExistent"), list,
            1);
        _containerControl.replay();

        List dbProps = _containerAdapter.configList("NonExistent");

        _containerControl.verify();

        assertSame(dbProps, list);
    }
}