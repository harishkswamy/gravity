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

import gravity.GravityTestCase;
import gravity.WrapperException;
import gravity.mocks.MockComboServiceImpl;
import gravity.mocks.MockSetterServiceImpl;

import java.text.Format;

/**
 * @author Harish Krishnaswamy
 * @version $Id: ReflectUtilsTest.java,v 1.4 2004-06-14 04:24:30 harishkswamy Exp $
 */
public class ReflectUtilsTest extends GravityTestCase
{
    public void testInvokeNoArgConstructor()
    {
        Object obj = ReflectUtils.invokeConstructor(Object.class, null);

        assertNotNull(obj);
    }

    public void testInvokeConstructor()
    {
        Object obj = ReflectUtils.invokeConstructor(String.class, new Object[]{"xyz"});

        assertNotNull(obj);
    }

    public void testInvokeWithNullArgs()
    {
        ReflectUtils.invokeConstructor(MockComboServiceImpl.class, new Object[]{new Integer(2),
            null});

        try
        {
            ReflectUtils.invokeConstructor(MockComboServiceImpl.class, new Object[]{null, null});

            unreachable();
        }
        catch (WrapperException e)
        {
            assertSuperString(e.getWrapped(),
                "Unable to find constructor: gravity.mocks.MockComboServiceImpl(null, null)");
        }
    }

    public void testInvokeNonMatchingConstructor()
    {
        try
        {
            ReflectUtils.invokeConstructor(Object.class, new Object[]{new Integer(1)});

            unreachable();
        }
        catch (WrapperException e)
        {
            assertSuperString(e.getWrapped(),
                "Unable to find constructor: java.lang.Object(java.lang.Integer)");
        }
    }

    public void testFailureInvokingConstructor()
    {
        try
        {
            ReflectUtils.invokeConstructor(Format.class, null);

            unreachable();
        }
        catch (Exception e)
        {
            assertSuperString(e, "Unable to invoke constructor: class java.text.Format()");
        }
    }

    public void testInvokeValidSetter()
    {
        MockSetterServiceImpl obj = new MockSetterServiceImpl();
        ReflectUtils.invokeMethod(obj, "setPrimitive", new Object[]{new Integer(5)});
        assertTrue(obj.getPrimitive() == 5);

        Object obj1 = new Object();
        ReflectUtils.invokeMethod(obj, "setObject", new Object[]{obj1});
        assertSame(obj.getObject(), obj1);
    }

    public void testInvokeUnavailableSetter()
    {
        try
        {
            ReflectUtils.invokeMethod(new Object(), "setUnavailableProp", new Object[]{"someVal"});

            unreachable();
        }
        catch (WrapperException e)
        {
            assertSuperString(e.getWrapped(),
                "Unable to find method: setUnavailableProp(java.lang.String) in: class "
                    + "java.lang.Object");
        }
    }

    public void testUnmatchedSetter()
    {
        try
        {
            ReflectUtils.invokeMethod(new MockComboServiceImpl(1, null), "setPrimitive",
                new Object[]{"someVal"});

            unreachable();
        }
        catch (WrapperException e)
        {
            assertSuperString(e.getWrapped(),
                "Unable to find method: setPrimitive(java.lang.String) in: class "
                    + "gravity.mocks.MockComboServiceImpl");
        }
    }

    public void testInvokeSetterException()
    {
        try
        {
            ReflectUtils.invokeMethod(new Object(), null, null);

            unreachable();
        }
        catch (Exception e)
        {
            assertSuperString(e, "Unable to invoke method: null()");
        }
    }
}