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
 * @version $Id: MutableContainer.java,v 1.5 2004-05-29 16:43:33 harishkswamy Exp $
 */
public interface MutableContainer extends Container
{
    Object registerComponentImplementation(Object compKey, Class compClass, Object[] ctorArgs,
        ComponentCallback[] callbacks);

    // Constructor arguments registration ==========================================================

    Object registerComponentConstructorArguments(Object compKey, Object[] args);

    // Lifecycle methods registration ==============================================================

    Object registerComponentCallbacks(Object compKey, ComponentCallback[] methods);

    // Location registration methods ===============================================================

    Object registerComponentRegistrationLocation(Object compKey, Location location);

    Object registerComponentRetrievalLocation(Object compKey, Location location);

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