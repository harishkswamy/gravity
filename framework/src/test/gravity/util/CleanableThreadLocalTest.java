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

package gravity.util;

import junit.framework.TestCase;

import org.easymock.MockControl;

/**
 * @author Harish Krishnaswamy
 * @version $Id: CleanableThreadLocalTest.java,v 1.2 2004-05-22 20:19:38 harishkswamy Exp $
 */
public class CleanableThreadLocalTest extends TestCase
{
    private CleanableThreadLocal         _local;
    private MockControl                  _observerControl;
    private ThreadPreTerminationObserver _observerMock;

    private void setUpLocal()
    {
        _local = new CleanableThreadLocal();
    }

    private void setUpObservedLocal()
    {
        _observerControl = MockControl.createStrictControl(ThreadPreTerminationObserver.class);
        _observerMock = (ThreadPreTerminationObserver) _observerControl.getMock();

        _local = new CleanableThreadLocal(_observerMock);
    }

    public void tearDown()
    {
        ThreadEvent.getInstance().cleanUp();
    }

    public void testGetSet()
    {
        setUpLocal();

        Object obj = _local.get();

        assertNull(obj);

        Object objB = new Object();
        _local.set(objB);

        obj = _local.get();

        assertSame(obj, objB);
    }

    public void testHandleThreadPreTermination()
    {
        setUpLocal();

        Object objA = new Object();
        _local.set(objA);

        Object obj = _local.get();
        assertSame(obj, objA);

        _local.handleThreadPreTermination();
        obj = _local.get();
        assertNull(obj);
    }

    public void testHandleThreadPreTerminationWhenObserved()
    {
        setUpObservedLocal();

        _observerMock.handleThreadPreTermination();
        _observerControl.setVoidCallable(2);

        _observerControl.replay();

        _local.handleThreadPreTermination();
        _local.handleThreadPreTermination();

        _observerControl.verify();
    }

    public void testInitialized()
    {
        setUpObservedLocal();

        _observerMock.handleThreadPreTermination();

        _observerControl.replay();

        _local.set("xyz");
        ThreadEvent.getInstance().notifyPreTerminationObservers();

        _observerControl.verify();

        assertNull(_local.get());
    }
}