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
 * This is a mutable container that allows new {@link gravity.Component}s, configuration data and
 * {@link gravity.ComponentStrategy}s to be registered with it.
 * 
 * @author Harish Krishnaswamy
 * @version $Id: MutableContainer.java,v 1.7 2004-09-02 04:04:48 harishkswamy Exp $
 */
public interface MutableContainer extends Container
{
    /**
     * This is the primary implementation registration method. If the implementation does not need
     * constructor arguments or callbacks this method will allow nulls for them.
     * 
     * @param compKey
     *            The key that identifies the component within the container.
     * @param compClass
     *            The implementation class for this component.
     * @param ctorArgs
     *            The constructor argument values that is to be used while instantiating the
     *            component instance.
     * @param callbacks
     *            The callbacks that will be called at the appropriate life cycle phase.
     */
    Object registerComponentImplementation(Object compKey, Class compClass, Object[] ctorArgs,
        ComponentCallback[] callbacks);

    /**
     * This method will allow a component to have the same implementation and strategy as another
     * component, only a different interface.
     * 
     * @param compKey
     *            The key that identifies the component within the container.
     * @param srcCompKey
     *            The key that identifies the source component, within the container, the
     *            implementation of which will be reused.
     */
    Object registerComponentImplementation(Object compKey, Object srcCompKey);

    /**
     * This method will allow registration of an external factory for the component. The container
     * will simply delegate requests for concrete component instances to the registered factory
     * which should produce valid instances.
     * 
     * @param compKey
     *            The key that identifies the component within the container.
     * @param compFac
     *            The factory object instance that will produce component instances.
     * @param facMethodName
     *            The name of the factory method that will produce component instances.
     * @param facMethodArgs
     *            The factory method argument values that will be needed when requests are
     *            delegated.
     * @param callbacks
     *            The callbacks that will be called at the appropriate life cycle phase.
     */
    Object registerComponentFactory(Object compKey, Object compFac, String facMethodName,
        Object[] facMethodArgs, ComponentCallback[] callbacks);

    // Constructor arguments registration ==========================================================

    /**
     * This method will allow the registration of an implementation's constructor arguments
     * before/after the implementation itself has been registered.
     * 
     * @param compKey
     *            The key that identifies the component within the container.
     * @param ctorArgs
     *            The constructor argument values that is to be used while instantiating the
     *            component instance.
     */
    Object registerComponentConstructorArguments(Object compKey, Object[] ctorArgs);

    // Lifecycle methods registration ==============================================================

    /**
     * This method will allow the registration of an implementation's callbacks before/after the
     * implementation itself has been registered.
     * 
     * @param compKey
     *            The key that identifies the component within the container.
     * @param callbacks
     *            The callbacks that will be called at the appropriate life cycle phase.
     */
    Object registerComponentCallbacks(Object compKey, ComponentCallback[] callbacks);

    // Location registration methods ===============================================================

    /**
     * This method will allow the registration of the component's implementation registration
     * location.
     * <p>
     * This is an optional method that may have a null implementation. This method is primarily for
     * line precise error reporting purposes.
     * 
     * @param compKey
     *            The key that identifies the component within the container.
     * @param location
     *            The location of the component registration.
     */
    Object registerComponentRegistrationLocation(Object compKey, Location location);

    /**
     * This method will allow the registration of the component's instance retrieval location.
     * <p>
     * This is an optional method that may have a null implementation. This method is primarily for
     * line precise error reporting purposes.
     * 
     * @param compKey
     *            The key that identifies the component within the container.
     * @param location
     *            The location of the component retrieval.
     */
    Object registerComponentRetrievalLocation(Object compKey, Location location);

    // Component strategy decorator methods ========================================================

    /**
     * This method will add a singleton strategy to the current component strategy.
     * 
     * @param compKey
     *            The key that identifies the component within the container.
     * @return Component Key.
     */
    Object wrapComponentStrategyWithSingleton(Object compKey);

    /**
     * This method will add a pooling strategy to the current component strategy.
     * 
     * @param compKey
     *            The key that identifies the component within the container.
     * @return Component Key.
     */
    Object wrapComponentStrategyWithPooling(Object compKey);

    /**
     * This method will add a thread local strategy to the current component strategy.
     * 
     * @param compKey
     *            The key that identifies the component within the container.
     * @return Component Key.
     */
    Object wrapComponentStrategyWithThreadLocal(Object compKey);

    // Configuration methods =======================================================================

    /**
     * This method will allow the registration of a single configuration item. The item will be
     * added to a list identified by the provided configuration key.
     * 
     * @param configKey
     *            The key that identifies the configutation data within the container.
     * @param configItem
     *            The configuration datum to be added to the configuration data.
     */
    Object registerConfiguration(Object configKey, Object configItem);

    /**
     * This method will allow the registration of a single configuration item. The item will be
     * added to a map identified by the provided configuration key.
     * 
     * @param configKey
     *            The key that identifies the configutation data within the container.
     * @param configItemKey
     *            The key that identifies the configuration item within the configuration map.
     * @param configItem
     *            The configuration datum to be added to the configuration data.
     */
    Object registerConfiguration(Object configKey, Object configItemKey, Object configItem);
}