// Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package gravity.impl;

import gravity.Component;
import gravity.ComponentCallback;
import gravity.ComponentKey;
import gravity.ComponentStrategyType;
import gravity.Context;
import gravity.Location;
import gravity.MutableContainer;
import gravity.Plugin;
import gravity.UsageException;
import gravity.util.Message;

import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

// TODO Container hierarchy

/**
 * This is a mutable container that houses components and their configuration data.
 * <p>
 * Although components and configuration data can be added to this container at runtime, the initial
 * build step should typically be in a single startup thread.
 * <p>
 * This container is not thread safe; so any concurrent registrations that may occur during runtime
 * needs to be synchronized by the client.
 * 
 * @author Harish Krishnaswamy
 * @version $Id: DefaultContainer.java,v 1.11 2005-10-06 21:59:27 harishkswamy Exp $
 */
public final class DefaultContainer implements MutableContainer
{
    private Context                     _context;

    /**
     * Components container.
     */
    private final Map                   _componentCache     = new HashMap();

    /**
     * Configurations container.
     */
    private final Map                   _configurationCache = new HashMap();

    /**
     * Component keys generator/container.
     */
    private final ComponentKeyGenerator _componentKeyGenerator;

    public DefaultContainer(Context context)
    {
        _context = context;

        _componentKeyGenerator = new ComponentKeyGenerator(context);
    }

    /**
     * @return Returns the location of the plugin manifest file from the provided URL.
     */
    protected String getPluginManifestLocation(URL url)
    {
        String urlStr = url.toString();

        return urlStr.substring(0, urlStr.lastIndexOf('/') + 1);
    }

    protected void startPlugin(URL url)
    {
        // Each plugin is invoked with a new context, so each plugin can override the framework
        // defaults as it pleases.
        _context.decorateApplicationContext(null, url);

        _context.putContextItem(Plugin.MANIFEST_LOCATION_URL_KEY, getPluginManifestLocation(url));

        Plugin plugin = (Plugin) _context.newApiInstance(Plugin.class);

        plugin.startup(_context);

        // Switch back to the root context when plugin load is complete.
        _context.stripApplicationContext();
    }

    protected void loadPlugins(String classpath)
    {
        Enumeration e = _context.getClassUtils().getResources(classpath);

        while (e.hasMoreElements())
        {
            URL url = (URL) e.nextElement();

            startPlugin(url);
        }
    }

    /**
     * Discover plugins and load them.
     */
    protected void autoloadPlugins()
    {
        loadPlugins((String) _context.getContextItem(PLUGINS_DEFAULT_MANIFEST_CLASSPATH_KEY));
    }

    /**
     * Only loads plugins specified in the context.
     */
    protected void loadPluginsFromUrl()
    {
        String urlStr = (String) _context.getContextItem(PLUGINS_MANIFEST_URL_KEY);

        List urlList = _context.getUtils().splitQuoted(urlStr, ',');

        for (Iterator itr = urlList.iterator(); itr.hasNext();)
        {
            URL url = _context.getClassUtils().newUrl((String) itr.next());

            startPlugin(url);
        }
    }

    protected void loadPluginsFromClassPath()
    {
        String urlStr = (String) _context.getContextItem(PLUGINS_MANIFEST_CLASSPATH_KEY);

        List urlList = _context.getUtils().splitQuoted(urlStr, ',');

        for (Iterator itr = urlList.iterator(); itr.hasNext();)
        {
            String pluginManifestFilePath = (String) itr.next();

            loadPlugins(pluginManifestFilePath);
        }
    }

    public void load()
    {
        loadPluginsFromUrl();
        loadPluginsFromClassPath();

        Boolean autoload = (Boolean) _context.getContextItem(PLUGINS_AUTOLOAD_KEY);

        if (autoload.booleanValue())
            autoloadPlugins();
    }

    public void switchContextTo(Context context)
    {
        _context = context;
    }

    // End initialize ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public ComponentKey getComponentKey(Class compIntf, Object compType)
    {
        return _componentKeyGenerator.get(compIntf, compType);
    }

    public ComponentKey getComponentKey(Class compIntf)
    {
        return getComponentKey(compIntf, null);
    }

    private Component getComponent(ComponentKey compKey)
    {
        Component comp = (Component) _componentCache.get(compKey);

        if (comp == null)
        {
            comp = (Component) _context.newApiInstance(Component.class);
            comp.initialize(_context, compKey);

            _componentCache.put(compKey, comp);
        }

        return comp;
    }

    // MutableContainer methods ====================================================================

    // Primary component registration method =======================================================

    /**
     * @return Component key.
     */
    public ComponentKey registerComponentImplementation(ComponentKey compKey,
        ComponentKey srcCompKey)
    {
        Component comp = getComponent(compKey);

        comp.registerImplementation(getComponent(srcCompKey));

        return compKey;
    }

    /**
     * @return Component key.
     */
    public ComponentKey registerComponentImplementation(ComponentKey compKey, Class compClass,
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
    public ComponentKey registerComponentFactory(ComponentKey compKey, Object compFac,
        String facMethodName, Object[] facMethodArgs, ComponentCallback[] callbacks)
    {
        Component comp = getComponent(compKey);

        comp.registerFactory(compFac, facMethodName, facMethodArgs, callbacks);

        return compKey;
    }

    // Constructor arguments registration ===============================================

    public ComponentKey registerComponentConstructorArguments(ComponentKey compKey, Object[] args)
    {
        Component comp = getComponent(compKey);

        comp.registerConstructorArguments(args);

        return compKey;
    }

    // LifeCycle methods registration ===============================================

    public ComponentKey registerComponentCallbacks(ComponentKey compKey,
        ComponentCallback[] callbacks)
    {
        Component comp = getComponent(compKey);

        comp.registerCallbacks(callbacks);

        return compKey;
    }

    // Location registration methods ===============================================================

    public ComponentKey registerComponentRegistrationLocation(ComponentKey compKey,
        Location location)
    {
        Component comp = getComponent(compKey);

        comp.setRegistrationLocation(location);

        return compKey;
    }

    public ComponentKey registerComponentRetrievalLocation(ComponentKey compKey, Location location)
    {
        Component comp = getComponent(compKey);

        comp.setRetrievalLocation(location);

        return compKey;
    }

    // Component factory decorator methods =========================================================

    /**
     * @return Component key.
     */
    public ComponentKey wrapComponentStrategy(ComponentKey compKey,
        ComponentStrategyType strategyType)
    {
        Component comp = getComponent(compKey);

        comp.wrapStrategy(strategyType);

        return compKey;
    }

    // Configuration methods =======================================================================

    public Object registerConfigurationItem(Object configKey, Object configItem)
    {
        List configList = getConfigurationList(configKey);

        configItem = realizeKey(configItem);

        configList.add(configItem);

        return configKey;
    }

    public Object registerConfigurationItem(Object configKey, Object configItemKey,
        Object configItem)
    {
        Map configMap = getConfigurationMap(configKey);

        if (configItem instanceof ComponentKey)
            configItem = getComponentInstance((ComponentKey) configItem);

        configMap.put(configItemKey, configItem);

        return configKey;
    }

    public Object registerConfiguration(Object configKey, Object config)
    {
        config = realizeKey(config);

        _configurationCache.put(configKey, config);

        return configKey;
    }

    public void realizeKeys(Object[] args)
    {
        if (args == null)
            return;

        for (int i = 0; i < args.length; i++)
            args[i] = realizeKey(args[i]);
    }

    private Object realizeKey(Object item)
    {
        return item instanceof ComponentKey ? getComponentInstance((ComponentKey) item) : item;
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
    public Object getComponentInstance(ComponentKey compKey)
    {
        Component comp = getComponent(compKey);

        return comp.getInstance();
    }

    public void collectComponentInstance(ComponentKey compKey, Object compInst)
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
        List configList = (List) _configurationCache.get(configKey);

        if (configList == null)
        {
            configList = (List) _context.newApiInstance(List.class);

            _configurationCache.put(configKey, configList);
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
        Map configMap = (Map) _configurationCache.get(configKey);

        if (configMap == null)
        {
            configMap = (Map) _context.newApiInstance(Map.class);

            _configurationCache.put(configKey, configMap);
        }

        return configMap;
    }

    /**
     * Gets the configuration registered for the supplied key.
     * 
     * @return The configuration object.
     * @throws UsageException
     *             When no configuration is registered for the provided key.
     */
    public Object getConfiguration(Object configKey)
    {
        Object config = _configurationCache.get(configKey);

        if (config == null)
            throw _context.getExceptionWrapper().wrap(new UsageException(),
                Message.CONFIGURATION_NOT_REGISTERED, configKey);

        return config;
    }

    public void handlePreThreadTermination()
    {
        _context.getThreadEvent().notifyPreTerminationObservers();
    }

    /**
     * Wipes out the components and configurations registered with the container.
     */
    public void cleanup()
    {
        _componentCache.clear();
        _configurationCache.clear();
    }

    // ComponentKey generator ======================================================================

    /**
     * This is the key that will uniquely identify a component within the container. This key is
     * composed of the component interface and optionally a component type (a string identifier).
     */
    private static class ComponentKeyGenerator
    {
        private static final String DEFAULT_COMPONENT_TYPE = "default";

        private static final Map    KEYS                   = new HashMap();

        private Context             _context;

        ComponentKeyGenerator(Context context)
        {
            _context = context;
        }

        ComponentKey get(Class compIntf, Object compType)
        {
            if (compType == null)
                compType = DEFAULT_COMPONENT_TYPE;

            String keyKey = compIntf + compType.toString();

            ComponentKey compKey = (ComponentKey) KEYS.get(keyKey);

            if (compKey == null)
            {
                compKey = new DefaultComponentKey(compIntf, compType, _context);

                synchronized (KEYS)
                {
                    KEYS.put(keyKey, compKey);
                }
            }

            return compKey;
        }

        private class DefaultComponentKey implements ComponentKey
        {
            private Class        _componentInterface;
            private Object       _componentType;
            private volatile int _hashCode;

            private DefaultComponentKey(Class compIntf, Object compType, Context context)
            {
                if (compIntf == null)
                    throw context.getExceptionWrapper().wrap(new IllegalArgumentException(),
                        Message.COMPONENT_INTERFACE_MUST_NOT_BE_NULL);

                _componentInterface = compIntf;
                _componentType = compType;
            }

            public Class getComponentInterface()
            {
                return _componentInterface;
            }

            public String toString()
            {
                return "[Component Interface: " + _componentInterface + ", Component Type: "
                    + _componentType + "]";
            }

            public boolean equals(Object obj)
            {
                if (obj == this)
                    return true;

                if (!(obj instanceof DefaultComponentKey))
                    return false;

                DefaultComponentKey key = (DefaultComponentKey) obj;

                boolean compIntfIsEqual = _componentInterface == null
                    ? key._componentInterface == null
                    : _componentInterface.equals(key._componentInterface);

                boolean compTypeIsEqual = _componentType == null ? key._componentType == null
                    : _componentType.equals(key._componentType);

                if (compIntfIsEqual && compTypeIsEqual)
                    return true;

                return false;
            }

            public int hashCode()
            {
                if (_hashCode == 0)
                {
                    int result = 17;

                    result = 37 * result
                        + (_componentInterface == null ? 0 : _componentInterface.hashCode());

                    result = 37 * result + (_componentType == null ? 0 : _componentType.hashCode());

                    _hashCode = result;
                }

                return _hashCode;
            }
        }
    }
}