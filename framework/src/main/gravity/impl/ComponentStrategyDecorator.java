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
import gravity.ProxyableComponent;

/**
 * @author Harish Krishnaswamy
 * @version $Id: ComponentStrategyDecorator.java,v 1.1 2004-05-22 20:19:32 harishkswamy Exp $
 */
public abstract class ComponentStrategyDecorator implements ComponentStrategy
{
    protected ComponentStrategy     _decoratedState;
    protected ProxyableComponent _component;

    protected ComponentStrategyDecorator(ComponentStrategy decoratedState, ProxyableComponent component)
    {
        _decoratedState = decoratedState;

        _component = component;
    }

    public Object getConcreteComponentInstance()
    {
        if (_decoratedState == null)
            return _component.newInstance();

        else
            return _decoratedState.getConcreteComponentInstance();
    }

    public void collectComponentInstance(Object comp)
    {
        if (_decoratedState != null)
            _decoratedState.collectComponentInstance(comp);
    }

    public String toString()
    {
        if (_decoratedState == null)
            return _component.toString();
        else
            return _decoratedState.toString();
    }
}