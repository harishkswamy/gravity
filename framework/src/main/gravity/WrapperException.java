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

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * Wraps and converts non {@link java.lang.RuntimeException}s to {@link java.lang.RuntimeException}s
 * In addition, it is capable of adding the supplied message to the wrapped exception.
 * 
 * @author Harish Krishnaswamy
 * @version $Id: WrapperException.java,v 1.1 2004-05-10 17:29:05 harishkswamy Exp $
 */
public class WrapperException extends RuntimeException
{
    private String    _msg;
    private Throwable _wrapped;

    private WrapperException(Throwable t)
    {
        _wrapped = t;
    }

    private WrapperException(Throwable t, String msg)
    {
        _wrapped = t;
        _msg = msg;
    }

    public static RuntimeException wrap(Throwable t)
    {
        if (t instanceof RuntimeException)
            return (RuntimeException) t;

        return new WrapperException(t);
    }

    public static RuntimeException wrap(Throwable t, String msg)
    {
        if (msg == null)
            return wrap(t);

        if (t instanceof WrapperException)
        {
            WrapperException we = (WrapperException) t;

            we._msg = (we._msg == null) ? msg : msg + "\n\t" + we._msg;

            return we;
        }
        else
            return new WrapperException(t, msg);
    }

    public String getMessage()
    {
        return _msg == null ? _wrapped.getMessage() : _msg;
    }

    /**
     * Returns a string of the following format: exception name: message
     * <p>
     * The exception name is always the name of the root cause exception. The message is a
     * concatenation of all wrapped messages, each separated by a newline and a tab (\n\t).
     */
    public String toString()
    {
        return _wrapped.getClass().getName() + ": " + getMessage();
    }

    public void printStackTrace()
    {
        printStackTrace(System.err);
    }

    public void printStackTrace(PrintStream out)
    {
        synchronized (out)
        {
            if (_msg != null)
                out.println(this);

            _wrapped.printStackTrace(out);
        }
    }

    public void printStackTrace(PrintWriter out)
    {
        synchronized (out)
        {
            if (_msg != null)
                out.println(this);

            _wrapped.printStackTrace(out);
        }
    }

    public Throwable getWrapped()
    {
        return _wrapped;
    }
}