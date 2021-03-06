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

import gravity.ExceptionWrapper;
import gravity.UsageException;

import java.net.URL;
import java.util.Enumeration;

/**
 * @author Harish Krishnaswamy
 * @version $Id: ClassUtils.java,v 1.7 2005-10-06 21:59:26 harishkswamy Exp $
 */
public class ClassUtils
{
    private ExceptionWrapper _exceptionWrapper;

    public ClassUtils()
    {
        // This is constructor is here only for testing to create a class proxy.
    }
    
    public ClassUtils(ExceptionWrapper exceptionWrapper)
    {
        _exceptionWrapper = exceptionWrapper;
    }

    /**
     * @return Returns the current thread's context class loader; when not found it returns the
     *         system class loader.
     */
    public ClassLoader getClassLoader()
    {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        return loader == null ? ClassLoader.getSystemClassLoader() : loader;
    }

    /**
     * @return Returns the class loader of the provided class; when not found it returns the class
     *         loader from {@link #getClassLoader()}.
     */
    public ClassLoader getClassLoader(Class clazz)
    {
        ClassLoader loader = clazz.getClassLoader();

        return loader == null ? getClassLoader() : loader;
    }

    /**
     * Loads the resource from the class loader returned by {@link #getClassLoader()}.
     * 
     * @param path
     *            The classpath of the resource.
     * @return The URL of the resource identified by the provided path.
     * @throws UsageException
     *             When the provided path does not translate to a valid resource.
     * @see ClassLoader#getResource(java.lang.String)
     */
    public URL getResource(String path)
    {
        URL url = getClassLoader().getResource(path);

        if (url == null)
            throw _exceptionWrapper.wrap(new UsageException(), Message.CANNOT_GET_RESOURCE, path);

        return url;
    }

    /**
     * Creates and returns the {@link URL}from the provided URL string.
     * 
     * @throws WrapperException
     *             {@link Message#CANNOT_BUILD_URL}: When a valid URL cannot be built from the
     *             provided URL string.
     */
    public URL newUrl(String urlStr)
    {
        try
        {
            return new URL(urlStr);
        }
        catch (Exception e)
        {
            throw _exceptionWrapper.wrap(e, Message.CANNOT_BUILD_URL, urlStr);
        }
    }

    /**
     * @return Returns an enumeration of URLs to resources found with provided string path using the
     *         class loader returned by {@link #getClassLoader()}.
     * @throws WrapperException
     *             {@link Message#CANNOT_GET_RESOURCE}
     * @see ClassLoader#getResources(java.lang.String)
     */
    public Enumeration getResources(String path)
    {
        try
        {
            Enumeration e = getClassLoader().getResources(path);

            return e;
        }
        catch (Exception e)
        {
            throw _exceptionWrapper.wrap(e, Message.CANNOT_GET_RESOURCE, path);
        }
    }

    // TODO move to ReflectUtils
    /**
     * Creates and returns a new instance of the provided class using the no args constructor.
     * 
     * @return Returns a new instance of the provided class that is created via reflection.
     * @throws WrapperException
     *             {@link Message#CANNOT_INSTANTIATE_OBJECT}
     */
    public Object newInstance(Class clazz)
    {
        try
        {
            return clazz.newInstance();
        }
        catch (Exception e)
        {
            throw _exceptionWrapper.wrap(e, Message.CANNOT_INSTANTIATE_OBJECT, clazz);
        }
    }

    /**
     * @return Loads and returns the Class for the provided class name from the class loader
     *         returned by {@link #getClassLoader()}.
     * @throws WrapperException
     *             {@link Message#CANNOT_LOAD_CLASS}
     */
    public Class loadClass(String className)
    {
        try
        {
            return getClassLoader().loadClass(className);
        }
        catch (Exception e)
        {
            throw _exceptionWrapper.wrap(e, Message.CANNOT_LOAD_CLASS, className);
        }
    }

    // TODO move to ReflectUtils
    /**
     * Loads the class for the provided class name and returns a new instance that is created via
     * reflection.
     * 
     * @see #loadClass(String)
     * @see #newInstance(Class)
     */
    public Object newInstance(String className)
    {
        Class clazz = loadClass(className);

        return newInstance(clazz);
    }
}