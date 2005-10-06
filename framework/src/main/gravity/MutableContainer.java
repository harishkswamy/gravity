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

/**
 * This is a mutable container that allows new {@link gravity.Component}s, configuration data and
 * {@link gravity.ComponentStrategy}s to be registered with it.
 * <p>
 * Prior to accessing the services provided by the container clients must invoke
 * {@link #initialize(MutableContext)}to initialize the container.
 * 
 * @author Harish Krishnaswamy
 * @version $Id: MutableContainer.java,v 1.9 2005-10-06 21:59:23 harishkswamy Exp $
 */
public interface MutableContainer extends Container
{
    /**
     * This is the key for the default path of the plugin manifest file that the container will use
     * to search for plugins. The path is relative to the classpath root and will be obtained from
     * the context using this key.
     */
    String PLUGINS_DEFAULT_MANIFEST_CLASSPATH_KEY = "gravity.plugins.default.manifest.classpath";

    /**
     * This method must be invoked to load the container with components. Container will either
     * autoload components from the classpath or only load the ones specifically requested by the
     * client, depending on the <code>gravity.plugins.autoload</code> property.
     */
    void load();

    /**
     * Switches the current context to the provided context. Components/configurations loaded after
     * this method call will load under the new context. Components/configurations loaded prior to
     * this method call will be untouched.
     */
    void switchContextTo(Context context);

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
     * @return The component key.
     */
    ComponentKey registerComponentImplementation(ComponentKey compKey, Class compClass,
        Object[] ctorArgs, ComponentCallback[] callbacks);

    /**
     * This method will allow a component to have the same implementation and strategy as another
     * component, only a different interface.
     * 
     * @param compKey
     *            The key that identifies the component within the container.
     * @param srcCompKey
     *            The key that identifies the source component, within the container, the
     *            implementation of which will be reused.
     * @return The component key.
     */
    ComponentKey registerComponentImplementation(ComponentKey compKey, ComponentKey srcCompKey);

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
     * @return The component key.
     */
    ComponentKey registerComponentFactory(ComponentKey compKey, Object compFac,
        String facMethodName, Object[] facMethodArgs, ComponentCallback[] callbacks);

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
     * @return The component key.
     */
    ComponentKey registerComponentConstructorArguments(ComponentKey compKey, Object[] ctorArgs);

    // Lifecycle methods registration ==============================================================

    /**
     * This method will allow the registration of an implementation's callbacks before/after the
     * implementation itself has been registered.
     * 
     * @param compKey
     *            The key that identifies the component within the container.
     * @param callbacks
     *            The callbacks that will be called at the appropriate life cycle phase.
     * @return The component key.
     */
    ComponentKey registerComponentCallbacks(ComponentKey compKey, ComponentCallback[] callbacks);

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
     * @return The component key.
     */
    ComponentKey registerComponentRegistrationLocation(ComponentKey compKey, Location location);

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
     * @return The component key.
     */
    ComponentKey registerComponentRetrievalLocation(ComponentKey compKey, Location location);

    // Component strategy decorator methods ========================================================

    /**
     * This method will add a strategy of the provided type to the current component strategy.
     * 
     * @param compKey
     *            The key that identifies the component within the container.
     * @param strategyType
     *            The type of strategy to be added.
     * @return The component key.
     */
    ComponentKey wrapComponentStrategy(ComponentKey compKey, ComponentStrategyType strategyType);

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
    Object registerConfigurationItem(Object configKey, Object configItem);

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
    Object registerConfigurationItem(Object configKey, Object configItemKey, Object configItem);

    /**
     * This method will allow the registration of any object with the container. The object will be
     * added to the container under the specified key. If the container already has the specified
     * key, this method will replace the existing object in the container with the provided object.
     * This method will provide the ability to add components that do not require any dynamic
     * behavior.
     * 
     * @param configKey
     *            The key that identifies the configutation data within the container.
     * @param config
     *            The object to be added to the container.
     * @return Returns the onfiguration key.
     */
    Object registerConfiguration(Object configKey, Object config);

    /**
     * This method will scan the provided array for component keys and replace these keys with
     * component instances retrieved from the container.
     */
    void realizeKeys(Object[] args);
}