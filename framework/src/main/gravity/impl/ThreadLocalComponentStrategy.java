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
import gravity.util.CleanableThreadLocal;
import gravity.util.ThreadPreTerminationObserver;

/**
 * @author Harish Krishnaswamy
 * @version $Id: ThreadLocalComponentStrategy.java,v 1.1 2004-05-22 20:19:31 harishkswamy Exp $
 */
public class ThreadLocalComponentStrategy extends DispatchingComponentStrategy implements
    ThreadPreTerminationObserver
{
    private CleanableThreadLocal _threadLocal;

    public ThreadLocalComponentStrategy(ComponentStrategy delegate, ProxyableComponent component)
    {
        super(delegate, component);

        // This will register for ThreadEvents and notify us when appropriate.
        _threadLocal = new CleanableThreadLocal(this);
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

    /**
     * This will be invoked by {@link CleanableThreadLocal}prior to clearing the thread local
     * variable.
     */
    public void handleThreadPreTermination()
    {
        collectComponentInstance(_threadLocal.get());
    }

    public String toString()
    {
        return "[Thread Local: " + super.toString() + "]";
    }
}