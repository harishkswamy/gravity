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
 * Implementations of this interface will instrument objects dynamically at runtime. This is
 * basically a hook to allow cross-cutting aspects to be weaved into components dynamically.
 * 
 * @author Harish Krishnaswamy
 * @version $Id: DynamicWeaver.java,v 1.3 2004-05-29 16:54:38 harishkswamy Exp $
 */
public interface DynamicWeaver
{
    /**
     * Takes an object, that needs instrumentation, and enhances it (typically based on some
     * configuration).
     * <p>
     * This method will be called at the time of component construction right after instantiation.
     * 
     * @return The instrumented object.
     * @see gravity.impl.DefaultComponent#newInstance()()
     */
    Object weave(Object obj);
}