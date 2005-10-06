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

import gravity.Context;

import java.net.URL;
import java.text.MessageFormat;
import java.util.Properties;

/**
 * @author Harish Krishnaswamy
 * @version $Id: Message.java,v 1.3 2005-10-06 21:59:25 harishkswamy Exp $
 */
public class Message
{
    /**
     * This property specifies the message resource bundle url.
     */
    public static final String MESSAGES_URL_KEY                        = "gravity.messages.url";

    /**
     * This property specifies the message resource bundle classpath.
     */
    public static final String MESSAGES_CLASSPATH_KEY                  = "gravity.messages.classpath";

    // Message Codes ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static final String GRAVITY_NOT_INITIALIZED                 = "GRAVITY_NOT_INITIALIZED";
    public static final String GRAVITY_ALREADY_INITIALIZED             = "GRAVITY_ALREADY_INITIALIZED";
    public static final String COMPONENT_IMPLEMENTATION_NOT_REGISTERED = "COMPONENT_IMPLEMENTATION_NOT_REGISTERED";
    public static final String COMPONENT_INTERFACE_MUST_NOT_BE_NULL    = "COMPONENT_INTERFACE_MUST_NOT_BE_NULL";
    public static final String CANNOT_CONSTRUCT_COMPONENT_INSTANCE     = "CANNOT_CONSTRUCT_COMPONENT_INSTANCE";
    public static final String CANNOT_FIND_CONSTRUCTOR                 = "CANNOT_FIND_CONSTRUCTOR";
    public static final String CANNOT_INVOKE_CONSTRUCTOR               = "CANNOT_INVOKE_CONSTRUCTOR";
    public static final String CANNOT_FIND_METHOD                      = "CANNOT_FIND_METHOD";
    public static final String CANNOT_INVOKE_METHOD                    = "CANNOT_INVOKE_METHOD";
    public static final String CANNOT_GET_CONCRETE_COMPONENT_INSTANCE  = "CANNOT_GET_CONCRETE_COMPONENT_INSTANCE";
    public static final String CANNOT_FIND_MODULE                      = "CANNOT_FIND_MODULE";
    public static final String PLUGIN_SPECIFICATION_ERROR              = "PLUGIN_SPECIFICATION_ERROR";
    public static final String PLUGIN_SPECIFICATION_EXECUTION_ERROR    = "PLUGIN_SPECIFICATION_EXECUTION_ERROR";
    public static final String CANNOT_CREATE_PROXY                     = "CANNOT_CREATE_PROXY";
    public static final String CANNOT_GET_RESOURCE                     = "CANNOT_GET_RESOURCE";
    public static final String CANNOT_LOAD_CLASS                       = "CANNOT_LOAD_CLASS";
    public static final String CANNOT_INSTANTIATE_OBJECT               = "CANNOT_INSTANTIATE_OBJECT";
    public static final String CANNOT_BUILD_URL                        = "CANNOT_BUILD_URL";
    public static final String INVALID_CLASS_TYPE                      = "INVALID_CLASS_TYPE";
    public static final String CANNOT_FORMAT_MESSAGE                   = "CANNOT_FORMAT_MESSAGE";
    public static final String CANNOT_LOAD_PROPERTIES                  = "CANNOT_LOAD_PROPERTIES";
    public static final String CONFIGURATION_NOT_REGISTERED            = "CONFIGURATION_NOT_REGISTERED";
    public static final String INVALID_IMPLEMENTATION_TYPE             = "INVALID_IMPLEMENTATION_TYPE";

    private Context            _context;
    private Properties         _messages;

    // ~ Constructors ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public Message(Context context)
    {
        _context = context;
    }

    /**
     * This method creates, loads and returns properties from the provided URL.
     * <p>
     * This method does not wrap any exception thrown in a WrapperException as usual, so a call from
     * {@link WrapperException}does not get back another WrapperException and go in an infinite
     * loop.
     */
    private Properties loadProperties(URL url) throws Exception
    {
        Properties props = new Properties();

        props.load(url.openStream());

        return props;
    }

    private URL getResource() throws Exception
    {
        Object urlObj = _context.getContextItem(MESSAGES_URL_KEY);

        URL url;

        if (urlObj == null)
            url = getClass().getClassLoader().getResource(
                (String) _context.getContextItem(MESSAGES_CLASSPATH_KEY));

        else if (urlObj instanceof String)
            url = new URL((String) urlObj);

        else
            url = (URL) urlObj;

        return url;
    }

    private synchronized void loadMessages() throws Exception
    {
        if (_messages == null)
            _messages = new Properties(loadProperties(getResource()));
    }

    /**
     * This is a special method for the {@link WrapperException}. This method does not catch the
     * exception and wrap it as usual to avoid an infinite loop.
     * 
     * @see #format(String, Object[])
     */
    public String formatSpecial(String messageCode, Object[] msgParts) throws Exception
    {
        if (_messages == null)
            loadMessages();

        String pattern = _messages.getProperty(messageCode, "[" + messageCode + "]");

        return MessageFormat.format(pattern, msgParts);
    }

    /**
     * This method is simply calls the {@link #formatSpecial(String, Object[])}method and catches
     * and wraps any exceptions thrown, in a {@link WrapperException}.
     * 
     * @throws WrapperException
     *             When unable to format the message.
     */
    public String format(String messageCode, Object[] msgParts)
    {
        try
        {
            return formatSpecial(messageCode, msgParts);
        }
        catch (Exception e)
        {
            throw _context.getExceptionWrapper().wrap(e, CANNOT_FORMAT_MESSAGE, messageCode);
        }
    }

    public void clear()
    {
        _messages.clear();
    }
}