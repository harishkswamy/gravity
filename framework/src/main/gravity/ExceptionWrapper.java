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

package gravity;

/**
 * @author Harish Krishnaswamy
 * @version $Id: ExceptionWrapper.java,v 1.1 2005-10-06 21:59:22 harishkswamy Exp $
 */
public interface ExceptionWrapper
{
    /**
     * Wraps and returns non-RuntimeExceptions in WrapperException; RuntimeExceptions are returned
     * unaltered.
     */
    RuntimeException wrap(Throwable t);

    /**
     * Convenience method for {@link #wrap(Throwable, String, Object[])}.
     */
    RuntimeException wrap(Throwable t, String msgCode);

    /**
     * Convenience method for {@link #wrap(Throwable, String, Object[])}.
     */
    RuntimeException wrap(Throwable t, String msgCode, Object msgPart1);

    /**
     * Convenience method for {@link #wrap(Throwable, String, Object[])}.
     */
    RuntimeException wrap(Throwable t, String msgCode, Object msgPart1, Object msgPart2);

    /**
     * Convenience method for {@link #wrap(Throwable, String, Object[])}.
     */
    RuntimeException wrap(Throwable t, String msgCode, Object msgPart1, Object msgPart2,
        Object msgPart3);

    /**
     * Wraps and returns the provided throwable with a formatted message built from the provided
     * message code and parts.
     * 
     * @throws WrapperException
     *             When a formatted message cannot be built.
     */
    RuntimeException wrap(Throwable t, String msgCode, Object[] msgParts);
}
