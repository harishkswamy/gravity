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

import gravity.Component;
import gravity.ComponentState;

/**
 * @author Harish Krishnaswamy
 * @version $Id: SingletonComponentState.java,v 1.1 2004-05-17 03:03:58 harishkswamy Exp $
 */
public class SingletonComponentState extends LazyLoadingComponentState
{
    private Object _component;

    public SingletonComponentState(ComponentState delegate, Component component)
    {
        super(delegate, component);
    }

    private synchronized void cacheComponent(Object component)
    {
        if (_component == null)
            _component = component;
    }

    public Object getConcreteComponentInstance()
    {
        if (_component == null)
            cacheComponent(super.getConcreteComponentInstance());

        return _component;
    }
    
    public String toString()
    {
        return "[Singleton: " + super.toString() + "]";
    }
}