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

import gravity.ComponentState;
import gravity.ProxyableComponent;
import gravity.util.ThreadTerminationObserver;

/**
 * @author Harish Krishnaswamy
 * @version $Id: ThreadLocalComponentState.java,v 1.3 2004-05-18 20:52:04 harishkswamy Exp $
 */
public class ThreadLocalComponentState extends DispatchingComponentState implements
    ThreadTerminationObserver
{
    private ThreadLocal _threadLocal = new ThreadLocal();

    public ThreadLocalComponentState(ComponentState delegate, ProxyableComponent component)
    {
        super(delegate, component);
    }

    private synchronized void cacheComponent(Object component)
    {
        if (_threadLocal.get() == null)
            _threadLocal.set(component);
    }

    public Object getConcreteComponentInstance()
    {
        if (_threadLocal.get() == null)
            cacheComponent(super.getConcreteComponentInstance());

        return _threadLocal.get();
    }

    public void cleanUp()
    {
        collectComponentInstance(_threadLocal.get());

        _threadLocal.set(null);
    }

    public void handlePreThreadTermination()
    {
        cleanUp();
    }

    public String toString()
    {
        return "[Thread Local: " + super.toString() + "]";
    }
}