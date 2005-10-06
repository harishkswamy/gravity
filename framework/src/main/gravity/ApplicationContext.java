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

import java.util.Map;

/**
 * This represents the context for applications including the framework.
 * <p>
 * Implementations of this interface should be capable of decorating/wrapping another application
 * context.
 * 
 * @author Harish Krishnaswamy
 * @version $Id: ApplicationContext.java,v 1.1 2005-10-06 21:59:23 harishkswamy Exp $
 */
public interface ApplicationContext
{
    /**
     * This will search for the context item in this context with the provided key, if not found it
     * will repeat the search in the wrapped context. So this will in effect search the entire
     * context hierarchy before returning null.
     * 
     * @param key
     *            The key for which the context item is requested.
     * @return The context item for the provided key if found or null if not found.
     */
    Object getContextItem(Object key);

    /**
     * Adds the provided context item to this context with the provided key.
     * 
     * @param key
     *            The key used to identify the new context item in this context.
     * @param item
     *            The new context item to be added to this context.
     */
    void putContextItem(Object key, Object item);

    /**
     * Loads all the items in the provided map into this context.
     * 
     * @param items
     *            The context items that needs to be added to this context. If the provided items
     *            map is null nothing is added.
     */
    void loadContextItems(Map items);

    /**
     * @return Returns the mutable container held in this context.
     */
    MutableContainer getMutableContainer();

    /**
     * @return Returns the context decorated by this context ie. the parent context.
     */
    ApplicationContext getDecoratedContext();
}
