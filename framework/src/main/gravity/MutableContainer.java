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

/**
 * @author Harish Krishnaswamy
 * @version $Id: MutableContainer.java,v 1.4 2004-05-27 03:36:32 harishkswamy Exp $
 */
public interface MutableContainer extends Container
{
    Object registerComponentImplementation(Class compIntf, Object compType, Class compClass,
        Object[] ctorArgs, ComponentCallback[] lifeCycleMethods);

    Object registerComponentImplementation(Class compIntf, Class compClass, Object[] ctorArgs,
        ComponentCallback[] lifeCycleMethods);

    Object registerComponentImplementation(Class compClass, Object compType, Object[] ctorArgs,
        ComponentCallback[] lifeCycleMethods);

    Object registerComponentImplementation(Class compClass, Object[] ctorArgs,
        ComponentCallback[] lifeCycleMethods);

    // Constructor arguments registration ==========================================================

    Object registerComponentConstructorArguments(Object compKey, Object[] args);

    Object registerComponentConstructorArguments(Class compIntf, Object compType, Object[] args);

    Object registerComponentConstructorArguments(Class compIntf, Object[] args);

    // Lifecycle methods registration ==============================================================

    Object registerComponentCallbacks(Object compKey, ComponentCallback[] methods);

    Object registerComponentCallbacks(Class compIntf, Object compType, ComponentCallback[] methods);

    Object registerComponentCallbacks(Class compIntf, ComponentCallback[] methods);

    // Location registration methods ===============================================================

    Object registerComponentRegistrationLocation(Object compKey, Location location);

    Object registerComponentRegistrationLocation(Class compIntf, Object compType, Location location);

    Object registerComponentRegistrationLocation(Class compIntf, Location location);

    Object registerComponentRetrievalLocation(Object compKey, Location location);

    Object registerComponentRetrievalLocation(Class compIntf, Object compType, Location location);

    Object registerComponentRetrievalLocation(Class compIntf, Location location);

    // Component strategy decorator methods ========================================================

    /**
     * This method will add a singleton strategy to the current component strategy.
     * 
     * @param compKey
     *        Component Key.
     * @return Component Key.
     */
    Object wrapComponentStrategyWithSingleton(Object compKey);

    /**
     * This method will add a pooling strategy to the current component strategy.
     * 
     * @param compKey
     *        Component Key.
     * @return Component Key.
     */
    Object wrapComponentStrategyWithPooling(Object compKey);

    /**
     * This method will add a thread local strategy to the current component strategy.
     * 
     * @param compKey
     *        Component Key.
     * @return Component Key.
     */
    Object wrapComponentStrategyWithThreadLocal(Object compKey);

    // Configuration methods =======================================================================

    Object registerConfiguration(Object configKey, Object configItem);

    Object registerConfiguration(Object configKey, Object configItemKey, Object configItem);
}