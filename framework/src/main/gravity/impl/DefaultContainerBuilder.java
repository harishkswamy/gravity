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

import gravity.Container;
import gravity.Gravity;
import gravity.ContainerBuilder;
import gravity.MutableContainer;
import gravity.Plugin;
import gravity.plugins.bsh.BshPlugin;
import gravity.util.ClassUtils;
import gravity.util.Utils;

import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

/**
 * This is helper class to {@link gravity.Gravity}that builds the {@link gravity.Container}from
 * the provided Gravity properties.
 * <p>
 * This is isolated from Gravity so custom implementations can be provided that can override the
 * {@link gravity.MutableContainer default-container}and the default behavior of retrieving the
 * plugin manifest files.
 * 
 * @author Harish Krishnaswamy
 * @version $Id: DefaultContainerBuilder.java,v 1.1 2004-09-02 04:00:42 harishkswamy Exp $
 */
public class DefaultContainerBuilder implements ContainerBuilder
{
    private Properties       _properties;
    private MutableContainer _container;

    public DefaultContainerBuilder()
    {
        _container = newMutableContainer();
    }

    /**
     * This can be overridden to provide a custom {@link MutableContainer}implementation.
     * 
     * @return Returns an instance of {@link DefaultContainer}.
     */
    protected MutableContainer newMutableContainer()
    {
        return new DefaultContainer();
    }

    /**
     * This method can be overridden to retrieve plugn manifest files from other sources.
     * 
     * @return Returns an enumeration of plugin manifest files found in the classpath.
     * @see Gravity#PLUGIN_MANIFEST_FILE_PATH
     */
    protected Enumeration getPluginManifestFiles()
    {
        return ClassUtils.getResources(Gravity.PLUGIN_MANIFEST_FILE_PATH);
    }

    /**
     * @return Returns the location of the plugin manifest file from the provided URL.
     */
    protected String getPluginLocation(URL url)
    {
        String urlStr = url.toString();

        return urlStr.substring(0, urlStr.lastIndexOf(Gravity.PLUGIN_MANIFEST_FILE_PATH));
    }

    private Plugin getPlugin(String pluginClassName)
    {
        // Return the plugin from the plugin manifest file
        if (pluginClassName != null)
            return (Plugin) ClassUtils.newInstance(pluginClassName);

        // Return the plugin (global) from the gravity.properties file
        pluginClassName = _properties.getProperty(Gravity.PLUGIN_CLASS_NAME_KEY);

        if (pluginClassName != null)
            return (Plugin) ClassUtils.newInstance(pluginClassName);

        // Return the default plugin
        return new BshPlugin();
    }

    private void acceptPlugins()
    {
        Enumeration e = getPluginManifestFiles();

        while (e.hasMoreElements())
        {
            URL url = (URL) e.nextElement();

            Properties props = Utils.loadProperties(url);

            props.setProperty(Plugin.LOCATION_URL_KEY, getPluginLocation(url));

            Plugin plugin = getPlugin(props.getProperty(Gravity.PLUGIN_CLASS_NAME_KEY));

            plugin.startup(props, _container);
        }
    }

    /*
     * See ContainerBuilder#build(Properties)
     */
    public Container build(Properties props)
    {
        _properties = props;

        acceptPlugins();

        return _container;
    }
}