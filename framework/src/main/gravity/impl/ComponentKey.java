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

import java.util.HashMap;
import java.util.Map;

/**
 * @author Harish Krishnaswamy
 * @version $Id: ComponentKey.java,v 1.3 2004-05-29 16:38:10 harishkswamy Exp $
 */
public class ComponentKey
{
    private static final String DEFAULT_COMPONENT_TYPE = "default";

    private static final Map    KEYS                   = new HashMap();

    public static ComponentKey get(Class compIntf, Object compType)
    {
        ComponentKey compKey = (ComponentKey) KEYS.get("" + compIntf + compType);

        if (compKey == null)
        {
            compKey = new ComponentKey(compIntf, compType);

            synchronized (KEYS)
            {
                KEYS.put("" + compIntf + compType, compKey);
            }
        }

        return compKey;
    }

    private Class        _componentInterface;
    private Object       _componentType;
    private volatile int _hashCode;

    public ComponentKey(Class compIntf, Object compType)
    {
        if (compIntf == null)
            throw new IllegalArgumentException("Component interface must not be null.");

        if (compType == null)
            compType = DEFAULT_COMPONENT_TYPE;

        _componentInterface = compIntf;
        _componentType = compType;
    }

    public Class getComponentInterface()
    {
        return _componentInterface;
    }

    public String toString()
    {
        return "[Component Interface: " + _componentInterface + ", Component Type: "
            + _componentType + "]";
    }

    public boolean equals(Object obj)
    {
        if (obj == this)
            return true;

        if (!(obj instanceof ComponentKey))
            return false;

        ComponentKey key = (ComponentKey) obj;

        boolean compIntfIsEqual = _componentInterface == null ? key._componentInterface == null
            : _componentInterface.equals(key._componentInterface);

        boolean compTypeIsEqual = _componentType == null ? key._componentType == null
            : _componentType.equals(key._componentType);

        if (compIntfIsEqual && compTypeIsEqual)
            return true;

        return false;
    }

    public int hashCode()
    {
        if (_hashCode == 0)
        {
            int result = 17;

            result = 37 * result
                + (_componentInterface == null ? 0 : _componentInterface.hashCode());

            result = 37 * result + (_componentType == null ? 0 : _componentType.hashCode());

            _hashCode = result;
        }

        return _hashCode;
    }
}