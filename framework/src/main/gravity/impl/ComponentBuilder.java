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

package gravity.impl;

import gravity.Location;
import gravity.UsageException;
import gravity.WrapperException;
import gravity.util.ClassUtils;
import gravity.util.ReflectUtils;

import java.util.Iterator;
import java.util.Map;

/**
 * Helps in building the service implementation by instantiating, setting dependencies and
 * initializing the instantiated object.
 * 
 * @author Harish Krishnaswamy
 * @version $Id: ComponentBuilder.java,v 1.1 2004-05-10 17:28:54 harishkswamy Exp $
 */
public class ComponentBuilder
{
    private ComponentBuilder()
    {
        // Static class.
    }

    private static void setDependencies(Object impl, Map implSetrArgs)
    {
        if (implSetrArgs == null)
            return;

        for (Iterator itr = implSetrArgs.keySet().iterator(); itr.hasNext();)
        {
            String propertyName = (String) itr.next();

            ReflectUtils.invokeSetter(impl, propertyName, implSetrArgs.get(propertyName));
        }
    }

    /**
     * Gives {@link gravity.DynamicWeaver}an opportunity to weave the supplied object and then it
     * sets any dependencies via the setter methods with the provided property name/value map.
     */
    private static Object initializeService(Object impl, Map implSetrArgs)
    {
        Object enhImpl = DynamicWeaverFactory.getDynamicWeaver().weave(impl);

        setDependencies(enhImpl, implSetrArgs);

        return enhImpl;
    }

    /**
     * Builds a service via constructor injection.
     * <p>
     * Instantiates the service from the supplied class and arguments and initializes it.
     * 
     * @return Fully constructed and initialized service.
     */
    private static Object buildViaComboInjection(Class implClass, Object[] implCtorArgs,
        Map implSetrArgs)
    {
        Object impl = ReflectUtils.invokeConstructor(implClass, implCtorArgs);

        return initializeService(impl, implSetrArgs);
    }

    /**
     * Builds a service via setter injection.
     * <p>
     * Instantiates the service from the supplied class and initializes it.
     * 
     * @return Fully constructed and initialized service.
     */
    private static Object buildViaSetterInjection(Class implClass, Map implSetrArgs)
    {
        Object impl = ClassUtils.newInstance(implClass);

        return initializeService(impl, implSetrArgs);
    }

    /**
     * Builds a service via constructor and/or setter injection.
     * <p>
     * When the supplied array of constructor arguments is null, this method will use the setter
     * injection strategy, otherwise it uses the constructor injection strategy.
     * <p>
     * When both the array of constructor arguments and the map of setter properties are supplied,
     * this method will do a combo injection (constructor injection + setter injection).
     * 
     * @param compKey
     *        Component key.
     * @param intfLocation
     *        Source location of the service interface definition.
     * @param implClass
     *        Service implementation class.
     * @param implCtorArgs
     *        Actual service implementation constructor arguments.
     * @param implSetrArgs
     *        Actual service implementation dependencies.
     * @param implLocation
     *        Source location of the service implementation definition.
     * @return Fully constructed and initialized service.
     * @throws UsageException
     *         When the supplied implementation class is null.
     * @throws WrapperException
     *         When there is any problem while building the service.
     * @see gravity.DynamicWeaver#weave(Object)
     */
    public static Object build(ComponentKey compKey, Location intfLocation, Class implClass,
        Object[] implCtorArgs, Map implSetrArgs, Location implLocation)
    {
        // TODO make this a passive aggressive check
        if (implClass == null)
            throw new UsageException("Implementation not registered for component: " + compKey
                + " at location: " + intfLocation);

        try
        {
            Object impl;

            if (implCtorArgs == null)
                impl = buildViaSetterInjection(implClass, implSetrArgs);

            else
                impl = buildViaComboInjection(implClass, implCtorArgs, implSetrArgs);

            return impl;
        }
        catch (Exception e)
        {
            throw WrapperException.wrap(e, "Build error for component: " + compKey
                + " at location: " + implLocation);
        }
    }
}