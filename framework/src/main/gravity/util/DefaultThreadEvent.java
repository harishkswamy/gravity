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

package gravity.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Harish Krishnaswamy
 * @version $Id: DefaultThreadEvent.java,v 1.1 2005-10-06 21:59:25 harishkswamy Exp $
 */
public class DefaultThreadEvent implements ThreadEvent
{
    private ThreadLocal _threadLocal = new ThreadLocal();

    private void initialize()
    {
        Map observerTypes = new HashMap();

        observerTypes.put(ThreadPreTerminationObserver.class, new ArrayList());

        _threadLocal.set(observerTypes);
    }

    public DefaultThreadEvent()
    {
        initialize();
    }

    private List getObservers(Class observerType)
    {
        Map observerTypes = (Map) _threadLocal.get();

        return (List) observerTypes.get(observerType);
    }

    private void removeObservers(Class observerType)
    {
        Map observerTypes = (Map) _threadLocal.get();

        observerTypes.put(observerType, new ArrayList());
    }

    public void notifyPreTerminationObservers()
    {
        List observers = getObservers(ThreadPreTerminationObserver.class);

        for (Iterator itr = observers.iterator(); itr.hasNext();)
        {
            ThreadPreTerminationObserver observer = (ThreadPreTerminationObserver) itr.next();

            observer.handleThreadPreTermination();
        }

        removeObservers(ThreadPreTerminationObserver.class);
    }

    public void registerObserver(Class observerType, Object observer)
    {
        List observers = getObservers(observerType);

        observers.add(observer);
    }

    public void cleanUp()
    {
        initialize();
    }
}