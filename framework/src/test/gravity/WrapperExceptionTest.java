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

package gravity;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author Harish Krishnaswamy
 * @version $Id: WrapperExceptionTest.java,v 1.1 2004-05-10 17:29:08 harishkswamy Exp $
 */
public class WrapperExceptionTest extends GravityTestCase
{
    public void testWrapRuntimeException()
    {
        Throwable t = WrapperException.wrap(new RuntimeException("Test exception"));

        assertTrue(t instanceof RuntimeException);
        assertFalse(t instanceof WrapperException);
    }

    public void testWrapCheckedException()
    {
        Throwable t = WrapperException.wrap(new Exception("Test exception"));

        assertTrue(t instanceof WrapperException);
    }

    public void testWrapRuntimeExceptionWoMsg()
    {
        Throwable t = WrapperException.wrap(new RuntimeException("Test exception"), null);

        assertTrue(t instanceof RuntimeException);
        assertFalse(t instanceof WrapperException);
    }

    public void testWrapRuntimeExceptionWithMsg()
    {
        Throwable t = WrapperException.wrap(new RuntimeException("Test exception"), "Wrapped msg");

        assertTrue(t instanceof WrapperException);
    }

    public void testWrapWrapperExceptionWithMsg()
    {
        Throwable t = WrapperException.wrap(new Exception("Test exception"));

        assertTrue(t instanceof WrapperException);

        t = WrapperException.wrap(t, "Wrapped msg");

        assertTrue(t instanceof WrapperException);

        t = WrapperException.wrap(t, "Double wrapped msg");

        WrapperException we = (WrapperException) t;

        assertTrue(we.getWrapped() instanceof Exception);
        assertFalse(we.getWrapped() instanceof RuntimeException);
        assertFalse(we.getWrapped() instanceof WrapperException);
    }

    public void testToString()
    {
        Throwable t = WrapperException.wrap(new Exception("Test exception"));

        assertSuperString(t, "java.lang.Exception: Test exception");

        t = WrapperException.wrap(t, "Wrapped msg");

        assertSuperString(t, "java.lang.Exception: Wrapped msg");

        t = WrapperException.wrap(t, "Double wrapped msg");

        assertSuperString(t, "java.lang.Exception: Double wrapped msg\n\tWrapped msg");
    }

    public void testPrintStackTrace()
    {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(buf);
        System.setErr(out);

        Throwable t = WrapperException.wrap(new Exception("Test exception."), "Appended message.");

        t.printStackTrace();

        assertSuperString(buf, "java.lang.Exception: Appended message.");

        t = WrapperException.wrap(new Exception("Test exception."));

        t.printStackTrace();

        assertSuperString(buf, "java.lang.Exception: Test exception.");
    }

    public void testPrintStackTraceToPrintStream()
    {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(buf);

        Throwable t = WrapperException.wrap(new Exception("Test exception."), "Appended message.");

        t.printStackTrace(out);

        assertSuperString(buf, "java.lang.Exception: Appended message.");
    }

    public void testPrintStackTraceToPrintWriter()
    {
        StringWriter buf = new StringWriter();
        PrintWriter out = new PrintWriter(buf);

        Throwable t = WrapperException.wrap(new Exception("Test exception."), "Appended message.");

        t.printStackTrace(out);

        assertSuperString(buf, "java.lang.Exception: Appended message.");

        t = WrapperException.wrap(new Exception("Test exception."));

        t.printStackTrace(out);

        assertSuperString(buf, "java.lang.Exception: Test exception.");
    }
}