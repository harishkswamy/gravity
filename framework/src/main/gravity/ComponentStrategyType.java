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

import gravity.util.ClassUtils;
import gravity.util.ReflectUtils;

/**
 * This is an enumerated type. It enumerates the various strategies available for
 * {@link gravity.Component}s.
 * <p>
 * Custom strategy types can be added simply by extending this class and adding new constants. The
 * type names should reflect the name of the class that represents the type.
 * 
 * @author Harish Krishnaswamy
 * @version $Id: ComponentStrategyType.java,v 1.1 2004-11-17 19:35:00 harishkswamy Exp $
 */
public class ComponentStrategyType
{
    public static final ComponentStrategyType LAZY_LOADING = new ComponentStrategyType(
                                                               "gravity.impl.LazyLoadingComponentStrategy");
    public static final ComponentStrategyType DISPATCHING  = new ComponentStrategyType(
                                                               "gravity.impl.DispatchingComponentStrategy");
    public static final ComponentStrategyType SINGLETON    = new ComponentStrategyType(
                                                               "gravity.impl.SingletonComponentStrategy");
    public static final ComponentStrategyType POOLING      = new ComponentStrategyType(
                                                               "gravity.impl.PoolingComponentStrategy");
    public static final ComponentStrategyType THREAD_LOCAL = new ComponentStrategyType(
                                                               "gravity.impl.ThreadLocalComponentStrategy");

    private String                            _className;
    private volatile int                      _hashCode;

    protected ComponentStrategyType(String name)
    {
        _className = name;
    }

    public final String getName()
    {
        return _className;
    }

    public final ComponentStrategy newInstance(ComponentStrategy decoratedStrategy)
    {
        Class clazz = ClassUtils.loadClass(_className);

        Object[] args = new Object[]{decoratedStrategy};

        return (ComponentStrategy) ReflectUtils.invokeConstructor(clazz, args);
    }

    public boolean equals(Object obj)
    {
        if (obj == this)
            return true;

        if (!(obj instanceof ComponentStrategyType))
            return false;

        ComponentStrategyType type = (ComponentStrategyType) obj;

        boolean typeIsEqual = (_className == null) ? (type._className == null)
            : _className.equals(type._className);

        if (typeIsEqual)
            return true;

        return false;
    }

    public int hashCode()
    {
        if (_hashCode == 0)
        {
            int result = 17;

            result = 37 * result + (_className == null ? 0 : _className.hashCode());

            _hashCode = result;
        }

        return _hashCode;
    }

    public String toString()
    {
        return "[Name: " + _className + "]";
    }
}