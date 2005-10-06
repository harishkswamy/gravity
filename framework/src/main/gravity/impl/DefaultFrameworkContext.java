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
import gravity.Component;
import gravity.ComponentInstanceBuilder;
import gravity.ComponentProxy;
import gravity.ComponentStrategy;
import gravity.Container;
import gravity.Context;
import gravity.DynamicWeaver;
import gravity.ExceptionWrapper;
import gravity.MutableContainer;
import gravity.Plugin;
import gravity.RealizableComponent;
import gravity.plugins.MutableContainerAdapter;
import gravity.plugins.bsh.BshPlugin;
import gravity.util.ClassUtils;
import gravity.util.CleanableThreadLocal;
import gravity.util.DefaultCleanableThreadLocal;
import gravity.util.DefaultThreadEvent;
import gravity.util.Message;
import gravity.util.ReflectUtils;
import gravity.util.ThreadEvent;
import gravity.util.Utils;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * This is the default context implementation for the framework. The framework context always wraps
 * any application contexts and thus the framwork can always get to the framework-defaults when they
 * are not overriden by the application.
 * 
 * @author Harish Krishnaswamy
 * @version $Id: DefaultFrameworkContext.java,v 1.1 2005-10-06 21:59:26 harishkswamy Exp $
 */
public class DefaultFrameworkContext extends DefaultApplicationContext implements Context,
    Cloneable
{
    private static ApplicationContext checkContext(ApplicationContext context)
    {
        if (context == null)
            throw new RuntimeException("Framework context must wrap an application context.");

        return context;
    }

    public DefaultFrameworkContext(ApplicationContext context)
    {
        super("Gravity Context", checkContext(context));
    }

    public void initialize()
    {
        putContextItem(ApplicationContext.class, DefaultApplicationContext.class);
        putContextItem(Component.class, DefaultComponent.class);
        putContextItem(ComponentInstanceBuilder.class, DefaultComponentInstanceBuilder.class);
        putContextItem(ComponentProxy.class, JdkComponentProxy.class);
        putContextItem(ComponentStrategy.class, LazyLoadingComponentStrategy.class);
        putContextItem(DynamicWeaver.class, DefaultDynamicWeaver.class);
        putContextItem(Plugin.class, BshPlugin.class);
        putContextItem(RealizableComponent.class, DefaultComponent.class);
        putContextItem(CleanableThreadLocal.class, DefaultCleanableThreadLocal.class);
        putContextItem(MutableContainerAdapter.class, MutableContainerAdapter.class);
        putContextItem(List.class, ArrayList.class);
        putContextItem(Map.class, HashMap.class);
        putContextItem(MutableContainer.PLUGINS_DEFAULT_MANIFEST_CLASSPATH_KEY,
            "META-INF/gravity-plugin.properties");
        putContextItem(Message.MESSAGES_CLASSPATH_KEY, "gravity/util/gravity-messages.properties");
        putContextItem(Container.PLUGINS_AUTOLOAD_KEY, Boolean.TRUE);
        putContextItem(PoolingComponentStrategy.POOL_SIZE_KEY, new Integer(10));

        setMessage(new Message(this));
        setExceptionWrapper(new DefaultExceptionWrapper(getMessage()));
        setUtils(new Utils(getExceptionWrapper()));
        setClassUtils(new ClassUtils(getExceptionWrapper()));
        setReflectUtils(new ReflectUtils(getExceptionWrapper()));
        setThreadEvent(new DefaultThreadEvent());
    }

    public Object clone()
    {
        try
        {
            return super.clone();
        }
        catch (CloneNotSupportedException e)
        {
            throw getExceptionWrapper().wrap(e);
        }
    }

    /**
     * This wraps the application context with a copy of the framework context. The reason to make a
     * copy of the framework context is because components are lazily realized and hence the
     * components have to maintain their contexts. Changing the decorated context in-place would
     * mess up the context hierarchy of already created components. Esentially anytime a context is
     * created the context hierarchy is frozen and should not be changed. If the hierarchy needs to
     * be changed the context should first be cloned using this method.
     * <p>
     * It is this method that guarantees the contract that the context of a component is stable
     * through out the life of the component.
     */
    private DefaultFrameworkContext copy(ApplicationContext decorated)
    {
        DefaultFrameworkContext ctx = (DefaultFrameworkContext) clone();

        ctx._decoratedContext = decorated;

        getMutableContainer().switchContextTo(ctx);

        return ctx;
    }

    private Properties getProperties(URL url)
    {
        if (url == null)
            return new Properties();

        return getUtils().loadProperties(url, null);
    }

    private ApplicationContext newApplicationContext(Properties props, String name)
    {
        String key = ApplicationContext.class.getName();

        String className = props.getProperty(key);

        Class ctxClass = className == null ? _decoratedContext.getClass()
            : getClassUtils().loadClass(className);

        return (ApplicationContext) getReflectUtils().invokeConstructor(ctxClass,
            new Object[]{name, _decoratedContext});
    }

    public Context decorateApplicationContext(String name, URL propsUrl)
    {
        Properties props = getProperties(propsUrl);

        name = name == null ? propsUrl == null ? null : propsUrl.toString() : name;

        ApplicationContext ctx = newApplicationContext(props, name);

        ctx.loadContextItems(props);

        return copy(ctx);
    }

    /**
     * Instantiates the implementation class registered in the Gravity properties for the provided
     * key.
     * 
     * @param implKey
     *            The implementation's key.
     * @param args
     *            The constructor arguments for the new API instance.
     * @return Object The new API instance.
     */
    public Object newApiInstance(Object implKey)
    {
        Object impl = getContextItem(implKey);

        if (impl instanceof Class)
            return getReflectUtils().invokeConstructor((Class) impl, null);

        Class implClass = getClassUtils().loadClass((String) impl);

        return getReflectUtils().invokeConstructor(implClass, null);
    }

    // Getters and setters ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public Message getMessage()
    {
        return (Message) getMutableContainer().getConfiguration(Message.class);
    }

    public void setMessage(Message message)
    {
        getMutableContainer().registerConfiguration(Message.class, message);
    }

    public ExceptionWrapper getExceptionWrapper()
    {
        return (ExceptionWrapper) getMutableContainer().getConfiguration(ExceptionWrapper.class);
    }

    public void setExceptionWrapper(ExceptionWrapper exceptionWrapper)
    {
        getMutableContainer().registerConfiguration(ExceptionWrapper.class, exceptionWrapper);
    }

    public Utils getUtils()
    {
        return (Utils) getMutableContainer().getConfiguration(Utils.class);
    }

    public void setUtils(Utils utils)
    {
        getMutableContainer().registerConfiguration(Utils.class, utils);
    }

    public ClassUtils getClassUtils()
    {
        return (ClassUtils) getMutableContainer().getConfiguration(ClassUtils.class);
    }

    public void setClassUtils(ClassUtils classUtils)
    {
        getMutableContainer().registerConfiguration(ClassUtils.class, classUtils);
    }

    public ReflectUtils getReflectUtils()
    {
        return (ReflectUtils) getMutableContainer().getConfiguration(ReflectUtils.class);
    }

    public void setReflectUtils(ReflectUtils reflectUtils)
    {
        getMutableContainer().registerConfiguration(ReflectUtils.class, reflectUtils);
    }

    /**
     * @return Returns the {@link ThreadEvent}for this context.
     */
    public ThreadEvent getThreadEvent()
    {
        return (ThreadEvent) getMutableContainer().getConfiguration(ThreadEvent.class);
    }

    public void setThreadEvent(ThreadEvent threadEvent)
    {
        getMutableContainer().registerConfiguration(ThreadEvent.class, threadEvent);
    }

    public void stripApplicationContext()
    {
        copy(_decoratedContext.getDecoratedContext());
    }

    /**
     * This method overrides {@link ApplicationContext#getContextItem(Object)} and searches for the
     * context item in the decorated context first prior to searching this context.
     */
    public Object getContextItem(Object key)
    {
        Object item = _decoratedContext == null ? null : _decoratedContext.getContextItem(key);

        return item == null ? _contextMap.get(key) : item;
    }
}