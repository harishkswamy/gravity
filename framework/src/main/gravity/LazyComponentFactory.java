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
 * @version $Id: LazyComponentFactory.java,v 1.1 2004-05-10 17:29:01 harishkswamy Exp $
 */
public interface LazyComponentFactory extends ComponentFactory
{
    /**
     * This method must be invoked by decorators at the time of decoration (typically from within
     * the constructor of the decorator) to notify this factory of the decoration.
     */
    void decorated(LazyComponentFactory decoratorCompFac);

    /**
     * This method must be invoked by decorators at the time of realizing a proxy (providing a
     * concrete component instance for the proxy) to notify this factory of the component hatching.
     */
    void proxyRealized(Object proxy);

    /**
     * This method will return a concrete instance of the component registered with this factory.
     * The component instance indentity is implementation dependent. This method will throw a
     * runtime exception if invoked prior to registering an implementation for the component. This
     * method is intended to be invoked by hollow instances (proxies) of the component.
     * 
     * @throws WrapperException
     *         Wraps the actual exception thrown while obtaining the concrete component instance.
     */
    Object getConcreteComponentInstance(Object proxy);
}