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

/**
 * This is a {@link java.lang.ThreadLocal}decorator that will register itself with
 * {@link gravity.util.ThreadEvent}to be notified.
 * 
 * @author Harish Krishnaswamy
 * @version $Id: CleanableThreadLocal.java,v 1.2 2004-06-14 04:22:10 harishkswamy Exp $
 */
public class CleanableThreadLocal implements ThreadPreTerminationObserver
{
    private static final String INITIALIZED = "$gravity.CleanableThreadLocal#initialized$";

    private static class InitializableThreadLocal extends ThreadLocal
    {
        /**
         * <p>
         * Intializes the variable with a HashMap containing a single Boolean flag to denote the
         * initialization of the variable. The Boolean flag will be used to determine when to
         * register the listener with {@link ThreadEvent}.
         * <p>
         * The registration cannot be done from here because it may get lost once the caller method
         * {@link ThreadLocal#get()}or {@link ThreadLocal#set(java.lang.Object)}completes, if this
         * was the first ThreadLocal variable access for the Thread.
         */
        protected Object initialValue()
        {
            // NOTE: This is a workaround to circumvent the ThreadLocal behavior.
            // It would be easier if the implementation of ThreadLocal.get() checked for
            // the existence of the thread local map, after initialValue() is evaluated,
            // and used it instead of creating a new map always after initialization (possibly
            // overwriting any variables created from within ThreadLocal.initialValue()).

            return INITIALIZED;
        }
    }

    private InitializableThreadLocal     _threadLocal = new InitializableThreadLocal();
    private ThreadPreTerminationObserver _observer;

    public CleanableThreadLocal()
    {
        // No args constructor
    }

    public CleanableThreadLocal(ThreadPreTerminationObserver observer)
    {
        _observer = observer;
    }

    /**
     * Gets the thread local variable and registers the listener with {@link ThreadEvent}if the
     * thread local variable has been initialized. The registration cannot be done from within
     * {@link InitializableThreadLocal#initialValue()}because the notifier's thread local variable
     * could possibly be overwritten and the listeners be lost.
     */
    private void initializeThreadLocal()
    {
        if (INITIALIZED.equals(_threadLocal.get()))
        {
            ThreadEvent.getInstance().registerObserver(ThreadPreTerminationObserver.class, this);

            _threadLocal.set(null);
        }
    }

    public Object get()
    {
        initializeThreadLocal();

        return _threadLocal.get();
    }

    public void set(Object value)
    {
        initializeThreadLocal();

        _threadLocal.set(value);
    }

    public void handleThreadPreTermination()
    {
        if (_observer != null)
            _observer.handleThreadPreTermination();

        _threadLocal.set(null);
    }
}