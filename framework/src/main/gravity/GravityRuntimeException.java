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

/**
 * The root of all Gravity exceptions except {@link gravity.WrapperException}. The exception takes
 * in a message code, reads the message pattern from a properties file and forms the message with
 * the provided message parts. This exception can be checked by the catcher for the contained
 * message code.
 * 
 * @see gravity.util.Message
 * @author Harish Krishnaswamy
 * @version $Id: GravityRuntimeException.java,v 1.1 2004-09-02 03:52:21 harishkswamy Exp $
 */
public class GravityRuntimeException extends RuntimeException
{
    private String _messageCode;

    public GravityRuntimeException(String messageCode)
    {
        this(messageCode, null);
    }

    public GravityRuntimeException(String messageCode, Object msgPart1)
    {
        this(messageCode, new Object[]{msgPart1});
    }

    public GravityRuntimeException(String messageCode, Object msgPart1, Object msgPart2)
    {
        this(messageCode, new Object[]{msgPart1, msgPart2});
    }

    public GravityRuntimeException(String messageCode, Object msgPart1, Object msgPart2,
        Object msgPart3)
    {
        this(messageCode, new Object[]{msgPart1, msgPart2, msgPart3});
    }

    public GravityRuntimeException(String messageCode, Object[] msgParts)
    {
        super(Message.format(messageCode, msgParts));

        _messageCode = messageCode;
    }

    /**
     * @return true, if the contained message code and the provided message code are the same, false
     *         otherwise.
     */
    public boolean isMessageCode(String msgCode)
    {
        return (msgCode == null) ? (_messageCode == null) : _messageCode.equals(msgCode);
    }
}