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

/**
 * This exception is thrown when the usage of the framework is at fault.
 * 
 * @author Harish Krishnaswamy
 * @version $Id: UsageException.java,v 1.2 2004-09-02 03:52:21 harishkswamy Exp $
 */
public class UsageException extends GravityRuntimeException
{
    public UsageException(String messageCode)
    {
        super(messageCode);
    }

    public UsageException(String messageCode, Object msgPart1)
    {
        super(messageCode, msgPart1);
    }

    public UsageException(String messageCode, Object msgPart1, Object msgPart2)
    {
        super(messageCode, msgPart1, msgPart2);
    }

    public UsageException(String messageCode, Object msgPart1, Object msgPart2, Object msgPart3)
    {
        super(messageCode, msgPart1, msgPart2, msgPart3);
    }

    public UsageException(String messageCode, Object[] msgParts)
    {
        super(messageCode, msgParts);
    }
}