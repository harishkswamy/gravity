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
 * @version $Id: DefaultContainer.java,v 1.7 2004-05-29 16:43:33 harishkswamy Exp $
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

    public Object getComponentKey(Class compIntf, Object compType)
    {
        return ComponentKey.get(compIntf, compType);
    }

    public Object getComponentKey(Class compIntf)
    {
        return ComponentKey.get(compIntf, null);
    }

    protected Component newDefaultComponent(Object compKey)
    {
        return new DefaultComponent((ComponentKey) compKey);
    }

    private Component getComponent(Object compKey)
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
    public Object registerComponentImplementation(Object compKey, Class compClass,
        Object[] ctorArgs, ComponentCallback[] callbacks)
    {
        Component comp = getComponent(compKey);

        comp.registerImplementation(compClass, ctorArgs, callbacks);

        return compKey;
    }

    // Constructor arguments registration ===============================================

    public Object registerComponentConstructorArguments(Object compKey, Object[] args)
    {
        Component comp = getComponent(compKey);

        comp.registerConstructorArguments(args);

        return compKey;
    }

    // LifeCycle methods registration ===============================================

    public Object registerComponentCallbacks(Object compKey, ComponentCallback[] callbacks)
    {
        Component comp = getComponent(compKey);

        comp.registerCallbacks(callbacks);

        return compKey;
    }

    // Location registration methods ===============================================================

    public Object registerComponentRegistrationLocation(Object compKey, Location location)
    {
        Component comp = getComponent(compKey);

        comp.setRegistrationLocation(location);

        return compKey;
    }

    public Object registerComponentRetrievalLocation(Object compKey, Location location)
    {
        Component comp = getComponent(compKey);

        comp.setRetrievalLocation(location);

        return compKey;
    }

    // Component factory decorator methods =========================================================

    /**
     * @return Component key.
     */
    public Object wrapComponentStrategyWithSingleton(Object compKey)
    {
        Component comp = getComponent(compKey);

        comp.wrapStrategyWithSingleton();

        return compKey;
    }

    public Object wrapComponentStrategyWithPooling(Object compKey)
    {
        Component comp = getComponent(compKey);

        comp.wrapStrategyWithPooling();

        return compKey;
    }

    public Object wrapComponentStrategyWithThreadLocal(Object compKey)
    {
        Component comp = getComponent(compKey);

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

    /**
     * Gets the component registered for the supplied component key (component interface + component
     * implementation type).
     * 
     * @return The component registered for the supplied key.
     * @throws UsageException
     *         When no component is registered for the supplied key.
     */
    public Object getComponentInstance(Object compKey)
    {
        Component comp = getComponent(compKey);

        return comp.getInstance();
    }

    public void collectComponentInstance(Object compKey, Object compInst)
    {
        Component comp = getComponent(compKey);

        comp.collectInstance(compInst);
    }

    /**
     * Gets the configuration registered for the supplied key.
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
     * Gets the configuration registered for the supplied key.
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
     * Wipes out the components and configurations registered with the container.
     */
    public void cleanup()
    {
        _componentCache = new HashMap();
        _configuarationCache = new HashMap();
    }
}