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

import gravity.ComponentState;
import gravity.ProxyableComponent;
import gravity.util.ThreadEvent;
import junit.framework.TestCase;

import org.easymock.MockControl;

/**
 * @author Harish Krishnaswamy
 * @version $Id: ThreadLocalComponentStateTest.java,v 1.1 2004-05-20 11:46:53 harishkswamy Exp $
 */
public class ThreadLocalComponentStateTest extends TestCase
{
    private ThreadLocalComponentState _state;
    private ProxyableComponent        _mockComp;
    private MockControl               _compControl;
    private ComponentState            _mockState;
    private MockControl               _stateControl;

    private void setUpMockComp()
    {
        _compControl = MockControl.createStrictControl(ProxyableComponent.class);
        _mockComp = (ProxyableComponent) _compControl.getMock();
    }

    private void setUpState()
    {
        setUpMockComp();

        _state = new ThreadLocalComponentState(null, _mockComp);
    }

    public void setUpDecoratedState()
    {
        setUpMockComp();

        _stateControl = MockControl.createStrictControl(ComponentState.class);
        _mockState = (ComponentState) _stateControl.getMock();

        _state = new ThreadLocalComponentState(_mockState, _mockComp);
    }
    
    public void tearDown()
    {
        ThreadEvent.getInstance().cleanUp();
    }

    public void testGetConcreteComponentInstance()
    {
        setUpState();

        _mockComp.newInstance();
        Object compInst = new Object();
        _compControl.setReturnValue(compInst, 1);

        _compControl.replay();

        Object rtnInst1 = _state.getConcreteComponentInstance();
        Object rtnInst2 = _state.getConcreteComponentInstance();

        _compControl.verify();

        assertSame(compInst, rtnInst1);
        assertSame(compInst, rtnInst2);
    }

    public void testGetConcreteComponentInstanceWhenDecorated()
    {
        setUpDecoratedState();

        _compControl.replay();

        _mockState.getConcreteComponentInstance();
        Object compInst = new Object();
        _stateControl.setReturnValue(compInst, 1);

        _stateControl.replay();

        Object rtnInst1 = _state.getConcreteComponentInstance();
        Object rtnInst2 = _state.getConcreteComponentInstance();

        _compControl.verify();
        _stateControl.verify();

        assertSame(compInst, rtnInst1);
        assertSame(compInst, rtnInst2);
    }

    public void testHandleThreadPreTermination()
    {
        setUpState();

        _compControl.replay();

        _state.handleThreadPreTermination();

        _compControl.verify();
    }

    public void testHandleThreadPreTerminationWhenDecorated()
    {
        setUpDecoratedState();

        _compControl.replay();

        _mockState.collectComponentInstance(null);
        
        Object compInst = new Object();
        _stateControl.expectAndReturn(_mockState.getConcreteComponentInstance(), compInst, 1);

        _mockState.collectComponentInstance(compInst);

        _stateControl.replay();

        _state.handleThreadPreTermination();
        _state.getConcreteComponentInstance();
        _state.handleThreadPreTermination();

        _compControl.verify();
        _stateControl.verify();
    }
}