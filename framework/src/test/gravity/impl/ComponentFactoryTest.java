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
import gravity.RealizableComponent;
import gravity.mocks.MockComboServiceImpl;

import org.easymock.MockControl;

/**
 * @author Harish Krishnaswamy
 * @version $Id: ComponentFactoryTest.java,v 1.1 2004-09-02 04:20:56 harishkswamy Exp $
 */
public class ComponentFactoryTest extends GravityTestCase
{
    private ComponentFactory   _factory = new ComponentFactory();
    private MockControl        _proxyableCompCtrl;
    private RealizableComponent _proxyableComp;

    public void setUp()
    {
        _proxyableCompCtrl = MockControl.createStrictControl(RealizableComponent.class);
        _proxyableComp = (RealizableComponent) _proxyableCompCtrl.getMock();
    }

    public void testUnregisteredComponent()
    {
        try
        {
            _factory.newInstance(_proxyableComp);

            unreachable();
        }
        catch (Exception e)
        {
            assertSuperString(e, "Neither implementation nor factory registered for component: "
                + _proxyableComp);
        }
    }

    public void testComponentConfigError()
    {
        _factory.registerImplementation(MockComboServiceImpl.class, null, null);

        try
        {
            _factory.newInstance(_proxyableComp);
        }
        catch (Exception e)
        {
            assertSuperString(e, "Unable to construct new instance for component: "
                + _proxyableComp);
        }
    }
}