/**
 * Copyright (C) 2014 uniknow. All rights reserved.
 * 
 * This Java class is subject of the following restrictions:
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * 3. The end-user documentation included with the redistribution, if any, must
 * include the following acknowledgment: "This product includes software
 * developed by uniknow." Alternately, this acknowledgment may appear in the
 * software itself, if and wherever such third-party acknowledgments normally
 * appear.
 * 
 * 4. The name ''uniknow'' must not be used to endorse or promote products
 * derived from this software without prior written permission.
 * 
 * 5. Products derived from this software may not be called ''UniKnow'', nor may
 * ''uniknow'' appear in their name, without prior written permission of
 * uniknow.
 * 
 * THIS SOFTWARE IS PROVIDED ''AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL WWS OR ITS
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.uniknow.agiledev.junitbdd;

import org.junit.runners.model.*;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.statements.*;

/**
 * This is a JUnitBDD runner for Spring. If you want to have spring-aware
 * JUnitBDD scenarios, use this runner instead of the standard BDDRunner.
 * 
 * You can use the same Spring annotations as with the SpringJUnit4ClassRunner,
 * while being able to take advantage of the whole JUnitBDD, including
 * parameters.
 * 
 * @author Pawel Lipinski
 * 
 */
public class SpringBDDRunner extends BDDRunner {

    private final TestContextManager testContextManager;

    public SpringBDDRunner(Class<?> klass) throws InitializationError {
        super(klass);
        this.testContextManager = new TestContextManager(klass);
    }

    @Override
    protected Statement withBeforeClasses(Statement statement) {
        Statement junitBeforeClasses = super.withBeforeClasses(statement);
        return new RunBeforeTestClassCallbacks(junitBeforeClasses,
            testContextManager);
    }

    @Override
    protected Statement withAfterClasses(Statement statement) {
        Statement junitAfterClasses = super.withAfterClasses(statement);
        return new RunAfterTestClassCallbacks(junitAfterClasses,
            testContextManager);
    }

    @Override
    protected Object createTest() throws Exception {
        Object testInstance = super.createTest();
        testContextManager.prepareTestInstance(testInstance);
        return testInstance;
    }

    @Override
    protected Statement withBefores(FrameworkMethod frameworkMethod,
        Object testInstance, Statement statement) {
        Statement junitBefores = super.withBefores(frameworkMethod,
            testInstance, statement);
        return new RunBeforeTestMethodCallbacks(junitBefores, testInstance,
            frameworkMethod.getMethod(), testContextManager);
    }

    @Override
    protected Statement withAfters(FrameworkMethod frameworkMethod,
        Object testInstance, Statement statement) {
        Statement junitAfters = super.withAfters(frameworkMethod, testInstance,
            statement);
        return new RunAfterTestMethodCallbacks(junitAfters, testInstance,
            frameworkMethod.getMethod(), testContextManager);
    }
}
