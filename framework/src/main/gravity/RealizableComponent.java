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
 * Implementations of this interface will enable a component proxy to be realized into a concrete
 * instance. This is primarily an interface for {@link gravity.ComponentProxy}and
 * {@link gravity.ComponentStrategy}.
 * 
 * @author Harish Krishnaswamy
 * @version $Id: RealizableComponent.java,v 1.1 2004-09-02 03:54:27 harishkswamy Exp $
 */
public interface RealizableComponent extends Component
{
    /**
     * Returns the interface class for this component.
     */
    Class getInterface();

    /**
     * This method will return a concrete instance of this component. This method obeys the
     * component strategy and hence the component instance indentity will depend on the component
     * strategy at the time of the call.
     * 
     * @throws UsageException
     *             When the implementation class for the component is null.
     * @throws WrapperException
     *             Wraps the actual exception thrown while obtaining the concrete component
     *             instance.
     */
    Object getConcreteInstance();

    /**
     * Builds and returns a new component instance. This method is primarily for
     * {@link ComponentProxy}, it will return a new instance everytime unlike
     * {@link #getConcreteInstance()}.
     */
    Object newInstance();

    /**
     * @return true If the component is in dispatching state and false otherwise.
     */
    boolean isInDispatchingState();
}