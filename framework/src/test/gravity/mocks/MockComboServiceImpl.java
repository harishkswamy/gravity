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

/**
 * @author Harish Krishnaswamy
 * @version $Id: MockComboServiceImpl.java,v 1.4 2004-09-02 04:20:58 harishkswamy Exp $
 */
public class MockComboServiceImpl implements MockComboService
{
    int          _id;
    List         _list;
    MockService  _mockService;
    Object       _object;
    int          _primitive;
    volatile int _hashCode;
    int          _counter;

    public MockComboServiceImpl(int id, List list)
    {
        _id = id;
        _list = list;
    }

    public int getId()
    {
        return _id;
    }

    public List getList()
    {
        return _list;
    }

    public Object getObject()
    {
        return _object;
    }

    public void setObject(Object object)
    {
        _object = object;
    }

    public void service()
    {
        _counter++;
    }
    
    public int getCounter()
    {
        return _counter;
    }

    public void setMockService(MockService service)
    {
        _mockService = service;
    }

    public void setPrimitive(int i)
    {
        _primitive = i;
    }

    public MockService getMockService()
    {
        return _mockService;
    }

    public int getPrimitive()
    {
        return _primitive;
    }

    public void setId(int id)
    {
        _id = id;
    }

    public void setList(List list)
    {
        _list = list;
    }
}