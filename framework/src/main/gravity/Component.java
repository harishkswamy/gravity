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
 * The component is the central concept of the framework around which everything else revolves. A
 * component is simply an object that has an interface, an implementation and a
 * {@link gravity.ComponentStrategy strategy}to build instances. A component is the entity that can
 * be configured by the {@link gravity.Container container}. Implementations of this interface will
 * be quite analogous to {@link java.lang.Class}.
 * <p>
 * Every component has a unique identity typically comprised of the component interface and an
 * optional type (a String); and there will be only one instance of this class for each component.
 * <p>
 * The Component is also a factory for component instances just like {@link java.lang.Class}. The
 * identity of the component instances (not to be confused with the component identity) and their
 * life is dependent on the component strategy.
 * <p>
 * Components may share an implementation and a strategy. This will allow multiple components to act
 * as facets of the same implementation.
 * <p>
 * Prior to accessing the services provided by the component clients must invoke
 * {@link #initialize(Context, ComponentKey)} to initialize the component.
 * 
 * @author Harish Krishnaswamy
 * @version $Id: Component.java,v 1.12 2005-10-06 21:59:21 harishkswamy Exp $
 */
public interface Component
{
    void initialize(Context context, ComponentKey compKey);
    
    /**
     * This method should be used to register an implementation for this component provided by the
     * component passed in.
     * 
     * @param comp
     *            Component, the implementation of which will be used.
     */
    void registerImplementation(Component comp);

    /**
     * This method should be used to register an implementation for this component.
     * 
     * @param compClass
     *            Component implementation class.
     * @param ctorArgs
     *            Component constructor arguments. This is simply an array of the constructor
     *            argument values that will be used in the order it is provided.
     * @param callbacks
     *            Component methods to be invoked during component life cycle stages.
     */
    void registerImplementation(Class compClass, Object[] ctorArgs, ComponentCallback[] callbacks);

    /**
     * This method will allow registration of a factory for creating instances of the component.
     */
    void registerFactory(Object compFac, String facMethodName, Object[] facMethodArgs,
        ComponentCallback[] callbacks);

    /**
     * This method may be used to register the location of the component registration. The sole
     * purpose of this method is to provide error messages with precise location information in the
     * event of an error.
     */
    void setRegistrationLocation(Location location);

    /**
     * This method allows registration of the implementation's constructor arguments. These
     * arguments will be appended to the already existing arguments. The order of the arguments will
     * determine the constructor that will be selected for instatiation.
     */
    void registerConstructorArguments(Object[] args);

    /**
     * This method allows registration of the implementation's life-cycle callbacks. These callbacks
     * will be appended to the callbacks already registered. The callback invocations for a
     * particular life-cycle phase will be in the order it was provided.
     */
    void registerCallbacks(ComponentCallback[] callbacks);

    /**
     * Returns the factory for this component.
     */
    Object getFactory();

    /**
     * This method will return an instance (concrete or hollow/proxy) of this component. The
     * component instance indentity is dependent on the strategy used by the component at the time
     * of this method call.
     * <p>
     * Only the following is true of the returned component instance:
     * <li>The returned component instance will be of the registered component interface type.
     * <li>Method invocations on the returned component instance will be completed successfully
     * provided an implementation was registered prior to a method call.
     * <p>
     * By using this method, an instance of the component may be obtained even prior to registering
     * an implementation for the component.
     */
    Object getInstance();

    /**
     * This method may be used to register the location of the component retrieval. The sole purpose
     * of this method is to provide error messages with precise location information in the event of
     * an error.
     */
    void setRetrievalLocation(Location location);

    /**
     * This method will change the strategy for this component to that of the provided type.
     */
    void wrapStrategy(ComponentStrategyType strategyType);

    /**
     * This method should be used to return a component instance obtained via {@link #getInstance()}.
     * This method is primarily used for returning pooled components.
     */
    void collectInstance(Object comp);
}