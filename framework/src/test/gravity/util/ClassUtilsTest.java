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

import gravity.Gravity;
import gravity.GravityTestCase;
import gravity.plugins.BshPlugin;

import java.net.URL;
import java.text.Format;

/**
 * @author Harish Krishnaswamy
 * @version $Id: ClassUtilsTest.java,v 1.1 2004-05-10 17:29:12 harishkswamy Exp $
 */
public class ClassUtilsTest extends GravityTestCase
{
    public void testGetClassLoader()
    {
        ClassLoader loader = ClassUtils.getClassLoader();

        assertNotNull(loader);
    }

    public void testGetClassLoaderForClass()
    {
        ClassLoader loader = ClassUtils.getClassLoader(Gravity.class);

        assertNotNull(loader);
    }

    public void testGetResource()
    {
        URL url = ClassUtils.getResource("gravity.properties");

        assertNotNull(url);
    }

    public void testGetNonExistentResource()
    {
        try
        {
            ClassUtils.getResource("non-existent.file");

            unreachable();
        }
        catch (Exception e)
        {
            assertSuperString(e, "Cannot find resource: non-existent.file");
        }
    }

    public void testNewInstanceFromClass()
    {
        Object obj = ClassUtils.newInstance(BshPlugin.class);

        assertNotNull(obj);
    }

    public void testNewInstanceFromUnintantalizbleClass()
    {
        try
        {
            ClassUtils.newInstance(Format.class);
            
            unreachable();
        }
        catch (Exception e)
        {
            assertSuperString(e, "Cannot instantiate object for class: class java.text.Format");
        }
    }

    public void testLoadClass()
    {
        Class clazz = ClassUtils.loadClass("gravity.Gravity");
        
        assertNotNull(clazz);
    }
    
    public void testLoadNonExistentClass()
    {
        try
        {
            ClassUtils.loadClass("NonExistentClass");
            
            unreachable();
        }
        catch (Exception e)
        {
            assertSuperString(e, "Cannot load class: NonExistentClass");
        }
    }

    public void testNewInstanceFromClassName()
    {
        Object obj = ClassUtils.newInstance("gravity.plugins.BshPlugin");
        
        assertNotNull(obj);
    }
}