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

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Harish Krishnaswamy
 * @version $Id: Pool.java,v 1.3 2005-10-06 21:59:25 harishkswamy Exp $
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
        if (item == null)
            return;

        Object comp = null;

        // Do NOT use iterator here. If the collecting item is an unrealized proxy, the invocation
        // of the equals would cause it to realize which will lead to
        // ConcurrentModificationException
        for (int i = 0; i < _loanedObjects.size(); i++)
        {
            SoftReference ref = (SoftReference) _loanedObjects.get(i);

            Object referent = ref.get();

            // The collecting object could be a proxy and hence the equals should be invoked on
            // the collecting object; the referent is always a concrete object.
            if (referent == null || item.equals(referent))
                _loanedObjects.remove(i);

            if (item.equals(referent))
                comp = referent;

            // Continue even after finding the component, to remove null references from the pool.
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