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

import gravity.util.Message;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Wraps and converts non {@link java.lang.RuntimeException}s to {@link java.lang.RuntimeException}
 * s. In addition, it is capable of realizing provided message code and adding the message to the
 * wrapped exception. All wrapped message codes are cached and can be queried to see if a particular
 * message code exists.
 * 
 * @author Harish Krishnaswamy
 * @version $Id: WrapperException.java,v 1.3 2004-09-02 03:52:22 harishkswamy Exp $
 */
public class WrapperException extends RuntimeException
{
    //~ Static Methods ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Wraps and returns non-RuntimeExceptions in WrapperException; RuntimeExceptions are returned
     * unaltered.
     */
    public static RuntimeException wrap(Throwable t)
    {
        if (t instanceof RuntimeException)
            return (RuntimeException) t;

        return new WrapperException(t);
    }

    /**
     * Convenience method for {@link #wrap(Throwable, String, Object[])}.
     */
    public static RuntimeException wrap(Throwable t, String msgCode)
    {
        return wrap(t, msgCode, null);
    }

    /**
     * Convenience method for {@link #wrap(Throwable, String, Object[])}.
     */
    public static RuntimeException wrap(Throwable t, String msgCode, Object msgPart1)
    {
        return wrap(t, msgCode, new Object[]{msgPart1});
    }

    /**
     * Convenience method for {@link #wrap(Throwable, String, Object[])}.
     */
    public static RuntimeException wrap(Throwable t, String msgCode, Object msgPart1,
        Object msgPart2)
    {
        return wrap(t, msgCode, new Object[]{msgPart1, msgPart2});
    }

    /**
     * Convenience method for {@link #wrap(Throwable, String, Object[])}.
     */
    public static RuntimeException wrap(Throwable t, String msgCode, Object msgPart1,
        Object msgPart2, Object msgPart3)
    {
        return wrap(t, msgCode, new Object[]{msgPart1, msgPart2, msgPart3});
    }

    /**
     * Wraps and returns the provided throwable with a formatted message built from the provided
     * message code and parts.
     * 
     * @throws WrapperException
     *             When a formatted message cannot be built.
     */
    public static RuntimeException wrap(Throwable t, String msgCode, Object[] msgParts)
    {
        if (msgCode == null)
            return wrap(t);

        if (t instanceof WrapperException)
        {
            WrapperException we = (WrapperException) t;

            we.prependMessage(msgCode, msgParts);

            return we;
        }

        return new WrapperException(t, msgCode, msgParts);
    }

    //~ Instance Variables ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private String    _msg;
    private Throwable _wrapped;
    private List      _msgCodes = new ArrayList();

    //~ Constructors ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private WrapperException(Throwable t)
    {
        _wrapped = t;
    }

    private WrapperException(Throwable t, String msgCode, Object[] msgParts)
    {
        // Wrapped is accessed within formatMessage so it needs to be set prior to making the call.
        _wrapped = t;
        _msg = formatMessage(msgCode, msgParts);
    }

    private String formatMessage(String msgCode, Object[] msgParts)
    {
        try
        {
            _msgCodes.add(msgCode);

            return Message.formatSpecial(msgCode, msgParts);
        }
        catch (Exception e)
        {
            _msg = "Unable to format message for code: " + msgCode + "\n\tWhile throwing: "
                + getWrapped();
            _wrapped = e;

            throw this;
        }
    }

    private void prependMessage(String msgCode, Object[] msgParts)
    {
        String msg = formatMessage(msgCode, msgParts);

        _msg = (_msg == null) ? msg : (msg + "\n\t" + _msg);
    }

    //~ Inherited Methods ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Returns the message for this and all wrapped exceptions.
     */
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

    //~ Public Methods ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * @return Returns the root cause that is wrapped in this exception.
     */
    public Throwable getWrapped()
    {
        return _wrapped;
    }

    /**
     * @return true, if the provided message code IS wrapped in this exception.
     * @return false, if the provided message code is NOT wrapped in this exception.
     */
    public boolean isMessageCode(String msgCode)
    {
        return _msgCodes.contains(msgCode);
    }
}