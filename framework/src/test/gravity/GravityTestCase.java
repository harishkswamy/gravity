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

import gravity.util.ClassUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import junit.framework.TestCase;

/**
 * @author Harish Krishnaswamy
 * @version $Id: GravityTestCase.java,v 1.1 2004-05-10 17:29:08 harishkswamy Exp $
 */
public class GravityTestCase extends TestCase
{
    protected void unreachable()
    {
        fail("ERROR: Reached unreachable code.");
    }

    protected void assertSuperString(Object t, String msg)
    {
        if (t.toString().indexOf(msg) > -1)
            return;

        fail("Expected super-string of: <" + msg + ">\n\tbut was: <" + t.toString() + ">");
    }

    protected void writeFile(String fPath, String content)
    {
        try
        {
            File file = new File(fPath);

            BufferedWriter bw = new BufferedWriter(new FileWriter(file));

            bw.write(content);

            bw.close();
        }
        catch (Exception e)
        {
            throw WrapperException.wrap(e);
        }
    }

    protected void writePluginFile(String pluginProp)
    {
        String path = ClassUtils.getResource(Gravity.PLUGIN_MANIFEST_FILE_PATH).getFile();

        writeFile(path, pluginProp);
    }
}