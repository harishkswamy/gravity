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

import gravity.impl.DefaultContainerBuilder;
import gravity.util.ClassUtils;
import gravity.util.Message;
import gravity.util.Utils;

import java.net.URL;
import java.util.Properties;

/**
 * This class is the gateway to the framework. It provides helper methods to initialize the
 * framework and obtain a fully constructed container.
 * <p>
 * This class stores the framework properties that can be accessed and/or modified anytime.
 * 
 * @author Harish Krishnaswamy
 * @version $Id: Gravity.java,v 1.12 2004-09-02 03:50:43 harishkswamy Exp $
 */
public final class Gravity
{
    /**
     * This is the path of the plugin file that Gravity will use to search for plugins. The path is
     * relative to the classpath.
     */
    public static final String   PLUGIN_MANIFEST_FILE_PATH      = "META-INF/gravity-plugin.properties";

    /**
     * This is name of the property that specifies the class name of the custom {@link Plugin}that
     * is to be used for all plugins across the board. In addition, each individual plugin can
     * specify its own plugin class which would take precendence over this specification.
     */
    public static final String   PLUGIN_CLASS_NAME_KEY          = "pluginClassName";

    /**
     * This is name of the property that specifies the class name of the custom
     * {@link ComponentProxy}.
     */
    public static final String   COMPONENT_PROXY_CLASS_NAME_KEY = "componentProxyClassName";

    /**
     * This is name of the property that specifies the class name of the custom
     * {@link DynamicWeaver}.
     */
    public static final String   DYNAMIC_WEAVER_CLASS_NAME_KEY  = "dynamicWeaverClassName";

    /**
     * This is the name of the property that specifies the class name of a custom
     * {@link ContainerBuilder}.
     */
    public static final String   GRAVITY_BUILDER_CLASS_NAME_KEY = "gravityBuilderClassName";

    /**
     * This is name of the system property that specifies the name and location of the gravity
     * properties file.
     */
    private static final String  PROPERTIES_PATH_KEY            = "gravity.properties";

    /**
     * This is the name of the default properties file that will be used when not supplied. This
     * file should be placed in the classpath root to be recognized.
     */
    private static final String  DEFAULT_PROPERTIES_PATH        = "gravity.properties";

    /**
     * Singleton instance.
     */
    private static final Gravity INSTANCE                       = new Gravity();

    public static Gravity getInstance()
    {
        return INSTANCE;
    }

    // Instance code ===============================================================================

    /**
     * Stores the framework properties.
     */
    private Properties _properties;

    private Container  _container;

    private Gravity()
    {
        // Singleton.
    }

    /**
     * Initializes the framework and creates the container.
     * 
     * @throws UsageException
     *             When the framework is already initialized.
     */
    public synchronized void initialize(Properties props)
    {
        if (_properties != null)
            throw new UsageException(Message.GRAVITY_ALREADY_INITIALIZED);

        _properties = props;
    }

    /**
     * Initializes the framework by loading the framework properties from the supplied URL.
     */
    public void initialize(URL url)
    {
        initialize(Utils.loadProperties(url));
    }

    private URL getPropertiesUrl(String fPath)
    {
        return ClassUtils.getResource(fPath);
    }

    /**
     * Initializes the framework by loading the framework properties from the supplied file path.
     * The file path is relative to the classpath root.
     */
    public void initialize(String fPath)
    {
        URL url = getPropertiesUrl(fPath);

        initialize(url);
    }

    private String getPropertiesUrlString()
    {
        return System.getProperty(PROPERTIES_PATH_KEY, DEFAULT_PROPERTIES_PATH);
    }

    /**
     * Initializes the framework by loading the framework properties from the default properties
     * file - gravity.properties. The default properties file is expected to be in the classpath
     * root.
     */
    public void initialize()
    {
        String fPath = getPropertiesUrlString();

        initialize(fPath);
    }

    private ContainerBuilder newGravityBuilder()
    {
        String builderName = _properties.getProperty(GRAVITY_BUILDER_CLASS_NAME_KEY);

        if (builderName != null)
            return (ContainerBuilder) ClassUtils.newInstance(builderName);

        return new DefaultContainerBuilder();
    }

    /**
     * Initializes the framework from the supplied Properties and builds the {@link Container}.
     * 
     * @return Newly built {@link Container}.
     * @see Gravity#initialize(Properties)
     */
    public Container startup(Properties props)
    {
        initialize(props);

        _container = newGravityBuilder().build(props);

        return _container;
    }

    /**
     * Initializes the framework from the supplied URL and builds the {@link Container}.
     * 
     * @return Newly built {@link Container}.
     * @see Gravity#initialize(URL)
     */
    public Container startup(URL url)
    {
        return startup(Utils.loadProperties(url));
    }

    /**
     * Initializes the framework from the supplied properties file path and builds the
     * {@link Container}. The file path is relative to the classpath root.
     * 
     * @return Newly built {@link Container}.
     * @see Gravity#initialize(String)
     */
    public Container startup(String fPath)
    {
        URL url = getPropertiesUrl(fPath);

        return startup(url);
    }

    /**
     * Initializes the framework from the default properties file and builds the {@link Container}.
     * The file path is relative to the classpath root.
     * 
     * @return Newly built {@link Container}.
     * @see Gravity#initialize()
     */
    public Container startup()
    {
        String fPath = getPropertiesUrlString();

        return startup(fPath);
    }

    /**
     * This returns the container managed by this Gravity instance.
     */
    public Container getContainer()
    {
        return _container;
    }

    /**
     * Gets the gravity property value for the supplied key.
     * 
     * @throws UsageException
     *             When the framework is not initialized.
     */
    public String getProperty(String key)
    {
        if (_properties == null)
            throw new UsageException(Message.GRAVITY_NOT_INITIALIZED);

        return _properties.getProperty(key);
    }

    /**
     * Sets the gravity property with the supplied key/value pair.
     * <p>
     * Properties are only expected to be set during startup prior to building the {@link Container}
     * which should typically happen in a single startup thread.
     * 
     * @throws UsageException
     *             When the framework is not initialized.
     */
    public synchronized void setProperty(String key, String value)
    {
        if (_properties == null)
            throw new UsageException(Message.GRAVITY_NOT_INITIALIZED);

        _properties.setProperty(key, value);
    }

    // TODO shutdown all components
    /**
     * Wipes out the gravity properties.
     */
    public void shutdown()
    {
        _properties = null;
        _container = null;
    }
}