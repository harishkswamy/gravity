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

package gravity.impl;

import gravity.DynamicWeaver;
import gravity.Gravity;
import gravity.WrapperException;
import gravity.util.ClassUtils;

/**
 * This is a factory that produces {@link gravity.DynamicWeaver}.
 * 
 * @author Harish Krishnaswamy
 * @version $Id: DynamicWeaverFactory.java,v 1.2 2004-05-17 03:04:03 harishkswamy Exp $
 */
public class DynamicWeaverFactory
{
    private static DynamicWeaver _weaver;

    private DynamicWeaverFactory()
    {
        // Static class.
    }

    /**
     * The default weaver simply returns the original object.
     */
    private static DynamicWeaver getDefaultDynamicWeaver()
    {
        return new DynamicWeaver()
        {
            public Object weave(Object obj)
            {
                return obj;
            }
        };
    }

    private static DynamicWeaver newDynamicWeaver()
    {
        String className = Gravity.getInstance().getProperty(Gravity.DYNAMIC_WEAVER_CLASS_NAME);

        if (className == null)
            return getDefaultDynamicWeaver();

        try
        {
            return (DynamicWeaver) ClassUtils.newInstance(className);
        }
        catch (ClassCastException e)
        {
            throw WrapperException.wrap(e, className + " must implement gravity.DynamicWeaver.");
        }
    }

    public static DynamicWeaver getDynamicWeaver()
    {
        if (_weaver == null)
            _weaver = newDynamicWeaver();

        return _weaver;
    }

    public static void cleanup()
    {
        _weaver = null;
    }
}