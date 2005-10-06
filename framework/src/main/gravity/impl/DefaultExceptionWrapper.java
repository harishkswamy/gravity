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

package gravity.impl;

import gravity.ExceptionWrapper;
import gravity.util.Message;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Harish Krishnaswamy
 * @version $Id: DefaultExceptionWrapper.java,v 1.1 2005-10-06 21:59:27 harishkswamy Exp $
 */
public class DefaultExceptionWrapper implements ExceptionWrapper
{
    private Message _message;

    public DefaultExceptionWrapper(Message message)
    {
        _message = message;
    }

    /**
     * Wraps and returns non-RuntimeExceptions in WrapperException; RuntimeExceptions are returned
     * unaltered.
     */
    public RuntimeException wrap(Throwable t)
    {
        if (t instanceof RuntimeException)
            return (RuntimeException) t;

        else if (t instanceof InvocationTargetException)
            t = ((InvocationTargetException) t).getTargetException();

        return new WrapperException(t);
    }

    /**
     * Convenience method for {@link #wrap(Throwable, String, Object[])}.
     */
    public RuntimeException wrap(Throwable t, String msgCode)
    {
        return wrap(t, msgCode, null);
    }

    /**
     * Convenience method for {@link #wrap(Throwable, String, Object[])}.
     */
    public RuntimeException wrap(Throwable t, String msgCode, Object msgPart1)
    {
        return wrap(t, msgCode, new Object[]{msgPart1});
    }

    /**
     * Convenience method for {@link #wrap(Throwable, String, Object[])}.
     */
    public RuntimeException wrap(Throwable t, String msgCode, Object msgPart1, Object msgPart2)
    {
        return wrap(t, msgCode, new Object[]{msgPart1, msgPart2});
    }

    /**
     * Convenience method for {@link #wrap(Throwable, String, Object[])}.
     */
    public RuntimeException wrap(Throwable t, String msgCode, Object msgPart1, Object msgPart2,
        Object msgPart3)
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
    public RuntimeException wrap(Throwable t, String msgCode, Object[] msgParts)
    {
        if (msgCode == null)
            return wrap(t);

        if (t instanceof WrapperException)
        {
            WrapperException we = (WrapperException) t;

            we.prependMessage(_message, msgCode, msgParts);

            return we;
        }
        else if (t instanceof InvocationTargetException)
            t = ((InvocationTargetException) t).getTargetException();

        return new WrapperException(t, _message, msgCode, msgParts);
    }

    private static class WrapperException extends RuntimeException
    {
        private static final long serialVersionUID = 3761409720300681522L;

        // ~ Instance Variables
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        private String            _msg;
        private Throwable         _wrapped;
        private List              _msgCodes        = new ArrayList();

        // ~ Constructors
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        WrapperException(Throwable t)
        {
            _wrapped = t;
        }

        WrapperException(Throwable t, Message message, String msgCode, Object[] msgParts)
        {
            // Wrapped is accessed within formatMessage so it needs to be set prior to making the
            // call.
            _wrapped = t;
            _msg = formatMessage(message, msgCode, msgParts);
        }

        private String formatMessage(Message message, String msgCode, Object[] msgParts)
        {
            try
            {
                _msgCodes.add(msgCode);

                return message.formatSpecial(msgCode, msgParts);
            }
            catch (Exception e)
            {
                _msg = "Unable to format message for code: " + msgCode + "\n\tWhile throwing: "
                    + getWrapped();
                _wrapped = e;

                throw this;
            }
        }

        void prependMessage(Message message, String msgCode, Object[] msgParts)
        {
            String msg = formatMessage(message, msgCode, msgParts);

            _msg = (_msg == null) ? msg : (msg + "\n\t" + _msg);
        }

        // ~ Inherited Methods
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

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

        // ~ Public Methods
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

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
}
