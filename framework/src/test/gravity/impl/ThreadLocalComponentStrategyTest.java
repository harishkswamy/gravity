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

import gravity.ComponentStrategy;
import gravity.RealizableComponent;
import gravity.util.ThreadEvent;
import junit.framework.TestCase;

import org.easymock.MockControl;

/**
 * @author Harish Krishnaswamy
 * @version $Id: ThreadLocalComponentStrategyTest.java,v 1.3 2004-09-02 04:20:56 harishkswamy Exp $
 */
public class ThreadLocalComponentStrategyTest extends TestCase
{
    private ThreadLocalComponentStrategy _strategy;
    private RealizableComponent           _mockComp;
    private MockControl                  _compControl;
    private ComponentStrategy            _mockStrategy;
    private MockControl                  _strategyControl;

    private void setUpMockComp()
    {
        _compControl = MockControl.createStrictControl(RealizableComponent.class);
        _mockComp = (RealizableComponent) _compControl.getMock();
    }

    private void setUpState()
    {
        setUpMockComp();

        _strategy = new ThreadLocalComponentStrategy(null);
    }

    public void setUpDecoratedState()
    {
        setUpMockComp();

        _strategyControl = MockControl.createStrictControl(ComponentStrategy.class);
        _mockStrategy = (ComponentStrategy) _strategyControl.getMock();

        _strategy = new ThreadLocalComponentStrategy(_mockStrategy);
    }

    public void tearDown()
    {
        ThreadEvent.getInstance().cleanUp();
    }

    public void testGetConcreteComponentInstance()
    {
        setUpState();

        Object compInst = new Object();
        _compControl.expectAndReturn(_mockComp.newInstance(), compInst, 1);

        _compControl.replay();

        Object rtnInst1 = _strategy.getComponentInstance(_mockComp);
        Object rtnInst2 = _strategy.getComponentInstance(_mockComp);

        _compControl.verify();

        assertSame(compInst, rtnInst1);
        assertSame(compInst, rtnInst2);
    }

    public void testGetConcreteComponentInstanceWhenDecorated()
    {
        setUpDecoratedState();

        _compControl.replay();

        Object compInst = new Object();
        _strategyControl.expectAndReturn(_mockStrategy.getComponentInstance(_mockComp), compInst,
            1);

        _strategyControl.replay();

        Object rtnInst1 = _strategy.getComponentInstance(_mockComp);
        Object rtnInst2 = _strategy.getComponentInstance(_mockComp);

        _compControl.verify();
        _strategyControl.verify();

        assertSame(compInst, rtnInst1);
        assertSame(compInst, rtnInst2);
    }

    public void testHandleThreadPreTermination()
    {
        setUpState();

        _compControl.replay();

        _strategy.handleThreadPreTermination();

        _compControl.verify();
    }

    public void testHandleThreadPreTerminationWhenDecorated()
    {
        setUpDecoratedState();

        _compControl.replay();

        _mockStrategy.collectComponentInstance(null);

        Object compInst = new Object();
        _strategyControl.expectAndReturn(_mockStrategy.getComponentInstance(_mockComp), compInst,
            1);

        _mockStrategy.collectComponentInstance(compInst);

        _strategyControl.replay();

        _strategy.handleThreadPreTermination();
        _strategy.getComponentInstance(_mockComp);
        _strategy.handleThreadPreTermination();

        _compControl.verify();
        _strategyControl.verify();
    }

    public void testToString()
    {
        setUpState();

        assertEquals(_strategy.toString(), " [Thread Local] ");
    }
}