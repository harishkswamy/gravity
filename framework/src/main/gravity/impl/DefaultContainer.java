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

import gravity.Component;
import gravity.ComponentCallback;
import gravity.Location;
import gravity.MutableContainer;
import gravity.UsageException;
import gravity.util.ThreadEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the container that houses all components and configurations.
 * 
 * @author Harish Krishnaswamy
 * @version $Id: DefaultContainer.java,v 1.6 2004-05-27 03:36:33 harishkswamy Exp $
 */
public class DefaultContainer implements MutableContainer
{
    /**
     * Components container.
     */
    private Map _componentCache      = new HashMap();

    /**
     * Configurations container.
     */
    private Map _configuarationCache = new HashMap();

    private ComponentKey getComponentKey(Class compIntf, Object compType)
    {
        return new ComponentKey(compIntf, compType);
    }

    protected Component newDefaultComponent(ComponentKey compKey)
    {
        return new DefaultComponent(compKey);
    }

    private Component getComponent(ComponentKey compKey)
    {
        Component compFactory = (Component) _componentCache.get(compKey);

        if (compFactory == null)
        {
            compFactory = newDefaultComponent(compKey);

            _componentCache.put(compKey, compFactory);
        }

        return compFactory;
    }

    // MutableContainer methods ====================================================================

    // Primary component registration method =======================================================

    /**
     * @return Component key.
     */
    public Object registerComponentImplementation(Class compIntf, Object compType, Class compClass,
        Object[] ctorArgs, ComponentCallback[] callbacks)
    {
        ComponentKey compKey = getComponentKey(compIntf, compType);

        Component comp = getComponent(compKey);

        comp.registerImplementation(compClass, ctorArgs, callbacks);

        return compKey;
    }

    public Object registerComponentImplementation(Class compIntf, Class compClass,
        Object[] ctorArgs, ComponentCallback[] callbacks)
    {
        return registerComponentImplementation(compIntf, null, compClass, ctorArgs, callbacks);
    }

    public Object registerComponentImplementation(Class compClass, Object compType,
        Object[] ctorArgs, ComponentCallback[] callbacks)
    {
        return registerComponentImplementation(compClass, compType, compClass, ctorArgs, callbacks);
    }

    public Object registerComponentImplementation(Class compClass, Object[] ctorArgs,
        ComponentCallback[] callbacks)
    {
        return registerComponentImplementation(compClass, null, compClass, ctorArgs, callbacks);
    }

    // Constructor arguments registration ===============================================

    public Object registerComponentConstructorArguments(Object compKey, Object[] args)
    {
        Component comp = (Component) _componentCache.get(compKey);

        comp.registerConstructorArguments(args);

        return compKey;
    }

    public Object registerComponentConstructorArguments(Class compIntf, Object compType,
        Object[] args)
    {
        ComponentKey compKey = getComponentKey(compIntf, compType);

        return registerComponentConstructorArguments(compKey, args);
    }

    public Object registerComponentConstructorArguments(Class compIntf, Object[] args)
    {
        ComponentKey compKey = getComponentKey(compIntf, null);

        return registerComponentConstructorArguments(compKey, args);
    }

    // LifeCycle methods registration ===============================================

    public Object registerComponentCallbacks(Object compKey, ComponentCallback[] callbacks)
    {
        Component comp = (Component) _componentCache.get(compKey);

        comp.registerCallbackMethods(callbacks);

        return compKey;
    }

    public Object registerComponentCallbacks(Class compIntf, Object compType,
        ComponentCallback[] callbacks)
    {
        ComponentKey compKey = getComponentKey(compIntf, compType);

        return registerComponentCallbacks(compKey, callbacks);
    }

    public Object registerComponentCallbacks(Class compIntf, ComponentCallback[] callbacks)
    {
        ComponentKey compKey = getComponentKey(compIntf, null);

        return registerComponentCallbacks(compKey, callbacks);
    }

    // Location registration methods ===============================================================

    public Object registerComponentRegistrationLocation(Object compKey, Location location)
    {
        Component comp = (Component) _componentCache.get(compKey);

        comp.setRegistrationLocation(location);

        return compKey;
    }

    public Object registerComponentRegistrationLocation(Class compIntf, Object compType,
        Location location)
    {
        ComponentKey compKey = getComponentKey(compIntf, compType);

        return registerComponentRegistrationLocation(compKey, location);
    }

    public Object registerComponentRegistrationLocation(Class compIntf, Location location)
    {
        ComponentKey compKey = getComponentKey(compIntf, null);

        return registerComponentRegistrationLocation(compKey, location);
    }

    public Object registerComponentRetrievalLocation(Object compKey, Location location)
    {
        Component comp = (Component) _componentCache.get(compKey);

        comp.setRetrievalLocation(location);

        return compKey;
    }

    public Object registerComponentRetrievalLocation(Class compIntf, Object compType,
        Location location)
    {
        ComponentKey compKey = getComponentKey(compIntf, compType);

        return registerComponentRetrievalLocation(compKey, location);
    }

    public Object registerComponentRetrievalLocation(Class compIntf, Location location)
    {
        ComponentKey compKey = getComponentKey(compIntf, null);

        return registerComponentRetrievalLocation(compKey, location);
    }

    // Component factory decorator methods =========================================================

    /**
     * @return Component _key.
     */
    public Object wrapComponentStrategyWithSingleton(Object compKey)
    {
        Component comp = (Component) _componentCache.get(compKey);

        comp.wrapStrategyWithSingleton();

        return compKey;
    }

    public Object wrapComponentStrategyWithPooling(Object compKey)
    {
        Component comp = (Component) _componentCache.get(compKey);

        comp.wrapStrategyWithPooling();

        return compKey;
    }

    public Object wrapComponentStrategyWithThreadLocal(Object compKey)
    {
        Component comp = (Component) _componentCache.get(compKey);

        comp.wrapStrategyWithThreadLocal();

        return compKey;
    }

    // Configuration methods =======================================================================

    public Object registerConfiguration(Object configKey, Object configItem)
    {
        List configList = getConfigurationList(configKey);

        configList.add(configItem);

        return configKey;
    }

    public Object registerConfiguration(Object configKey, Object configItemKey, Object configItem)
    {
        Map configMap = getConfigurationMap(configKey);

        configMap.put(configItemKey, configItem);

        return configKey;
    }

    // Container methods ===========================================================================

    public Object getComponentInstance(Object compKey)
    {
        Component comp = getComponent((ComponentKey) compKey);

        return comp.getInstance();
    }

    /**
     * Gets the service registered for the supplied service _key (service interface + service
     * implementation type).
     * 
     * @return The service registered for the supplied _key.
     * @throws UsageException
     *         When no service is registered for the supplied _key.
     */
    public Object getComponentInstance(Class compIntf, Object compType)
    {
        ComponentKey compKey = getComponentKey(compIntf, compType);

        return getComponentInstance(compKey);
    }

    /**
     * Gets the default service registered for the supplied service interface.
     * 
     * @return The service registered for the supplied _key.
     * @throws UsageException
     *         When no service is registered for the supplied _key.
     */
    public Object getComponentInstance(Class compIntf)
    {
        return getComponentInstance(compIntf, null);
    }

    public void collectComponentInstance(Class compIntf, Object compType, Object compInst)
    {
        ComponentKey compKey = getComponentKey(compIntf, compType);

        Component comp = getComponent(compKey);

        comp.collectInstance(compInst);
    }

    public void collectComponentInstance(Class compIntf, Object comp)
    {
        collectComponentInstance(compIntf, null, comp);
    }

    /**
     * Gets the configuration registered for the supplied _key.
     * 
     * @return The configuration list.
     */
    public List getConfigurationList(Object configKey)
    {
        List configList = (List) _configuarationCache.get(configKey);

        if (configList == null)
        {
            configList = new ArrayList();

            _configuarationCache.put(configKey, configList);
        }

        return configList;
    }

    /**
     * Gets the configuration registered for the supplied _key.
     * 
     * @return The configuration map.
     */
    public Map getConfigurationMap(Object configKey)
    {
        Map configMap = (Map) _configuarationCache.get(configKey);

        if (configMap == null)
        {
            configMap = new HashMap();

            _configuarationCache.put(configKey, configMap);
        }

        return configMap;
    }

    public void handlePreThreadTermination()
    {
        ThreadEvent.getInstance().notifyPreTerminationObservers();
    }

    /**
     * Wipes out the services and configurations registered in the registry.
     */
    public void cleanup()
    {
        _componentCache = new HashMap();
        _configuarationCache = new HashMap();
    }
}