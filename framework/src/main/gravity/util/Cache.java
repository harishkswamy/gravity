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

package gravity.util;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Harish Krishnaswamy
 * @version $Id: Cache.java,v 1.1 2004-05-18 04:56:32 harishkswamy Exp $
 */
public class Cache
{
    private List _cache = new ArrayList();

    public void add(Object item)
    {
        _cache.add(new SoftReference(item));
    }

    public List getAll()
    {
        List list = new ArrayList();

        for (Iterator itr = _cache.iterator(); itr.hasNext();)
        {
            SoftReference ref = (SoftReference) itr.next();

            Object item = ref.get();

            if (item == null)
                itr.remove();
            else
                list.add(item);
        }

        return list;
    }
}