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
 * This is the container that houses all services and configurations.
 * <p>
 * This is a singleton and should typically be built in a single startup thread.
 * 
 * @author Harish Krishnaswamy
 * @version $Id: Registry.java,v 1.1 2004-05-10 17:29:05 harishkswamy Exp $
 */
public interface Registry
{
    public Object getComponentInstance(Object compKey);

    /**
     * Gets the service registered for the supplied service key (service interface + service
     * implementation type).
     * 
     * @return The service registered for the supplied key.
     * @throws UsageException
     *         When no service is registered for the supplied key.
     */
    Object getComponentInstance(Class compClass, Object compType);

    /**
     * Gets the default service registered for the supplied service interface.
     * 
     * @return The service registered for the supplied key.
     * @throws IllegalArgumentException
     *         When no service is registered for the supplied key.
     */
    Object getComponentInstance(Class compClass);

    /**
     * This method should be used to return a component instance obtained via one of the
     * getComponentInstance(...) methods. This method is primarily used for returning pooled
     * components.
     */
    void returnComponentInstance(Class compIntf, Object compType, Object comp);

    /**
     * This method should be used to return a component instance obtained via one of the
     * getComponentInstance(...) methods. This method is primarily used for returning pooled
     * components.
     */
    void returnComponentInstance(Class compIntf, Object comp);

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
     * Wipes out the services and configurations registered in the registry.
     */
    void cleanup();
}