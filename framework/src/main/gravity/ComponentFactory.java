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
 * This is the factory that will produce components. A factory can only produce instances of one
 * component.
 * 
 * @author Harish Krishnaswamy
 * @version $Id: ComponentFactory.java,v 1.1 2004-05-10 17:29:04 harishkswamy Exp $
 */
public interface ComponentFactory
{
    /**
     * This method should be used to register an implementation for the component.
     * 
     * @param compClass
     *        Component implementation class.
     * @param compCtorArgs
     *        Component constructor arguments. This is simply an array of the constructor argument
     *        values that will be used in the order it is provided.
     * @param compSetrArgs
     *        Component setter arguments. This is a map of the implementation property names and
     *        their values.
     */
    void registerComponentImplementation(Class compClass, Object[] compCtorArgs, Map compSetrArgs);

    /**
     * This method may be used to register the location of the component registration. The sole
     * purpose of this method is to provide an error message with precise location information in
     * the event of an error.
     */
    void registerComponentRegistrationLocation(Location location);

    /**
     * This method will return an instance (concrete or hollow) of the component registered with
     * this factory. The component instance indentity is implementation dependent.
     * <p>
     * Only the following is true of the returned component instance:
     * <li>The returned component instance will be of the registered component type.
     * <li>Method invocations on the returned component instance will be completed successfully
     * provided an implementation was registered prior to the method invocation.
     * <p>
     * The advantage of using this method is that an instance of the component may be obtained even
     * prior to registering an implementation for the component.
     */
    Object getComponentInstance();

    /**
     * This method may be used to register the location of the component retrieval. The sole purpose
     * of this method is to provide an error message with precise location information in the event
     * of an error.
     */
    void registerComponentRetrievalLocation(Location location);

    /**
     * This method will return a concrete instance of the component registered with this factory.
     * The component instance indentity is implementation dependent. This method will throw a
     * runtime exception if invoked prior to registering an implementation for the component.
     * 
     * @throws WrapperException
     *         Wraps the actual exception thrown while obtaining the concrete component instance.
     */
    Object getConcreteComponentInstance();

    /**
     * This method should be used to return a component instance obtained via
     * {@link #getComponentInstance()}. This method is primarily used for returning pooled
     * components.
     */
    void returnComponentInstance(Object comp);
}