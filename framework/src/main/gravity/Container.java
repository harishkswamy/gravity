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

import java.util.List;
import java.util.Map;

/**
 * This is the container that houses all components and configurations.
 * <p>
 * This is a singleton and should typically be built in a single startup thread.
 * 
 * @author Harish Krishnaswamy
 * @version $Id: Container.java,v 1.3 2004-05-29 16:43:34 harishkswamy Exp $
 */
public interface Container
{
    Object getComponentKey(Class compIntf, Object compType);

    Object getComponentKey(Class compIntf);

    /**
     * Gets the component registered for the supplied component key (component interface + component
     * implementation type).
     * 
     * @return The component registered for the supplied key.
     * @throws UsageException
     *         When no component is registered for the supplied key.
     */
    Object getComponentInstance(Object compKey);

    /**
     * This method should be used to return a component instance obtained via one of the
     * {@link Container#getComponentInstance(Object)}methods. This method is primarily used for
     * returning pooled components.
     */
    void collectComponentInstance(Object compKey, Object comp);

    /**
     * Gets the configuration registered for the supplied key.
     * 
     * @return The configuration list.
     * @throws IllegalArgumentException
     *         When no configuration is registered for the supplied key.
     */
    List getConfigurationList(Object configKey);

    /**
     * Gets the configuration registered for the supplied key.
     * 
     * @return The configuration map.
     * @throws IllegalArgumentException
     *         When no configuration is registered for the supplied key.
     */
    Map getConfigurationMap(Object configKey);

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