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
import gravity.MutableContext;
import gravity.util.ClassUtils;
import gravity.util.ReflectUtils;
import gravity.util.Utils;

import java.net.URL;
import java.util.Properties;

/**
 * This class is the gateway to the framework. It provides helper methods to initialize the
 * framework and obtain a fully constructed container.
 * <p>
 * This class stores the framework properties that can be accessed and/or modified anytime.
 * <p>
 * NOTE: This class is expected to be mutated only during startup which is expected to be done in a
 * single thread, and so this class is NOT thread safe. Any concurrent mutations should be
 * synchronized externally.
 * 
 * @author Harish Krishnaswamy
 * @version $Id: DefaultContext.java,v 1.1 2004-11-17 19:50:39 harishkswamy Exp $
 */
public final class DefaultContext implements MutableContext
{
    /**
     * This is name of the system property that specifies the name and location of the gravity
     * properties file.
     */
    private static final String PROPERTIES_PATH_KEY     = "gravity.properties";

    /**
     * This is the name of the default properties file that will be used when not supplied. This
     * file should be placed in the classpath root to be recognized.
     */
    private static final String DEFAULT_PROPERTIES_PATH = "gravity.properties";

    /**
     * This is the name of the file that specifies the defaults for the framework.
     */
    private static final String DEFAULTS_FILE_NAME      = "gravity/gravity-defaults.properties";

    private Properties          _properties;
    private Container           _container;

    private static Properties findAndLoad(String fPath)
    {
        URL url = ClassUtils.getResource(fPath);

        return Utils.loadProperties(url);
    }

    private static Properties loadProperties()
    {
        // Load framework defaults
        Properties props = findAndLoad(DEFAULTS_FILE_NAME);

        String fPath = System.getProperty(PROPERTIES_PATH_KEY);

        if (fPath != null)
        {
            // Load user defaults from a user specified properties file
            props.putAll(findAndLoad(fPath));
        }
        else
        {
            try
            {
                // Load user defaults from the default properties file
                props.putAll(findAndLoad(DEFAULT_PROPERTIES_PATH));
            }
            catch (Exception e)
            {
                // Ignore if unable to load from default properties file
            }
        }

        return props;
    }

    /**
     * Initializes the framework by loading the framework properties from the default properties
     * file - gravity.properties. The default properties file is expected to be in the classpath
     * root.
     */
    public DefaultContext()
    {
        _properties = loadProperties();
    }

    public DefaultContext(Properties props)
    {
        this();

        _properties.putAll(props);
    }

    public DefaultContext(URL url)
    {
        this();

        _properties.putAll(Utils.loadProperties(url));
    }

    public DefaultContext(String fPath)
    {
        this();

        _properties.putAll(findAndLoad(fPath));
    }

    /**
     * Instantiates the implementation class registered in the Gravity properties for the provided
     * key.
     * 
     * @param implName
     *            The implementation's key.
     * @param args
     *            The arguments that will be used to reflectively invoke the implementation's
     *            constructor.
     */
    public Object newApiInstance(String implName, Object[] args)
    {
        String className = _properties.getProperty(implName);

        Class compClass = ClassUtils.loadClass(className);

        return ReflectUtils.invokeConstructor(compClass, args);
    }

    public Container getContainer()
    {
        return _container;
    }

    public void setContainer(Container container)
    {
        _container = container;
    }

    public Properties getProperties()
    {
        return new Properties(_properties);
    }
}