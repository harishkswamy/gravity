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

package gravity.impl;

import gravity.GravityTestCase;

/**
 * @author Harish Krishnaswamy
 * @version $Id: DispatchingComponentStrategyTest.java,v 1.1 2004-09-02 04:20:56 harishkswamy Exp $
 */
public class DispatchingComponentStrategyTest extends GravityTestCase
{
    private DispatchingComponentStrategy _strategy;
    
    public void setUp()
    {
        _strategy = new DispatchingComponentStrategy(null);
    }
    
    public void testToString()
    {
        assertEquals(_strategy.toString(), " [Dispatching] ");
    }
}