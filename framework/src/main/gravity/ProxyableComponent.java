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
 * @version $Id: ProxyableComponent.java,v 1.2 2004-06-14 04:23:43 harishkswamy Exp $
 */
public interface ProxyableComponent extends Component
{
    /**
     * Returns the interface class for this component.
     */
    Class getInterface();

    /**
     * Returns the implementation for this component.
     */
    Object getImplementation();

    /**
     * This method will return a concrete instance of this component. The component instance
     * indentity is dependent on the state of the component. This method will throw a runtime
     * exception if invoked prior to registering an implementation for the component.
     * 
     * @throws WrapperException
     *         Wraps the actual exception thrown while obtaining the concrete component instance.
     */
    Object getConcreteInstance();

    /**
     * Returns a new concrete instance of this component.
     */
    Object newInstance();

    /**
     * @return true If the component is in dispatching state and false otherwise.
     */
    boolean isInDispatchingState();
}