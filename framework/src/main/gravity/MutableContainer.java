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

import java.util.Map;

/**
 * @author Harish Krishnaswamy
 * @version $Id: MutableContainer.java,v 1.2 2004-05-22 20:19:31 harishkswamy Exp $
 */
public interface MutableContainer extends Container
{
    Object registerComponentImplementation(Class intf, Object implType, Class implClass,
        Object[] ctorArgs, Map methodArgs);

    Object registerComponentImplementation(Class compIntf, Object compType, Class compClass,
        Object[] ctorArgs);

    Object registerComponentImplementation(Class compIntf, Object compType, Class compClass,
        Map methodArgs);

    // Default service registration convenience methods ============================================

    Object registerComponentImplementation(Class compIntf, Class compClass, Object[] ctorArgs,
        Map methodArgs);

    Object registerComponentImplementation(Class compIntf, Class compClass, Object[] ctorArgs);

    Object registerComponentImplementation(Class compIntf, Class compClass, Map methodArgs);

    // Variant component registration convenience methods ==========================================

    Object registerComponentImplementation(Class compClass, Object compType, Object[] ctorArgs,
        Map methodArgs);

    Object registerComponentImplementation(Class compClass, Object compType, Object[] ctorArgs);

    Object registerComponentImplementation(Class compClass, Object compType, Map methodArgs);

    // Default component registration convenience methods ==========================================

    Object registerComponentImplementation(Class compClass, Object[] ctorArgs, Map methodArgs);

    Object registerComponentImplementation(Class compClass, Object[] ctorArgs);

    Object registerComponentImplementation(Class compClass, Map methodArgs);

    // Location registration methods ===============================================================

    Object registerComponentRegistrationLocation(Object compKey, Location location);

    Object registerComponentRegistrationLocation(Class compIntf, Object compType, Location location);

    Object registerComponentRegistrationLocation(Class compIntf, Location location);

    Object registerComponentRetrievalLocation(Object compKey, Location location);

    Object registerComponentRetrievalLocation(Class compIntf, Object compType, Location location);

    Object registerComponentRetrievalLocation(Class compIntf, Location location);

    // Component state decorator methods ===========================================================

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