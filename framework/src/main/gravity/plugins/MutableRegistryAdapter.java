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

import gravity.Location;
import gravity.MutableContainer;

import java.util.List;
import java.util.Map;

/**
 * This is an adapter to MutableContainer intended to be used by plugins.
 * 
 * @see gravity.MutableContainer
 * @author Harish Krishnaswamy
 * @version $Id: MutableRegistryAdapter.java,v 1.5 2004-05-22 20:19:37 harishkswamy Exp $
 */
public class MutableRegistryAdapter
{
    private MutableContainer _registry;

    private String          _currentModuleName;
    private int             _currentLineNumber;

    public MutableRegistryAdapter(MutableContainer registry)
    {
        _registry = registry;
    }

    public void setCurrentModuleName(String moduleName)
    {
        _currentModuleName = moduleName;
    }

    public void setCurrentLineNumber(int lineNum)
    {
        _currentLineNumber = lineNum;
    }

    private Location getCurrentLocation()
    {
        return new Location(_currentModuleName, _currentLineNumber);
    }

    // Component point definition methods =====================================

    public Object component(Object compKey)
    {
        Object comp = _registry.getComponentInstance(compKey);

        _registry.registerComponentRetrievalLocation(compKey, getCurrentLocation());

        return comp;
    }

    public Object component(Class compIntf, Object compType)
    {
        Object comp = _registry.getComponentInstance(compIntf, compType);

        _registry.registerComponentRetrievalLocation(compIntf, compType, getCurrentLocation());

        return comp;
    }

    public Object component(Class compIntf)
    {
        Object comp = _registry.getComponentInstance(compIntf);

        _registry.registerComponentRetrievalLocation(compIntf, getCurrentLocation());

        return comp;
    }

    // Custom interface component registration methods =========================

    public Object componentImpl(Class compIntf, Object compType, Class compClass,
        Object[] ctorArgs, Map setrArgs)
    {
        Object compKey = _registry.registerComponentImplementation(compIntf, compType, compClass,
            ctorArgs, setrArgs);

        return _registry.registerComponentRegistrationLocation(compKey, getCurrentLocation());
    }

    public Object componentImpl(Class compIntf, Object compType, Class compClass, Object[] ctorArgs)
    {
        Object compKey = _registry.registerComponentImplementation(compIntf, compType, compClass,
            ctorArgs);

        return _registry.registerComponentRegistrationLocation(compKey, getCurrentLocation());
    }

    public Object componentImpl(Class compIntf, Object compType, Class compClass, Map setrArgs)
    {
        Object compKey = _registry.registerComponentImplementation(compIntf, compType, compClass,
            setrArgs);

        return _registry.registerComponentRegistrationLocation(compKey, getCurrentLocation());
    }

    // Default interface component registration methods =========================

    public Object componentImpl(Class compIntf, Class compClass, Object[] ctorArgs, Map setrArgs)
    {
        Object compKey = _registry.registerComponentImplementation(compIntf, compClass, ctorArgs,
            setrArgs);

        return _registry.registerComponentRegistrationLocation(compKey, getCurrentLocation());
    }

    public Object componentImpl(Class compIntf, Class compClass, Object[] ctorArgs)
    {
        Object compKey = _registry.registerComponentImplementation(compIntf, compClass, ctorArgs);

        return _registry.registerComponentRegistrationLocation(compKey, getCurrentLocation());
    }

    public Object componentImpl(Class compIntf, Class compClass, Map setrArgs)
    {
        Object compKey = _registry.registerComponentImplementation(compIntf, compClass, setrArgs);

        return _registry.registerComponentRegistrationLocation(compKey, getCurrentLocation());
    }

    // Custom class component registration methods =========================

    public Object componentImpl(Class implClass, Object implType, Object[] cArgs, Map sArgs)
    {
        Object compKey = _registry.registerComponentImplementation(implClass, implType, implClass,
            cArgs, sArgs);

        return _registry.registerComponentRegistrationLocation(compKey, getCurrentLocation());
    }

    public Object componentImpl(Class implClass, Object implType, Object[] cArgs)
    {
        Object compKey = _registry.registerComponentImplementation(implClass, implType, cArgs);

        return _registry.registerComponentRegistrationLocation(compKey, getCurrentLocation());
    }

    public Object componentImpl(Class implClass, Object implType, Map sArgs)
    {
        Object compKey = _registry.registerComponentImplementation(implClass, implType, implClass,
            sArgs);

        return _registry.registerComponentRegistrationLocation(compKey, getCurrentLocation());
    }

    // Default class component registration methods =======================

    public Object componentImpl(Class implClass, Object[] cArgs, Map sArgs)
    {
        Object compKey = _registry.registerComponentImplementation(implClass, cArgs, sArgs);

        return _registry.registerComponentRegistrationLocation(compKey, getCurrentLocation());
    }

    public Object componentImpl(Class implClass, Object[] cArgs)
    {
        Object compKey = _registry.registerComponentImplementation(implClass, cArgs);

        return _registry.registerComponentRegistrationLocation(compKey, getCurrentLocation());
    }

    public Object componentImpl(Class implClass, Map sArgs)
    {
        Object compKey = _registry.registerComponentImplementation(implClass, sArgs);

        return _registry.registerComponentRegistrationLocation(compKey, getCurrentLocation());
    }

    // Singleton service helper method =========================================

    public Object singleton(Object compKey)
    {
        return _registry.wrapComponentStrategyWithSingleton(compKey);
    }

    public Object pooling(Object compKey)
    {
        return _registry.wrapComponentStrategyWithPooling(compKey);
    }

    public Object threadLocal(Object compKey)
    {
        return _registry.wrapComponentStrategyWithThreadLocal(compKey);
    }

    // Config point definition helper methods =================================

    public List configList(Object configKey)
    {
        return _registry.getConfigurationList(configKey);
    }

    public Map configMap(Object configKey)
    {
        return _registry.getConfigurationMap(configKey);
    }

    // Config contribution helper methods ======================================

    public Object configItem(Object configKey, Object configItem)
    {
        return _registry.registerConfiguration(configKey, configItem);
    }

    public Object configItem(Object configKey, Object configItemKey, Object configItem)
    {
        return _registry.registerConfiguration(configKey, configItemKey, configItem);
    }
}