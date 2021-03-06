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

import gravity.DynamicWeaver;

/**
 * The default weaver simply returns the original object.
 * 
 * @author Harish Krishnaswamy
 * @version $Id: DefaultDynamicWeaver.java,v 1.2 2005-10-06 21:59:29 harishkswamy Exp $
 */
public class DefaultDynamicWeaver implements DynamicWeaver
{
    /**
     * The default weaver simply returns the original object.
     */
    public Object weave(Object obj)
    {
        return obj;
    }
}