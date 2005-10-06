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

package gravity.plugins;

import gravity.ComponentStrategy;
import gravity.ComponentStrategyType;
import gravity.Context;

/**
 * This is an enumerated type. It enumerates the various strategies available for
 * {@link gravity.Component}s.
 * <p>
 * Custom strategy types can be added simply by extending this class and adding new constants. The
 * type names should reflect the name of the class that represents the type.
 * 
 * @author Harish Krishnaswamy
 * @version $Id: DefaultComponentStrategyType.java,v 1.1 2005-10-06 21:59:30 harishkswamy Exp $
 */
public class DefaultComponentStrategyType implements ComponentStrategyType
{
    public static final DefaultComponentStrategyType LAZY_LOADING = new DefaultComponentStrategyType(
                                                                      "gravity.impl.LazyLoadingComponentStrategy");
    public static final DefaultComponentStrategyType DISPATCHING  = new DefaultComponentStrategyType(
                                                                      "gravity.impl.DispatchingComponentStrategy");
    public static final DefaultComponentStrategyType SINGLETON    = new DefaultComponentStrategyType(
                                                                      "gravity.impl.SingletonComponentStrategy");
    public static final DefaultComponentStrategyType POOLING      = new DefaultComponentStrategyType(
                                                                      "gravity.impl.PoolingComponentStrategy");
    public static final DefaultComponentStrategyType THREAD_LOCAL = new DefaultComponentStrategyType(
                                                                      "gravity.impl.ThreadLocalComponentStrategy");

    private String                                   _className;
    private volatile int                             _hashCode;

    protected DefaultComponentStrategyType(String name)
    {
        _className = name;
    }

    public boolean isTypeOf(ComponentStrategy strategy)
    {
        return strategy.getClass().getName().equals(_className);
    }

    public ComponentStrategy newInstance(Context context, ComponentStrategy decoratedStrategy)
    {
        Class strategyClass = context.getClassUtils().loadClass(_className);

        ComponentStrategy strategy = (ComponentStrategy) context.getReflectUtils().invokeConstructor(
            strategyClass, null);
        strategy.initialize(context, decoratedStrategy);

        return strategy;
    }

    public boolean equals(Object obj)
    {
        if (obj == this)
            return true;

        if (!(obj instanceof DefaultComponentStrategyType))
            return false;

        DefaultComponentStrategyType type = (DefaultComponentStrategyType) obj;

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