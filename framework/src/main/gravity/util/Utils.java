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

import gravity.WrapperException;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Harish Krishnaswamy
 * @version $Id: Utils.java,v 1.4 2004-06-14 04:21:07 harishkswamy Exp $
 */
public class Utils
{
    private Utils()
    {
        // This is a static class, not to be instantiated.
    }

    public static List splitQuoted(String str, char delimiter)
    {
        List tokens = new ArrayList();

        if (isBlank(str))
            return tokens;

        StringBuffer sb = new StringBuffer(str);

        int start = 0, ch;
        boolean quoted = false;

        for (int i = 0; i < sb.length(); i++)
        {
            ch = sb.charAt(i);

            if (ch == '"')
            {
                quoted = !quoted;
                continue;
            }

            if (quoted)
                continue;

            if (ch == delimiter)
            {
                tokens.add(sb.substring(start, i).trim());
                start = i + 1;
            }
        }

        tokens.add(sb.substring(start).trim());

        return tokens;
    }

    public static boolean isBlank(String value)
    {
        if (value == null || value.trim().length() == 0)
            return true;

        return false;
    }

    public static Properties loadProperties(URL url)
    {
        try
        {
            Properties props = new Properties();

            props.load(url.openStream());

            return props;
        }
        catch (Exception e)
        {
            throw WrapperException.wrap(e, "Cannot load properties from " + url);
        }
    }
}