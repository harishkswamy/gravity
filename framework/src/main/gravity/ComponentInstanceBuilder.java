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
 * @version $Id: ComponentInstanceBuilder.java,v 1.1 2004-11-17 19:36:19 harishkswamy Exp $
 */
public interface ComponentInstanceBuilder
{
    void registerImplementation(Class compClass, Object[] ctorArgs, ComponentCallback[] callbacks);

    void registerFactoryDelegate(Object factory, String factoryMethodName,
        Object[] factoryMethodArgs, ComponentCallback[] callbacks);

    void registerConstructorArguments(Object[] args);

    void registerCallbacks(ComponentCallback[] callbacks);

    /**
     * @return Returns a proxy instance for the provided component.
     */
    Object getInstance(RealizableComponent comp);

    boolean isInDispatchingState();

    void wrapStrategy(ComponentStrategyType strategyType);

    Object getConcreteInstance(RealizableComponent comp);

    /**
     * Builds a component via constructor and/or setter injection.
     * <p>
     * When the supplied array of constructor arguments is null, this method will use the setter
     * injection strategy, otherwise it uses the constructor injection strategy.
     * <p>
     * When both the array of constructor arguments and the map of setter properties are supplied,
     * this method will do a combo injection (constructor injection + setter injection).
     * 
     * @return Fully constructed and initialized component.
     * @throws UsageException
     *             When the supplied implementation class is null.
     * @throws WrapperException
     *             When there is any problem while building the component.
     * @see gravity.DynamicWeaver#weave(Object)
     * @see gravity.ComponentCallback
     * @see gravity.ComponentPhase
     */
    Object newInstance(RealizableComponent comp);

    // End - Construct new instance ================================================================
    void collectInstance(Object inst);
}