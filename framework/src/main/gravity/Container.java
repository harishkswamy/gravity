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

package gravity;

import java.util.List;
import java.util.Map;

/**
 * The container houses all components and their configuration data and serve the housed components
 * and configuration data when requested.
 * <p>
 * The container also manages the life cycle of the housed components.
 * 
 * @author Harish Krishnaswamy
 * @version $Id: Container.java,v 1.7 2005-10-06 21:59:19 harishkswamy Exp $
 */
public interface Container
{
    /**
     * This property specifies a comma-separated list of plugin URLs that needs to be loaded prior
     * to auto-loading.
     */
    String PLUGINS_MANIFEST_URL_KEY       = "gravity.plugins.manifest.url";

    /**
     * This property specifies a comma-separated list of plugin classpaths that needs to be loaded
     * prior to auto-loading.
     */
    String PLUGINS_MANIFEST_CLASSPATH_KEY = "gravity.plugins.manifest.classpath";

    /**
     * This property specifies whether to automatically discover plugins and load them.
     */
    String PLUGINS_AUTOLOAD_KEY           = "gravity.plugins.autoload";

    /**
     * Generates and returns a key from the given interface and type.
     * 
     * @param compIntf
     *            The interface class of the component.
     * @param compType
     *            The type that identifies the implementation when multiple implementations exist.
     * @return The component key.
     */
    ComponentKey getComponentKey(Class compIntf, Object compType);

    /**
     * Generates and returns a key from the given interface.
     */
    ComponentKey getComponentKey(Class compIntf);

    /**
     * Gets the component registered for the supplied component key (component interface + component
     * implementation type).
     * 
     * @param compKey
     *            The key that uniquely identifies the component within this container.
     * @return The component registered for the supplied key.
     */
    Object getComponentInstance(ComponentKey compKey);

    /**
     * This method should be used to return a component instance obtained via one of the
     * {@link Container#getComponentInstance(Object)}methods. This method is primarily used for
     * returning pooled components.
     * 
     * @param compKey
     *            The key that uniquely identifies the component within this container.
     * @param comp
     *            The component instance that is being returned to the container.
     */
    void collectComponentInstance(ComponentKey compKey, Object comp);

    /**
     * Gets the configuration registered for the supplied key.
     * 
     * @return The configuration list.
     */
    List getConfigurationList(Object configKey);

    /**
     * Gets the configuration registered for the supplied key.
     * 
     * @return The configuration map.
     */
    Map getConfigurationMap(Object configKey);

    /**
     * Gets the configuration registered for the supplied key.
     * 
     * @return The configuration object.
     */
    Object getConfiguration(Object configKey);

    /**
     * This method should be invoked prior to the termination of the executing thread. This method
     * will cleanup all ThreadLocal variables and release any thread specific resources.
     */
    void handlePreThreadTermination();

    /**
     * Wipes out the components and configurations registered with the container.
     */
    void cleanup();
}