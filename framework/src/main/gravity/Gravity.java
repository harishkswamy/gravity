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

package gravity;

import gravity.impl.DefaultRegistry;
import gravity.plugins.BshPlugin;
import gravity.util.ClassUtils;
import gravity.util.Utils;

import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

/**
 * This class is the gateway to the framework. It provides static helper methods to initialize the
 * framework and obtain a fully constructed registry.
 * <p>
 * This class stores the framework properties that can be accessed and/or modified.
 * <p>
 * This class is typically expected to be used in a single thread and only during startup and
 * shutdown.
 * 
 * @author Harish Krishnaswamy
 * @version $Id: Gravity.java,v 1.1 2004-05-10 17:29:04 harishkswamy Exp $
 */
public class Gravity
{
    /**
     * This is the path of the plugin file that Gravity will use to search for plugins. The path is
     * relative to the classpath.
     */
    public static final String     PLUGIN_MANIFEST_FILE_PATH          = "META-INF/gravity-plugin.properties";

    public static final String     PLUGIN_CLASS_NAME                  = "pluginClassName";

    /**
     * This is name of the property that specifies the class name of the custom
     * {@link ComponentProxyFactory}.
     */
    public static final String     COMPONENT_PROXY_FACTORY_CLASS_NAME = "componentProxyFactoryClassName";

    /**
     * This is name of the property that specifies the class name of the custom
     * {@link DynamicWeaver}.
     */
    public static final String     DYNAMIC_WEAVER_CLASS_NAME          = "dynamicWeaverClassName";

    /**
     * This is name of the system property that specifies the name and location of the gravity
     * properties file.
     */
    private static final String    PROPERTIES_PATH_KEY                = "gravity.properties";

    /**
     * This is the name of the default properties file that will be used when not supplied. This
     * file should be placed in the classpath root to be recognized.
     */
    private static final String    DEFAULT_PROPERTIES_PATH            = "gravity.properties";

    /**
     * Stores the framework properties.
     */
    private static Properties      _props;

    // TODO cleanup registry after startup
    private static MutableRegistry _registry;

    private Gravity()
    {
        // Static class.
    }

    /**
     * Initializes the framework by loading the framework properties from the supplied URL.
     */
    public static synchronized void initialize(URL url)
    {
        if (_props != null)
            return;

        _props = Utils.loadProperties(url);

        // Load and initialize the registry
        // TODO decouple registry implementation
        _registry = new DefaultRegistry();
    }

    private static URL getPropertiesUrl(String fPath)
    {
        return ClassUtils.getResource(fPath);
    }

    /**
     * Initializes the framework by loading the framework properties from the supplied file path.
     * The file path is relative to the classpath root.
     */
    public static void initialize(String fPath)
    {
        URL url = getPropertiesUrl(fPath);

        initialize(url);
    }

    private static String getPropertiesUrlString()
    {
        String fPath = System.getProperty(PROPERTIES_PATH_KEY);

        return fPath == null ? DEFAULT_PROPERTIES_PATH : fPath;
    }

    /**
     * Initializes the framework by loading the framework properties from the default properties
     * file - gravity.properties. The default properties file is expected to be in the classpath.
     */
    public static void initialize()
    {
        String fPath = getPropertiesUrlString();

        initialize(fPath);
    }

    public static Enumeration getPluginManifestFiles()
    {
        return ClassUtils.getResources(PLUGIN_MANIFEST_FILE_PATH);
    }

    private static Plugin getPlugin(String pluginClassName)
    {
        // Return the plugin from the plugin manifest file
        if (pluginClassName != null)
            return (Plugin) ClassUtils.newInstance(pluginClassName);

        // Return the plugin (global) from the gravity.properties file
        pluginClassName = _props.getProperty(PLUGIN_CLASS_NAME);

        if (pluginClassName != null)
            return (Plugin) ClassUtils.newInstance(pluginClassName);

        // Return the default plugin
        return new BshPlugin();
    }

    private static void acceptPlugins()
    {
        Enumeration e = getPluginManifestFiles();

        while (e.hasMoreElements())
        {
            URL url = (URL) e.nextElement();

            Properties props = Utils.loadProperties(url);

            Plugin plugin = getPlugin(props.getProperty(PLUGIN_CLASS_NAME));

            plugin.startup(props, _registry);
        }
    }

    /**
     * Initializes the framework from the supplied URL and builds the {@link Registry}.
     * 
     * @return Newly built {@link Registry}.
     * @see Gravity#initialize(URL)
     */
    public static Registry startup(URL url)
    {
        initialize(url);

        acceptPlugins();

        return _registry;
    }

    /**
     * Initializes the framework from the supplied properties file path and builds the
     * {@link Registry}. The file path is relative to the classpath root.
     * 
     * @return Newly built {@link Registry}.
     * @see Gravity#initialize(String)
     */
    public static Registry startup(String fPath)
    {
        URL url = getPropertiesUrl(fPath);

        return startup(url);
    }

    /**
     * Initializes the framework from the default properties file and builds the {@link Registry}.
     * The file path is relative to the classpath root.
     * 
     * @return Newly built {@link Registry}.
     * @see Gravity#initialize()
     */
    public static Registry startup()
    {
        String fPath = getPropertiesUrlString();

        return startup(fPath);
    }

    /**
     * Gets the gravity property value for the supplied key.
     */
    public static String getProperty(String key)
    {
        if (_props == null)
            throw new IllegalArgumentException(
                "Gravity should be initialized prior to accessing its properties.");

        return _props.getProperty(key);
    }

    /**
     * Sets the gravity property with the supplied key/value pair.
     * <p>
     * Properties are only expected to be set during startup prior to building the {@link Registry}
     * which should typically happen in a single startup thread.
     */
    public static synchronized void setProperty(String key, String value)
    {
        if (_props == null)
            throw new IllegalArgumentException(
                "Gravity should be initialized prior to accessing its properties.");

        _props.setProperty(key, value);
    }

    /**
     * Wipes out the gravity properties.
     */
    public static void shutdown()
    {
        _props = null;
        _registry = null;
    }
}