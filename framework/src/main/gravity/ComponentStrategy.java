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
 * The strategy decides the identity and the life of concrete component instances returned by
 * {@link gravity.Component}.
 * <p>
 * There are two kinds of strategies: dispatching and lazy loading. All calls on a component will
 * get dispatched to its strategy if the strategy is of dispatching kind. The strategy in turn
 * determines the component instance that needs to be used for the calls. If the strategy is of lazy
 * loading kind, only the very first call on the component gets dispatched to the strategy; all
 * subsequent calls will reuse the same component instance returned the first time.
 * 
 * @author Harish Krishnaswamy
 * @version $Id: ComponentStrategy.java,v 1.3 2004-09-02 03:58:27 harishkswamy Exp $
 */
public interface ComponentStrategy
{
    /**
     * This method will return a concrete instance of this component. The component instance
     * indentity is implementation dependent. This method will throw a runtime exception if invoked
     * prior to registering an implementation for the component.
     * 
     * @throws WrapperException
     *             Wraps the actual exception thrown while obtaining the concrete component
     *             instance.
     */
    Object getComponentInstance(RealizableComponent component);

    /**
     * This method will accept a component instance and return it to its cache.
     */
    void collectComponentInstance(Object compInst);

    /**
     * @return true If this strategy is in dispatching state, false otherwise.
     */
    boolean isDispatching();
}