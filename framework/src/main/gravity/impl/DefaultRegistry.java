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

import gravity.ComponentFactory;
import gravity.LazyComponentFactory;
import gravity.Location;
import gravity.MutableRegistry;
import gravity.UsageException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO need support for pooled components
/**
 * This is the container that houses all components and configurations.
 * 
 * @author Harish Krishnaswamy
 * @version $Id: DefaultRegistry.java,v 1.1 2004-05-10 17:28:53 harishkswamy Exp $
 */
public class DefaultRegistry implements MutableRegistry
{
    /**
     * Components container.
     */
    private Map _componentFactoryCache = new HashMap();

    /**
     * Configurations container.
     */
    private Map _configuarationCache   = new HashMap();

    private ComponentKey getComponentKey(Class compIntf, Object compType)
    {
        return new ComponentKey(compIntf, compType);
    }

    protected ComponentFactory newDefaultComponentFactory(ComponentKey compKey)
    {
        return new DefaultComponentFactory(compKey);
    }

    private ComponentFactory getComponentFactory(ComponentKey compKey)
    {
        ComponentFactory compFactory = (ComponentFactory) _componentFactoryCache.get(compKey);

        if (compFactory == null)
        {
            compFactory = newDefaultComponentFactory(compKey);

            _componentFactoryCache.put(compKey, compFactory);
        }

        return compFactory;
    }

    // MutableRegistry methods =====================================================================

    // Primary component registration method =======================================================

    /**
     * @return Component key.
     */
    public Object registerComponentImplementation(Class compIntf, Object compType, Class compClass,
        Object[] ctorArgs, Map setrArgs)
    {
        ComponentKey compKey = getComponentKey(compIntf, compType);

        ComponentFactory compFactory = getComponentFactory(compKey);

        compFactory.registerComponentImplementation(compClass, ctorArgs, setrArgs);

        return compKey;
    }

    // Variant service registration convenience methods ============================================

    public Object registerComponentImplementation(Class compIntf, Object compType, Class compClass,
        Object[] ctorArgs)
    {
        return registerComponentImplementation(compIntf, compType, compClass, ctorArgs, null);
    }

    public Object registerComponentImplementation(Class compIntf, Object compType, Class compClass,
        Map setrArgs)
    {
        return registerComponentImplementation(compIntf, compType, compClass, null, setrArgs);
    }

    // Default service registration convenience methods ============================================

    public Object registerComponentImplementation(Class compIntf, Class compClass,
        Object[] ctorArgs, Map setrArgs)
    {
        return registerComponentImplementation(compIntf, null, compClass, ctorArgs, setrArgs);
    }

    public Object registerComponentImplementation(Class compIntf, Class compClass, Object[] ctorArgs)
    {
        return registerComponentImplementation(compIntf, null, compClass, ctorArgs, null);
    }

    public Object registerComponentImplementation(Class compIntf, Class compClass, Map setrArgs)
    {
        return registerComponentImplementation(compIntf, null, compClass, null, setrArgs);
    }

    // Variant component registration convenience methods ==========================================

    public Object registerComponentImplementation(Class compClass, Object compType,
        Object[] ctorArgs, Map setrArgs)
    {
        return registerComponentImplementation(compClass, compType, compClass, ctorArgs, setrArgs);
    }

    public Object registerComponentImplementation(Class compClass, Object compType,
        Object[] ctorArgs)
    {
        return registerComponentImplementation(compClass, compType, compClass, ctorArgs, null);
    }

    public Object registerComponentImplementation(Class compClass, Object compType, Map setrArgs)
    {
        return registerComponentImplementation(compClass, compType, compClass, null, setrArgs);
    }

    // Default component registration convenience methods ==========================================

    public Object registerComponentImplementation(Class compClass, Object[] ctorArgs, Map setrArgs)
    {
        return registerComponentImplementation(compClass, null, compClass, ctorArgs, setrArgs);
    }

    public Object registerComponentImplementation(Class compClass, Object[] ctorArgs)
    {
        return registerComponentImplementation(compClass, null, compClass, ctorArgs, null);
    }

    public Object registerComponentImplementation(Class compClass, Map setrArgs)
    {
        return registerComponentImplementation(compClass, null, compClass, null, setrArgs);
    }

    // Location registration methods ===============================================================

    public Object registerComponentRegistrationLocation(Object compKey, Location location)
    {
        ComponentFactory compFac = (ComponentFactory) _componentFactoryCache.get(compKey);

        compFac.registerComponentRegistrationLocation(location);

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
        ComponentFactory compFac = (ComponentFactory) _componentFactoryCache.get(compKey);

        compFac.registerComponentRetrievalLocation(location);

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

    protected LazyComponentFactory newSingletonComponentFactory(LazyComponentFactory delegate)
    {
        return new SingletonComponentFactory(delegate);
    }

    /**
     * @return Component key.
     */
    public Object makeComponentSingleton(Object compKey)
    {
        LazyComponentFactory compFactory = (LazyComponentFactory) _componentFactoryCache.get(compKey);

        // If already a singleton, ignore
        if (!(compFactory instanceof SingletonComponentFactory))
        {
            compFactory = newSingletonComponentFactory(compFactory);

            _componentFactoryCache.put(compKey, compFactory);
        }

        return compKey;
    }

    protected LazyComponentFactory newPooledComponentFactory(LazyComponentFactory delegate)
    {
        return new PooledComponentFactory(delegate);
    }

    public Object makeComponentPooled(Object compKey)
    {
        LazyComponentFactory compFactory = (LazyComponentFactory) _componentFactoryCache.get(compKey);

        // If already pooled, ignore
        if (!(compFactory instanceof PooledComponentFactory))
        {
            compFactory = newPooledComponentFactory(compFactory);

            _componentFactoryCache.put(compKey, compFactory);
        }

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

    // Registry methods ============================================================================

    public Object getComponentInstance(Object compKey)
    {
        ComponentFactory compFactory = getComponentFactory((ComponentKey) compKey);

        return compFactory.getComponentInstance();
    }

    /**
     * Gets the service registered for the supplied service key (service interface + service
     * implementation type).
     * 
     * @return The service registered for the supplied key.
     * @throws UsageException
     *         When no service is registered for the supplied key.
     */
    public Object getComponentInstance(Class compIntf, Object compType)
    {
        ComponentKey compKey = getComponentKey(compIntf, compType);

        return getComponentInstance(compKey);
    }

    /**
     * Gets the default service registered for the supplied service interface.
     * 
     * @return The service registered for the supplied key.
     * @throws UsageException
     *         When no service is registered for the supplied key.
     */
    public Object getComponentInstance(Class compIntf)
    {
        return getComponentInstance(compIntf, null);
    }

    public void returnComponentInstance(Class compIntf, Object compType, Object comp)
    {
        ComponentKey compKey = getComponentKey(compIntf, compType);

        ComponentFactory compFactory = getComponentFactory(compKey);

        compFactory.returnComponentInstance(comp);
    }

    public void returnComponentInstance(Class compIntf, Object comp)
    {
        returnComponentInstance(compIntf, null, comp);
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

    /**
     * Wipes out the services and configurations registered in the registry.
     */
    public void cleanup()
    {
        _componentFactoryCache = new HashMap();
        _configuarationCache = new HashMap();
    }
}