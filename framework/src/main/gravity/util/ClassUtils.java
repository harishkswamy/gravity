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

import gravity.UsageException;
import gravity.WrapperException;

import java.net.URL;
import java.util.Enumeration;

/**
 * @author Harish Krishnaswamy
 * @version $Id: ClassUtils.java,v 1.2 2004-05-13 06:12:29 harishkswamy Exp $
 */
public class ClassUtils
{
    public static ClassLoader getClassLoader()
    {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        return loader == null ? ClassLoader.getSystemClassLoader() : loader;
    }

    public static ClassLoader getClassLoader(Class clazz)
    {
        ClassLoader loader = clazz.getClassLoader();

        return loader == null ? getClassLoader() : loader;
    }

    public static URL getResource(String path)
    {
        URL url = getClassLoader().getResource(path);

        if (url == null)
            throw new UsageException("Cannot find resource: " + path);

        return url;
    }
    
    public static URL newUrl(String urlStr)
    {
        try
        {
            return new URL(urlStr);
        }
        catch (Exception e)
        {
            throw WrapperException.wrap(e, "Unable to build URL from: " + urlStr);
        }
    }

    public static Enumeration getResources(String path)
    {
        try
        {
            return getClassLoader().getResources(path);
        }
        catch (Exception e)
        {
            throw WrapperException.wrap(e, "Cannot get resources for path: " + path);
        }
    }

    // TODO move to ReflectUtils
    public static Object newInstance(Class clazz)
    {
        try
        {
            return clazz.newInstance();
        }
        catch (Exception e)
        {
            throw WrapperException.wrap(e, "Cannot instantiate object for class: " + clazz);
        }
    }

    public static Class loadClass(String className)
    {
        try
        {
            return getClassLoader().loadClass(className);
        }
        catch (Exception e)
        {
            throw WrapperException.wrap(e, "Cannot load class: " + className);
        }
    }

    // TODO move to ReflectUtils
    public static Object newInstance(String className)
    {
        Class clazz = loadClass(className);

        return newInstance(clazz);
    }
}