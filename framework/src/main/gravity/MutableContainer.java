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
 * @version $Id: MutableContainer.java,v 1.1 2004-05-18 20:52:01 harishkswamy Exp $
 */
public interface MutableContainer extends Container
{
    Object registerComponentImplementation(Class intf, Object implType, Class implClass,
        Object[] ctorArgs, Map setrArgs);

    Object registerComponentImplementation(Class compIntf, Object compType, Class compClass,
        Object[] ctorArgs);

    Object registerComponentImplementation(Class compIntf, Object compType, Class compClass,
        Map setrArgs);

    // Default service registration convenience methods ============================================

    Object registerComponentImplementation(Class compIntf, Class compClass, Object[] ctorArgs,
        Map setrArgs);

    Object registerComponentImplementation(Class compIntf, Class compClass, Object[] ctorArgs);

    Object registerComponentImplementation(Class compIntf, Class compClass, Map setrArgs);

    // Variant component registration convenience methods ==========================================

    Object registerComponentImplementation(Class compClass, Object compType, Object[] ctorArgs,
        Map setrArgs);

    Object registerComponentImplementation(Class compClass, Object compType, Object[] ctorArgs);

    Object registerComponentImplementation(Class compClass, Object compType, Map setrArgs);

    // Default component registration convenience methods ==========================================

    Object registerComponentImplementation(Class compClass, Object[] ctorArgs, Map setrArgs);

    Object registerComponentImplementation(Class compClass, Object[] ctorArgs);

    Object registerComponentImplementation(Class compClass, Map setrArgs);

    // Location registration methods ===============================================================

    Object registerComponentRegistrationLocation(Object compKey, Location location);

    Object registerComponentRegistrationLocation(Class compIntf, Object compType, Location location);

    Object registerComponentRegistrationLocation(Class compIntf, Location location);

    Object registerComponentRetrievalLocation(Object compKey, Location location);

    Object registerComponentRetrievalLocation(Class compIntf, Object compType, Location location);

    Object registerComponentRetrievalLocation(Class compIntf, Location location);

    // Component state decorator methods ===========================================================

    /**
     * This method will add a singleton state to the current component state. This method may be
     * invoked anytime prior to the first method invocation on the component, to take effect.
     * 
     * @param compKey
     *        Component Key.
     * @return Component Key.
     */
    Object wrapComponentStateWithSingleton(Object compKey);

    /**
     * This method will add a pooling state to the current component state. This method may be
     * invoked anytime prior to the first method invocation on the component, to take effect.
     * 
     * @param compKey
     *        Component Key.
     * @return Component Key.
     */
    Object wrapComponentStateWithPooling(Object compKey);

    /**
     * This method will add a thread local state to the current component state. This method may be
     * invoked anytime prior to the first method invocation on the component, to take effect.
     * 
     * @param compKey
     *        Component Key.
     * @return Component Key.
     */
    Object wrapComponentStateWithThreadLocal(Object compKey);

    // Configuration methods =======================================================================

    Object registerConfiguration(Object configKey, Object configItem);

    Object registerConfiguration(Object configKey, Object configItemKey, Object configItem);
}