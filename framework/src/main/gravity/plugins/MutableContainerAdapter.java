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
import gravity.Location;
import gravity.MutableContainer;

import java.util.List;
import java.util.Map;

/**
 * This is an adapter to MutableContainer intended to be used by plugins.
 * 
 * @see gravity.MutableContainer
 * @author Harish Krishnaswamy
 * @version $Id: MutableContainerAdapter.java,v 1.3 2004-06-14 04:23:44 harishkswamy Exp $
 */
public class MutableContainerAdapter
{
    private MutableContainer _container;

    private String           _currentModuleName;
    private int              _currentLineNumber;

    public MutableContainerAdapter(MutableContainer container)
    {
        _container = container;
    }

    public void setCurrentModuleName(String moduleName)
    {
        _currentModuleName = moduleName;
    }

    public void setCurrentLineNumber(int lineNum)
    {
        _currentLineNumber = lineNum;
    }

    public int getCurrentLineNumber()
    {
        return _currentLineNumber;
    }

    private Location getCurrentLocation()
    {
        return new Location(_currentModuleName, _currentLineNumber);
    }

    public Object componentKey(Class compIntf, Object compType)
    {
        return _container.getComponentKey(compIntf, compType);
    }

    public Object componentKey(Class compIntf)
    {
        return _container.getComponentKey(compIntf);
    }

    // Component point definition methods =====================================

    public Object componentInst(Object compKey)
    {
        Object comp = _container.getComponentInstance(compKey);

        _container.registerComponentRetrievalLocation(compKey, getCurrentLocation());

        return comp;
    }

    public Object componentInst(Class compIntf, Object compType)
    {
        Object compKey = _container.getComponentKey(compIntf, compType);

        return componentInst(compKey);
    }

    public Object componentInst(Class compIntf)
    {
        return componentInst(compIntf, null);
    }

    // Implementation registration from another component ==========================

    public Object componentImpl(Object compKey, Object srcCompKey)
    {
        _container.registerComponentImplementation(compKey, srcCompKey);

        return _container.registerComponentRegistrationLocation(compKey, getCurrentLocation());
    }

    // Implementation registration by component key ==========================

    public Object componentImpl(Object compKey, Class compClass, Object[] ctorArgs,
        ComponentCallback[] callbacks)
    {
        _container.registerComponentImplementation(compKey, compClass, ctorArgs, callbacks);

        return _container.registerComponentRegistrationLocation(compKey, getCurrentLocation());
    }

    public Object componentImpl(Object compKey, Class compClass, Object[] ctorArgs)
    {
        return componentImpl(compKey, compClass, ctorArgs, null);
    }

    public Object componentImpl(Object compKey, Class compClass, ComponentCallback[] callbacks)
    {
        return componentImpl(compKey, compClass, null, callbacks);
    }

    public Object componentImpl(Object compKey, Class compClass)
    {
        return componentImpl(compKey, compClass, null, null);
    }

    // Factory registration by component key ================================

    public Object componentFac(Object compKey, Object fac, String facMthdName,
        Object[] facMthdArgs, ComponentCallback[] callbacks)
    {
        _container.registerComponentFactory(compKey, fac, facMthdName, facMthdArgs, callbacks);

        return _container.registerComponentRegistrationLocation(compKey, getCurrentLocation());
    }

    public Object componentFac(Object compKey, Object fac, String facMthdName, Object[] facMthdArgs)
    {
        return componentFac(compKey, fac, facMthdName, facMthdArgs, null);
    }

    public Object componentFac(Object compKey, Object fac, String facMthdName,
        ComponentCallback[] callbacks)
    {
        return componentFac(compKey, fac, facMthdName, null, callbacks);
    }

    public Object componentFac(Object compKey, Object fac, String facMthdName)
    {
        return componentFac(compKey, fac, facMthdName, null, null);
    }

    // Implementation registration by interface and type =========================

    public Object componentImpl(Class compIntf, Object compType, Class compClass,
        Object[] ctorArgs, ComponentCallback[] callbacks)
    {
        Object compKey = _container.getComponentKey(compIntf, compType);

        return componentImpl(compKey, compClass, ctorArgs, callbacks);
    }

    public Object componentImpl(Class compIntf, Object compType, Class compClass, Object[] ctorArgs)
    {
        return componentImpl(compIntf, compType, compClass, ctorArgs, null);
    }

    public Object componentImpl(Class compIntf, Object compType, Class compClass,
        ComponentCallback[] callbacks)
    {
        return componentImpl(compIntf, compType, compClass, null, callbacks);
    }

    public Object componentImpl(Class compIntf, Object compType, Class compClass)
    {
        return componentImpl(compIntf, compType, compClass, null, null);
    }

    // Factory registration by component interface and type ==========================

    public Object componentFac(Class compIntf, Object compType, Object fac, String facMthdName,
        Object[] facMthdArgs, ComponentCallback[] callbacks)
    {
        Object compKey = _container.getComponentKey(compIntf, compType);

        return componentFac(compKey, fac, facMthdName, facMthdArgs, callbacks);
    }

    public Object componentFac(Class compIntf, Object compType, Object fac, String facMthdName,
        Object[] facMthdArgs)
    {
        return componentFac(compIntf, compType, fac, facMthdName, facMthdArgs, null);
    }

    public Object componentFac(Class compIntf, Object compType, Object fac, String facMthdName,
        ComponentCallback[] callbacks)
    {
        return componentFac(compIntf, compType, fac, facMthdName, null, callbacks);
    }

    public Object componentFac(Class compIntf, Object compType, Object fac, String facMthdName)
    {
        return componentFac(compIntf, compType, fac, facMthdName, null, null);
    }

    // Implementation registration methods by interface =========================

    public Object componentImpl(Class compIntf, Class compClass, Object[] ctorArgs,
        ComponentCallback[] callbacks)
    {
        return componentImpl(compIntf, null, compClass, ctorArgs, callbacks);
    }

    public Object componentImpl(Class compIntf, Class compClass, Object[] ctorArgs)
    {
        return componentImpl(compIntf, null, compClass, ctorArgs, null);
    }

    public Object componentImpl(Class compIntf, Class compClass, ComponentCallback[] callbacks)
    {
        return componentImpl(compIntf, null, compClass, null, callbacks);
    }

    public Object componentImpl(Class compIntf, Class compClass)
    {
        return componentImpl(compIntf, null, compClass, null, null);
    }

    // Factory registration by component interface ==============================

    public Object componentFac(Class compIntf, Object fac, String facMthdName,
        Object[] facMthdArgs, ComponentCallback[] callbacks)
    {
        Object compKey = _container.getComponentKey(compIntf, null);

        return componentFac(compKey, fac, facMthdName, facMthdArgs, callbacks);
    }

    public Object componentFac(Class compIntf, Object fac, String facMthdName, Object[] facMthdArgs)
    {
        return componentFac(compIntf, null, fac, facMthdName, facMthdArgs, null);
    }

    public Object componentFac(Class compIntf, Object fac, String facMthdName,
        ComponentCallback[] callbacks)
    {
        return componentFac(compIntf, null, fac, facMthdName, null, callbacks);
    }

    public Object componentFac(Class compIntf, Object fac, String facMthdName)
    {
        return componentFac(compIntf, null, fac, facMthdName, null, null);
    }

    // Implementation registration methods by implClass and type =========================

    public Object componentImpl(Class implClass, Object compType, Object[] cArgs,
        ComponentCallback[] callbacks)
    {
        return componentImpl(implClass, compType, implClass, cArgs, callbacks);
    }

    public Object componentImpl(Class implClass, Object compType, Object[] cArgs)
    {
        return componentImpl(implClass, compType, implClass, cArgs, null);
    }

    public Object componentImpl(Class implClass, Object compType, ComponentCallback[] callbacks)
    {
        return componentImpl(implClass, compType, implClass, null, callbacks);
    }

    public Object componentImpl(Class compClass, Object compType)
    {
        return componentImpl(compClass, compType, compClass, null, null);
    }

    // Implementation registration methods by implClass =======================

    public Object componentImpl(Class implClass, Object[] cArgs, ComponentCallback[] callbacks)
    {
        return componentImpl(implClass, null, implClass, cArgs, callbacks);
    }

    public Object componentImpl(Class implClass, Object[] cArgs)
    {
        return componentImpl(implClass, null, implClass, cArgs, null);
    }

    public Object componentImpl(Class implClass, ComponentCallback[] callbacks)
    {
        return componentImpl(implClass, null, implClass, null, callbacks);
    }

    public Object componentImpl(Class compClass)
    {
        return componentImpl(compClass, null, compClass, null, null);
    }

    // Constructor arguments registration ==========================================

    public Object add(Object compKey, Object[] args)
    {
        return _container.registerComponentConstructorArguments(compKey, args);
    }

    public Object add(Class compIntf, Object compType, Object[] args)
    {
        Object compKey = _container.getComponentKey(compIntf, compType);

        return _container.registerComponentConstructorArguments(compKey, args);
    }

    public Object add(Class compIntf, Object[] args)
    {
        Object compKey = _container.getComponentKey(compIntf);

        return _container.registerComponentConstructorArguments(compKey, args);
    }

    // Callbacks registration ==================================================

    public Object add(Object compKey, ComponentCallback callback)
    {
        ComponentCallback[] callbacks = {callback};

        return _container.registerComponentCallbacks(compKey, callbacks);
    }

    public Object add(Class compIntf, Object compType, ComponentCallback callback)
    {
        Object compKey = _container.getComponentKey(compIntf, compType);

        ComponentCallback[] callbacks = {callback};

        return _container.registerComponentCallbacks(compKey, callbacks);
    }

    public Object add(Class compIntf, ComponentCallback callback)
    {
        Object compKey = _container.getComponentKey(compIntf);

        ComponentCallback[] callbacks = {callback};

        return _container.registerComponentCallbacks(compKey, callbacks);
    }

    public Object add(Object compKey, ComponentCallback[] callbacks)
    {
        return _container.registerComponentCallbacks(compKey, callbacks);
    }

    public Object add(Class compIntf, Object compType, ComponentCallback[] callbacks)
    {
        Object compKey = _container.getComponentKey(compIntf, compType);

        return _container.registerComponentCallbacks(compKey, callbacks);
    }

    public Object add(Class compIntf, ComponentCallback[] callbacks)
    {
        Object compKey = _container.getComponentKey(compIntf);

        return _container.registerComponentCallbacks(compKey, callbacks);
    }

    // Singleton component helper method =========================================

    public Object singleton(Object compKey)
    {
        return _container.wrapComponentStrategyWithSingleton(compKey);
    }

    public Object pooling(Object compKey)
    {
        return _container.wrapComponentStrategyWithPooling(compKey);
    }

    public Object threadLocal(Object compKey)
    {
        return _container.wrapComponentStrategyWithThreadLocal(compKey);
    }

    // Config point definition helper methods =================================

    public List configList(Object configKey)
    {
        return _container.getConfigurationList(configKey);
    }

    public Map configMap(Object configKey)
    {
        return _container.getConfigurationMap(configKey);
    }

    // Config contribution helper methods ======================================

    public Object configItem(Object configKey, Object configItem)
    {
        return _container.registerConfiguration(configKey, configItem);
    }

    public Object configItem(Object configKey, Object configItemKey, Object configItem)
    {
        return _container.registerConfiguration(configKey, configItemKey, configItem);
    }
}