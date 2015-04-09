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
package org.uniknow.agiledev.junitbdd.internal.domain;

import junitparams.Parameters;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.uniknow.agiledev.junitbdd.BDDRunner;
import org.uniknow.agiledev.junitbdd.Scenario;
import org.uniknow.agiledev.junitbdd.Story;

import static org.junit.Assert.*;
import static org.uniknow.agiledev.junitbdd.BDD.*;

/**
 * Created by mase on 17-08-14.
 */
@RunWith(BDDRunner.class)
@Story(value = "Narrative Model Test", report = "html")
public class NarrativeModelTest {

    /**
     * Verifies IllegalArgumentException is thrown when passed value of argument
     * as is null
     * 
     * Currently ignored due to https://github.com/UniKnow/AgileDev/issues/2
     */
    @Ignore
    @Scenario
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorArgumentAsNull() {
        When("instantiating NarrativeModel instance with value of argument as is null");
        Then("IllegalArgumentException is thrown");
        new NarrativeModel(null, "i want", "so that");
    }

    /**
     * Verifies IllegalArgumentException is thrown when passed value of argument
     * iWant is null
     * 
     * Currently ignored due to https://github.com/UniKnow/AgileDev/issues/2
     */
    @Ignore
    @Scenario
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorArgumentIWantNull() {
        When("instantiating NarrativeModel instance with value of argument iWant is null");
        Then("IllegalArgumentException is thrown");
        new NarrativeModel("as", null, "so that");
    }

    /**
     * Verifies IllegalArgumentException is thrown when passed value of argument
     * soThat is null
     * 
     * Currently ignored due to https://github.com/UniKnow/AgileDev/issues/2
     */
    @Ignore
    @Scenario
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorArgumentSoThatNull() {
        When("instantiating NarrativeModel instance with value of argument soThat is null");
        Then("IllegalArgumentException is thrown");
        new NarrativeModel("as", "i want", null);
    }

    /**
     * Verifies constructor initializes instance correctly
     */
    @Scenario
    @Test
    @Parameters({ "Requested,To Achieve, Reason" })
    public void testConstructor(String as, String iWant, String soThat) {
        Given("argument as containing " + as);
        And("argument iWant containing " + iWant);
        And("argument soThat containing " + soThat);

        When("instantiating new instance of NarrativeModel");
        NarrativeModel model = new NarrativeModel(as, iWant, soThat);

        Then("Instance of type NarrativeModel should be created");
        assertNotNull(model);
        And("value of property as should be value " + as);
        assertEquals(as, model.as());
        And("value of property iWant should be " + iWant);
        assertEquals(iWant, model.iWant());
        And("value of property soThat should be " + soThat);
        assertEquals(soThat, model.soThat());
    }
}
