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

import gravity.ComponentStrategy;
import gravity.RealizableComponent;

/**
 * This class decorates {@link gravity.ComponentStrategy}. It can decorate the strategies
 * infinitely deep.
 * 
 * @author Harish Krishnaswamy
 * @version $Id: ComponentStrategyDecorator.java,v 1.4 2004-09-02 04:04:18 harishkswamy Exp $
 */
public abstract class ComponentStrategyDecorator implements ComponentStrategy
{
    protected ComponentStrategy _decoratedStrategy;

    protected ComponentStrategyDecorator(ComponentStrategy strategy)
    {
        _decoratedStrategy = strategy;
    }

    /**
     * @return Returns a new concrete instance for the provided component when this strategy does
     *         not decorate another strategy, otherwise it simply defers to the decorated strategy.
     */
    public Object getComponentInstance(RealizableComponent component)
    {
        if (_decoratedStrategy == null)
            return component.newInstance();

        return _decoratedStrategy.getComponentInstance(component);
    }

    /**
     * When this strategy decorates another strategy, this method defers to the decorated strategy,
     * otherwise it does nothing.
     * <p>
     * This method is intended to be overridden by subclasses that needs to recollect served
     * instances like the {@link PoolingComponentStrategy}for example.
     */
    public void collectComponentInstance(Object comp)
    {
        if (_decoratedStrategy != null)
            _decoratedStrategy.collectComponentInstance(comp);
    }

    /**
     * @return Returns an empty string when this strategy does not decorate another strategy,
     *         otherwise it defers to the decorated strategy.
     */
    public String decoratedStrategyToString()
    {
        return _decoratedStrategy == null ? "" : _decoratedStrategy.toString();
    }
}