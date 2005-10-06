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

/**
 * @author Harish Krishnaswamy
 * @version $Id: CleanableThreadLocal.java,v 1.3 2005-10-06 21:59:25 harishkswamy Exp $
 */
public interface CleanableThreadLocal
{
    void initialize(Context context, ThreadPreTerminationObserver observer);
    
    Object get();

    void set(Object value);
}