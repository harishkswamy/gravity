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
 * @version $Id: Pool.java,v 1.1 2004-05-10 17:28:51 harishkswamy Exp $
 */
public class Pool
{
    private static final int DEFAULT_MAX_SIZE = 10;
    private int              _maxSize;
    private List             _pooledObjects   = new ArrayList();
    private List             _loanedObjects   = new ArrayList();

    public Pool()
    {
        _maxSize = DEFAULT_MAX_SIZE;
    }

    public Pool(int maxSize)
    {
        _maxSize = (maxSize <= 0) ? DEFAULT_MAX_SIZE : maxSize;
    }

    public synchronized void collect(Object item)
    {
        Object comp = null;

        for (Iterator itr = _loanedObjects.iterator(); itr.hasNext();)
        {
            SoftReference ref = (SoftReference) itr.next();

            Object referent = ref.get();

            if (referent == null || item.equals(referent))
                itr.remove();

            if (item.equals(referent))
                comp = referent;
        }

        if (comp != null && _pooledObjects.size() <= _maxSize)
            _pooledObjects.add(new SoftReference(comp));
    }

    public synchronized Object loan()
    {
        for (int i = 1; i <= _pooledObjects.size(); i++)
        {
            SoftReference ref = (SoftReference) _pooledObjects.remove(_pooledObjects.size() - i);

            Object comp = ref.get();

            if (comp != null)
            {
                _loanedObjects.add(ref);

                return comp;
            }
        }

        return null;
    }

    public synchronized void loaned(Object item)
    {
        if (_pooledObjects.size() + _loanedObjects.size() <= _maxSize)
            _loanedObjects.add(new SoftReference(item));
    }
}