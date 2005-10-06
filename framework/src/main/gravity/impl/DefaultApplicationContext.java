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

import gravity.ApplicationContext;
import gravity.ComponentKey;
import gravity.Container;
import gravity.MutableContainer;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a convenience base application context implementation. It is intended that all contexts
 * including the framework context extend this class. The parent child hierarchy of contexts is
 * built using decoration which this class supports.
 * <p>
 * This class stores the context attributes and the container; in addition this class is capable of
 * decorating/wrapping another application context. Thereby enabling the creation of a hierarchy of
 * contexts. The context that does not decorate another context is the root context.
 * <p>
 * Each instance of this class will have a new namespace for context attributes but all instances
 * share the single global container.
 * 
 * @author Harish Krishnaswamy
 * @version $Id: DefaultApplicationContext.java,v 1.1 2005-10-06 21:59:27 harishkswamy Exp $
 */
public class DefaultApplicationContext implements ApplicationContext
{
    protected ApplicationContext _decoratedContext;

    protected Map                _contextMap;

    private String               _name;

    /**
     * This is a reference to the single, global container. All contexts share the global container.
     */
    private MutableContainer     _container;

    /**
     * This will create a new context and initialize it.
     * 
     * @param name
     *            The name for this context. This can be null and is only used in
     *            {@link #toString()}.
     * @param context
     *            The context that needs to be wrapped. This can be null in which case this will be
     *            the root context.
     */
    public DefaultApplicationContext(String name, ApplicationContext context)
    {
        _name = name;
        _decoratedContext = context;
        _contextMap = new HashMap();
    }

    public MutableContainer getMutableContainer()
    {
        return _container == null ? (_decoratedContext == null ? null
            : _decoratedContext.getMutableContainer()) : _container;
    }

    public Object getContextItem(Object key)
    {
        Object item = _contextMap.get(key);

        return item == null ? (_decoratedContext == null ? null
            : _decoratedContext.getContextItem(key)) : item;
    }

    public void putContextItem(Object key, Object item)
    {
        _contextMap.put(key, item);
    }

    public ApplicationContext getDecoratedContext()
    {
        return _decoratedContext;
    }

    public void loadContextItems(Map items)
    {
        if (items != null)
            _contextMap.putAll(items);
    }

    // ~ Helper methods for contexts ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * This allows sub contexts to initialize the framework with a custom container.
     */
    public void setMutableContainer(MutableContainer container)
    {
        _container = container;
    }

    /**
     * This is a convenience method for application contexts to easily initialize the framework with
     * defaults.
     */
    public void initializeFramework()
    {
        // The framework context always wraps the application context hierarchy. IOW, the framework
        // context always has full access to the application context hierarchy.
        DefaultFrameworkContext context = new DefaultFrameworkContext(this);

        // The container has to stored in the application context to be visible to the application.
        setMutableContainer(new DefaultContainer(context));

        context.initialize();
    }

    public Container getContainer()
    {
        return getMutableContainer();
    }

    /**
     * This is a convenience method that returns the default component instance for the provided
     * class.
     */
    public Object getComponentInstance(Class keyClass)
    {
        Container container = getMutableContainer();

        ComponentKey key = container.getComponentKey(keyClass);

        return container.getComponentInstance(key);
    }

    public String toString()
    {
        return "[ID: " + super.toString() + ", Name: " + _name + ", Attributes: " + _contextMap
            + ", Decorated Context: " + _decoratedContext + "]";
    }
}
