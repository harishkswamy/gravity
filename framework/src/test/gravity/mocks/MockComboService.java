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

/**
 * @author Harish Krishnaswamy
 * @version $Id: MockComboService.java,v 1.3 2004-09-02 04:20:58 harishkswamy Exp $
 */
public interface MockComboService
{
    public void service();

    public MockService getMockService();

    public Object getObject();

    public void setObject(Object object);

    public int getCounter();
}