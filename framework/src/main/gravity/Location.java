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

/**
 * @author Harish Krishnaswamy
 * @version $Id: Location.java,v 1.2 2004-06-14 04:23:43 harishkswamy Exp $
 */
public class Location
{
    private String _resourceUrlString;
    private int    _lineNumber;

    public Location(String urlStr, int lineNum)
    {
        _resourceUrlString = urlStr;
        _lineNumber = lineNum;
    }

    public String toString()
    {
        return "[Resource: " + _resourceUrlString + ", Line: " + _lineNumber + "]";
    }

    public boolean equals(Object obj)
    {
        if (obj == this)
            return true;

        if (!(obj instanceof Location))
            return false;

        Location loc = (Location) obj;

        boolean rsrcIsEqual = _resourceUrlString == null ? loc._resourceUrlString == null
            : _resourceUrlString.equals(loc._resourceUrlString);

        if (rsrcIsEqual && _lineNumber == loc._lineNumber)
            return true;

        return false;
    }

    public int hashCode()
    {
        int result = 17;

        result = 37 * result + (_resourceUrlString == null ? 0 : _resourceUrlString.hashCode());

        result = 37 * result + _lineNumber;

        return result;
    }
}