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

package gravity.mocks;

import java.util.List;
import java.util.Map;

/**
 * @author Harish Krishnaswamy
 * @version $Id: MockServiceImpl.java,v 1.1 2004-05-10 17:28:48 harishkswamy Exp $
 */
public class MockServiceImpl implements MockService
{
    public MockServiceImpl(MockService2 service, List listConfig, Map mapConfig)
    {

    }

    public void service()
    {

    }

    Object _configObject;

    public void setConfigObject(Object obj)
    {
        _configObject = obj;
    }

    public Object getConfigObject()
    {
        return _configObject;
    }

    private int _primitive;

    public int getPrimitive()
    {
        return _primitive;
    }

    public void setPrimitive(int primitive)
    {
        _primitive = primitive;
    }
}