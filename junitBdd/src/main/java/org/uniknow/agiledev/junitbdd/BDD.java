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

import static org.uniknow.agiledev.junitbdd.internal.ScenarioManager.*;
import org.uniknow.agiledev.junitbdd.internal.domain.*;

/**
 * <h1>JUnitBDD</h1><br/>
 * <p>
 * JUnitBDD is a library which will help you do the same with your code. It will
 * lead the way you think about and build your code, as well as help you
 * remember what should be implemented and when. As a side-effect you'll be able
 * to impress your customer with always up-to-date reports of what has already
 * been implemented (and whether it works or not), and what's still pending.
 * </p>
 * <br/>
 * <h2>Contents</h2> <b><a href="#1">1. Introduction</a><br/>
 * <a href="#2">2. Usage example</a><br/>
 * <a href="#3">3. Generating Java from scenarios</a><br/>
 * <a href="#4">4. Generating reports from Java</a><br/>
 * <a href="#5">5. Integration with JUnit</a><br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#5a">&gt;=4.5</a><br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#5b">4.4</a><br/>
 * <a href="#6">6. Annotating stories and scenarios</a><br/>
 * <a href="#7">7. Parameterised scenarios</a><br/>
 * <a href="#8">8. Integration with Spring</a><br/>
 * </b><br/>
 * <h3 id="1">1. Introduction</h3>
 * <p>
 * JUnitBDD is a Behaviour-Driven Development library. It supports BDD-like way
 * of thinking about the application you're building. No longer do you write
 * tests and test methods. Now you're thinking about a <b>Story</b>
 * (functionality) in terms of <b>Scenarios</b> of its use/work. All your
 * scenarios start with a magical word <b>should</b> which means, that we're
 * going to say what should happen in a given story.
 * </p>
 * <h3 id="2">2. Usage example</h3>
 * <p>
 * Let's give a real-life example. I have a library. I can lend books and accept
 * them back. Let me specify this functionality in terms of scenarios with the
 * following description:<br/>
 * 
 * <pre>
 *  Story: Library
 *    As a library user
 *    In order to borrow a book
 *    I want librarian to give me that book
 *    So that I can take it home
 * 
 *    Scenario: lend an existing book from the library
 *        Given 'Children from Bullerbyn' book in the library
 *           And a pretty librarian.
 *        When this book is borrowed from the library
 *           And the librarian is blinking at you
 *        Then the library doesn't contain it anymore
 *           And the librarian wants to go out with you
 *           But you're already married, so no way.
 * 
 *    Scenario: not lend a nonexisting book from the library
 *        Given empty library
 *        When we try to borrow 'Children from Bullerbyn' from the library
 *        Then the library doesn't let it to be borrowed.
 * 
 *    Scenario: accept back a book previously borrowed
 *        Given 'Children from Bullerbyn' has been borrowed from the library
 *        When this book is given back
 *        Then the library contains it.
 * </pre>
 * 
 * <br/>
 * You can represent exactly this way of thinking about your functionalities in
 * a form of JUnitBDD scenarios:<br/>
 * <img src="doc-files/java-examples.gif"/>
 * </p>
 * <p>
 * You can see that we're using a <code>BDDRunner</code> to run scenarios with
 * JUnit, define a <code>Story</code> with the proper name, and specify all
 * <code>Scenarios</code> as pending - meaning that they have not been
 * implemented yet, so should be ignored during execution.<br/>
 * All scenarios have names that start with the <b>should</b> word, and contain
 * <code>Given</code>, <code>When</code> and <code>Then</code> sections which
 * define: initial state for the scenario, action to be performed and expected
 * output accordingly. The only thing that's lacking is... the scenario
 * implementation.
 * </p>
 * <p>
 * Let me quickly show you how a sample implementation of one of the methods
 * could look like:<br/>
 * <img src="doc-files/java-example.gif"/>
 * </p>
 * <p>
 * See? Not only do you have a documentation of your functionalities, but you
 * just got an acceptance-testing framework!
 * </p>
 * <p>
 * You can see, that I now removed the <code>pending = true</code> from the
 * <code>@Scenario</code> annotation - that's because the scenario has its
 * implementation and the it should be failing/red now, so that you can produce
 * production code that realises this scenario and makes it passing/green.
 * </p>
 * <h3 id="4a">4a. Generating reports from a Maven build</h3>
 * <p>
 * You can also generate reports automatically in your maven build with this
 * surefire plugin configuration to your pom.xml: <code>
 *  &lt;plugin&gt;
 *     &lt;groupId&gt;org.apache.maven.plugins&lt;/groupId&gt;
 *     &lt;artifactId&gt;maven-surefire-plugin&lt;/artifactId&gt;
 *     &lt;configuration&gt;
 *         &lt;useFile&gt;false&lt;/useFile&gt;
 *         &lt;systemProperties&gt;
 *             &lt;property&gt;
 *                 &lt;name&gt;generateReport&lt;/name&gt;
 *                 &lt;value&gt;html&lt;/value&gt;
 *             &lt;/property&gt;
 *         &lt;/systemProperties&gt;
 *         &lt;includes&gt;
 *             &lt;include&gt;**\/*Scenarios.java&lt;/include&gt;
 *         &lt;/includes&gt;
 *     &lt;/configuration&gt;
 * &lt;/plugin&gt;
 * </code>
 * </p>
 * <h3 id="6">6. Annotating Stories and Scenarios</h3>
 * <p>
 * You don't need to annotate stories in your Java code. The story name is
 * automatically resolved from the Java class name. Still, you may want to use
 * some special characters, or simply have your code more intention-revealing
 * (that's always a good thing to have!). In such case just annotate your
 * scenarios class with:
 * 
 * <pre>
 * &#064;Story(&quot;Some story name&quot;)
 * </pre>
 * 
 * Scenarios must be annotated - in the end JUnitBDD must know which methods are
 * scenarios and which serve some other purpose. So each scenario method must be
 * annotated with <code>@Scenario</code>. Scenarios can have full-text names
 * different to method names. Just give the name as the annotation value like
 * this:
 * 
 * <pre>
 * &#064;Scenario(&quot;Some name other than the method name&quot;)
 * </pre>
 * 
 * Scenarios can also be set to <i>pending</i> state, which means that their
 * implementation has not been finished yet. In such cases annotate such
 * scenarios with:
 * 
 * <pre>
 * &#064;Scenario(pending = true)
 * </pre>
 * 
 * This is also the way they always get generated using
 * <code>ScenariosToJavaConverter</code> since obviously all generated scenarios
 * need proper implementation first.
 * </p>
 * <h3 id="8">8. Integration with Spring</h3>
 * <p>
 * You can easily use JUnitBDD together with Spring. The only problem is that
 * Spring's test framework is based on JUnit runners, and JUnit allows only one
 * runner to be run at once. Which would normally mean that you could use only
 * one of Spring or JUnitBDD. Luckily we can cheat Spring a little by adding
 * this to your test class:
 * 
 * <pre>
 * private TestContextManager testContextManager;
 * 
 * &#064;Before
 * public void init() throws Exception {
 *     this.testContextManager = new TestContextManager(getClass());
 *     this.testContextManager.prepareTestInstance(this);
 * }
 * </pre>
 * 
 * This lets you use in your tests anything that Spring provides in its test
 * framework.
 * </p>
 * 
 * 
 * @author mase
 */
public class BDD {
    /**
     * 'Given' section of a scenario
     * 
     * @param description
     *            description of what is given
     */
    public static void Given(String description) {
        currentScenario().withGiven(description);
    }

    /**
     * 'And' section of a scenario
     * 
     * @param description
     *            description of and
     */
    public static void And(String description) {
        currentScenario().withAnd(description);
    }

    /**
     * 'When' section of a scenario
     * 
     * @param description
     *            description of action being performed
     */
    public static void When(String description) {
        currentScenario().withWhen(description);
    }

    /**
     * 'Then' section of a scenario
     * 
     * @param description
     *            description of expected effect / state
     */
    public static void Then(String description) {
        currentScenario().withThen(description);
    }

}
