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

import gravity.ComponentLifeCycleMethod;
import gravity.Location;
import gravity.MutableContainer;

import java.util.List;
import java.util.Map;

/**
 * This is an adapter to MutableContainer intended to be used by plugins.
 * 
 * @see gravity.MutableContainer
 * @author Harish Krishnaswamy
 * @version $Id: MutableContainerAdapter.java,v 1.1 2004-05-24 00:38:42 harishkswamy Exp $
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

    private Location getCurrentLocation()
    {
        return new Location(_currentModuleName, _currentLineNumber);
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
        Object comp = _container.getComponentInstance(compIntf, compType);

        _container.registerComponentRetrievalLocation(compIntf, compType, getCurrentLocation());

        return comp;
    }

    public Object componentInst(Class compIntf)
    {
        Object comp = _container.getComponentInstance(compIntf);

        _container.registerComponentRetrievalLocation(compIntf, getCurrentLocation());

        return comp;
    }

    // Custom interface component registration methods =========================

    public Object componentImpl(Class compIntf, Object compType, Class compClass,
        Object[] ctorArgs, ComponentLifeCycleMethod[] lifeCycleMethods)
    {
        Object compKey = _container.registerComponentImplementation(compIntf, compType, compClass,
            ctorArgs, lifeCycleMethods);

        return _container.registerComponentRegistrationLocation(compKey, getCurrentLocation());
    }

    public Object componentImpl(Class compIntf, Object compType, Class compClass, Object[] ctorArgs)
    {
        return componentImpl(compIntf, compType, compClass, ctorArgs, null);
    }

    public Object componentImpl(Class compIntf, Object compType, Class compClass,
        ComponentLifeCycleMethod[] lifeCycleMethods)
    {
        return componentImpl(compIntf, compType, compClass, null, lifeCycleMethods);
    }

    // Default interface component registration methods =========================

    public Object componentImpl(Class compIntf, Class compClass, Object[] ctorArgs,
        ComponentLifeCycleMethod[] lifeCycleMethods)
    {
        return componentImpl(compIntf, null, compClass, ctorArgs, lifeCycleMethods);
    }

    public Object componentImpl(Class compIntf, Class compClass, Object[] ctorArgs)
    {
        return componentImpl(compIntf, null, compClass, ctorArgs, null);
    }

    public Object componentImpl(Class compIntf, Class compClass,
        ComponentLifeCycleMethod[] lifeCycleMethods)
    {
        return componentImpl(compIntf, null, compClass, null, lifeCycleMethods);
    }

    // Custom class component registration methods =========================

    public Object componentImpl(Class implClass, Object implType, Object[] cArgs,
        ComponentLifeCycleMethod[] lifeCycleMethods)
    {
        return componentImpl(implClass, implType, implClass, cArgs, lifeCycleMethods);
    }

    public Object componentImpl(Class implClass, Object implType, Object[] cArgs)
    {
        return componentImpl(implClass, implType, implClass, cArgs, null);
    }

    public Object componentImpl(Class implClass, Object implType,
        ComponentLifeCycleMethod[] lifeCycleMethods)
    {
        return componentImpl(implClass, implType, implClass, null, lifeCycleMethods);
    }

    // Default class component registration methods =======================

    public Object componentImpl(Class implClass, Object[] cArgs,
        ComponentLifeCycleMethod[] lifeCycleMethods)
    {
        return componentImpl(implClass, null, implClass, cArgs, lifeCycleMethods);
    }

    public Object componentImpl(Class implClass, Object[] cArgs)
    {
        return componentImpl(implClass, null, implClass, cArgs, null);
    }

    public Object componentImpl(Class implClass, ComponentLifeCycleMethod[] lifeCycleMethods)
    {
        return componentImpl(implClass, null, implClass, null, lifeCycleMethods);
    }
    
    // Only implementation registration =========================================
    
    public Object componentImpl(Class compIntf, Class compClass)
    {
        return componentImpl(compIntf, null, compClass, null, null);
    }

    public Object componentImpl(Class compIntf, Object compType, Class compClass)
    {
        return componentImpl(compIntf, compType, compClass, null, null);
    }

    public Object componentImpl(Class compClass)
    {
        return componentImpl(compClass, null, compClass, null, null);
    }

    public Object componentImpl(Class compClass, Object compType)
    {
        return componentImpl(compClass, compType, compClass, null, null);
    }

    // Constructor arguments registration ==========================================

    public Object add(Object compKey, Object[] args)
    {
        return _container.registerComponentConstructorArguments(compKey, args);
    }

    public Object add(Class compIntf, Object compType, Object[] args)
    {
        return _container.registerComponentConstructorArguments(compIntf, compType, args);
    }

    public Object add(Class compIntf, Object[] args)
    {
        return _container.registerComponentConstructorArguments(compIntf, args);
    }

    // Life cycle methods registration =============================================

    public Object add(Object compKey, ComponentLifeCycleMethod method)
    {
        ComponentLifeCycleMethod[] methods = {method};

        return _container.registerComponentLifeCycleMethods(compKey, methods);
    }

    public Object add(Class compIntf, Object compType, ComponentLifeCycleMethod method)
    {
        ComponentLifeCycleMethod[] methods = {method};

        return _container.registerComponentLifeCycleMethods(compIntf, compType, methods);
    }

    public Object add(Class compIntf, ComponentLifeCycleMethod method)
    {
        ComponentLifeCycleMethod[] methods = {method};

        return _container.registerComponentLifeCycleMethods(compIntf, methods);
    }

    public Object add(Object compKey, ComponentLifeCycleMethod[] methods)
    {
        return _container.registerComponentLifeCycleMethods(compKey, methods);
    }

    public Object add(Class compIntf, Object compType, ComponentLifeCycleMethod[] methods)
    {
        return _container.registerComponentLifeCycleMethods(compIntf, compType, methods);
    }

    public Object add(Class compIntf, ComponentLifeCycleMethod[] methods)
    {
        return _container.registerComponentLifeCycleMethods(compIntf, methods);
    }

    // Singleton service helper method =========================================

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