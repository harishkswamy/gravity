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
 * @version $Id: ComponentState.java,v 1.2 2004-05-18 04:56:31 harishkswamy Exp $
 */
public interface ComponentState
{
    /**
     * This method will return an instance (concrete or hollow) of the component. The component
     * instance indentity is implementation dependent.
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
     * This method will return a concrete instance of this component. The component instance
     * indentity is implementation dependent. This method will throw a runtime exception if invoked
     * prior to registering an implementation for the component.
     * 
     * @throws WrapperException
     *         Wraps the actual exception thrown while obtaining the concrete component instance.
     */
    Object getConcreteComponentInstance();

    /**
     * This method will accept a component and return it to its cache.
     */
    void collectComponentInstance(Object comp);
}