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
import gravity.ComponentStrategyType;
import gravity.Location;
import gravity.MutableContainer;
import gravity.MutableContext;
import gravity.Plugin;
import gravity.UsageException;
import gravity.util.ClassUtils;
import gravity.util.ThreadEvent;
import gravity.util.Utils;

import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * This is a mutable container that houses components and their configuration data.
 * <p>
 * Although components and configuration data can be added to this container at runtime, the initial
 * build step should typically be in a single startup thread.
 * <p>
 * This container is not thread safe; so any concurrent registrations that may occur during runtime
 * needs to synchronized by the client.
 * 
 * @author Harish Krishnaswamy
 * @version $Id: DefaultContainer.java,v 1.10 2004-11-17 19:49:04 harishkswamy Exp $
 */
public final class DefaultContainer implements MutableContainer
{
    private MutableContext _context;

    /**
     * Components container.
     */
    private Map            _componentCache      = new HashMap();

    /**
     * Configurations container.
     */
    private Map            _configuarationCache = new HashMap();

    /**
     * This method can be overridden to retrieve plugn manifest files from other sources.
     * 
     * @return Returns an enumeration of plugin manifest files found in the classpath.
     * @see MutableContainer#PLUGIN_MANIFEST_FILE_PATH
     */
    protected Enumeration getPluginManifestFiles()
    {
        return ClassUtils.getResources(PLUGIN_MANIFEST_FILE_PATH);
    }

    /**
     * @return Returns the location of the plugin manifest file from the provided URL.
     */
    protected String getPluginLocation(URL url)
    {
        String urlStr = url.toString();

        return urlStr.substring(0, urlStr.lastIndexOf(PLUGIN_MANIFEST_FILE_PATH));
    }

    private Plugin getPlugin(Properties pluginProps)
    {
        String pluginClassName = pluginProps.getProperty(Plugin.class.getName());

        // Return the plugin from the plugin manifest file
        if (pluginClassName != null)
            return (Plugin) ClassUtils.newInstance(pluginClassName);

        // Return the default plugin
        return (Plugin) _context.newApiInstance(Plugin.class.getName(), null);
    }

    private void acceptPlugins()
    {
        Enumeration e = getPluginManifestFiles();

        while (e.hasMoreElements())
        {
            URL url = (URL) e.nextElement();

            Properties props = _context.getProperties();

            // This will override the defaults defined in the framework properties.
            props.putAll(Utils.loadProperties(url));

            props.setProperty(Plugin.LOCATION_URL_KEY, getPluginLocation(url));

            Plugin plugin = getPlugin(props);

            plugin.startup(props, this);
        }
    }

    private void load()
    {
        acceptPlugins();
    }

    public DefaultContainer(MutableContext context)
    {
        _context = context;
        _context.setContainer(this);

        load();
    }

    public Object getComponentKey(Class compIntf, Object compType)
    {
        return ComponentKey.get(compIntf, compType);
    }

    public Object getComponentKey(Class compIntf)
    {
        return ComponentKey.get(compIntf, null);
    }

    private Component getComponent(Object compKey)
    {
        Component comp = (Component) _componentCache.get(compKey);

        if (comp == null)
        {
            Object[] args = new Object[]{_context, compKey};

            comp = (Component) _context.newApiInstance(Component.class.getName(), args);

            _componentCache.put(compKey, comp);
        }

        return comp;
    }

    // MutableContainer methods ====================================================================

    // Primary component registration method =======================================================

    /**
     * @return Component key.
     */
    public Object registerComponentImplementation(Object compKey, Object srcCompKey)
    {
        Component comp = getComponent(compKey);

        comp.registerImplementation(getComponent(srcCompKey));

        return compKey;
    }

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

    /**
     * Registers an external factory for the component.
     * 
     * @return Component key.
     */
    public Object registerComponentFactory(Object compKey, Object compFac, String facMethodName,
        Object[] facMethodArgs, ComponentCallback[] callbacks)
    {
        Component comp = getComponent(compKey);

        comp.registerFactory(compFac, facMethodName, facMethodArgs, callbacks);

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
    public Object wrapComponentStrategy(Object compKey, ComponentStrategyType strategyType)
    {
        Component comp = getComponent(compKey);

        comp.wrapStrategy(strategyType);

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
     *             When no component is registered for the supplied key.
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