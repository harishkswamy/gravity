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
 * @version $Id: Location.java,v 1.1 2004-05-10 17:29:02 harishkswamy Exp $
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
}