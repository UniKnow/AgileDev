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
package org.uniknow.agiledev.junitbdd.internal;

import static org.junit.Assert.*;
import static org.uniknow.agiledev.junitbdd.BDD.*;
import static org.uniknow.agiledev.junitbdd.internal.BDDStringUtils.*;

import java.io.*;
import java.lang.annotation.*;
import java.util.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runner.notification.*;

import org.uniknow.agiledev.junitbdd.Scenario;
import org.uniknow.agiledev.junitbdd.internal.domain.ScenarioModel.ScenarioStatus;
import org.uniknow.agiledev.junitbdd.internal.domain.*;

public class ScenarioListenerTest {

    private ScenarioListener listener = new ScenarioListener();

    /**
     * Verifies IllegalArgumentException is thrown when passed test results is
     * null
     * 
     * Ignored due to https://github.com/UniKnow/AgileDev/issues/3
     */
    @Ignore
    @Scenario
    @Test(expected = IllegalArgumentException.class)
    public void testRunFinishedResultNull() throws Exception {
        Given("instance of ScenarioListener");
        When("test finishes with test results null");
        Then("IllegalArgumentException is thrown");
        listener.testRunFinished(null);
    }

    @Test
    public void shouldCreateStoryUponTestRunStart() throws Exception {
        listener.testRunStarted(Description
            .createSuiteDescription(StoryStatisticsScenarios.class));

        assertEquals("Story statistics", listener.getStory().name());
        if ("html".equals(listener.getStory().report())) {
            new File("StoryStatisticsScenariosScenarios.html").delete();
            listener.storiesList().remove(listener.getStory());
        }
    }

    // @Test
    // public void shouldCreateFileUponTestRunFinish() throws Exception {
    // Given("running test class and generateReport property set to scenarios");
    // listener.testRunStarted(Description
    // .createSuiteDescription(StoryStatisticsScenarios.class));
    // String originalGenerateReport = System.getProperty("generateReport");
    // System.setProperty("generateReport", "scenarios");
    //
    // When("test class run is finished");
    // listener.testRunFinished(new Result());
    //
    // Then("an scenario file should be created");
    // assertReportExisted("StoryStatistics.scenarios");
    // if (originalGenerateReport != null) {
    // System.setProperty("generateReport", originalGenerateReport);
    // new File("StoryStatisticsScenariosScenarios.html").delete();
    // listener.storiesList().remove(listener.getStory());
    // } else
    // System.clearProperty("generateReport");
    // deleteReportFolder();
    // }

    @Test
    public void shouldCreateScenarioUponTestStart() throws Exception {
        Given("no current scenario in scenario manager");
        ScenarioManager.cleanScenario();

        When("test is started");
        Description description = Description
            .createTestDescription(StoryStatisticsScenarios.class,
                "shouldCreateStoryUponTestRunStart");
        listener.testStarted(description);

        Then("scenario manager should have a current scenario");
        assertNotNull(ScenarioManager.currentScenario());
        new File("StoryStatisticsScenariosScenarios.html").delete();
        if (System.getProperty("generateReport") != null) {
            new File("StoryStatisticsScenariosScenarios.html").delete();
            listener.storiesList().remove(listener.getStory());
        }
    }

    @Test
    public void shouldAddScenarioToStoryUponTestFinish() throws Exception {
        Given("listener with story and with started scenario");
        String testName = "shouldCreateStoryUponTestRunStart";
        listener.testRunStarted(Description
            .createSuiteDescription(StoryStatisticsScenarios.class));
        listener.testStarted(Description.createTestDescription(
            StoryStatisticsScenarios.class, testName));

        When("test is finished");
        listener.testFinished(Description.createTestDescription(
            StoryStatisticsScenarios.class, testName));

        Then("story should have the scenario");
        assertEquals(decamelise(testName),
            listener.getStory().scenarios().get(0).name());
        if (System.getProperty("generateReport") != null) {
            new File("StoryStatisticsScenariosScenarios.html").delete();
            listener.storiesList().remove(listener.getStory());
        }
    }

    @Test
    public void shouldSetPendingStatusInScenarioIfAnnotatedAsPending()
        throws Exception {
        Given("description for pending scenario");
        Description description = Description.createTestDescription(
            StoryStatisticsScenarios.class, "shouldBePending", new Scenario() {
                @Override
                public boolean pending() {
                    return true;
                }

                @Override
                public String value() {
                    return null;
                }

                @Override
                public Class<? extends Annotation> annotationType() {
                    return Scenario.class;
                }

                @Override
                public Class<? extends Throwable> expected() {
                    return Scenario.None.class;
                }
            });
        ScenarioManager.cleanScenario();
        listener.testRunStarted(description);

        When("test is finished and test run is finished");
        listener.testFinished(description);
        listener.testRunFinished(new Result());

        Then("scenario status is " + ScenarioStatus.PENDING);
        assertEquals(ScenarioStatus.PENDING, listener.getStory().scenarios()
            .get(0).status());
        if (System.getProperty("generateReport") != null) {
            new File("StoryStatisticsScenariosScenarios.html").delete();
            listener.storiesList().remove(listener.getStory());
        }
    }

    @Test
    public void shouldSetPassingStatusInScenarioNotFailed() throws Exception {
        Given("description for pending scenario");
        ScenarioManager.cleanScenario();
        Description description = Description.createTestDescription(
            StoryStatisticsScenarios.class, "shouldBePending");
        listener.testRunStarted(description);

        When("test is finished and test run is finished");
        listener.testFinished(description);
        listener.testRunFinished(new Result());

        Then("scenario status is " + ScenarioStatus.PASSED);
        assertEquals(ScenarioStatus.PASSED, listener.getStory().scenarios()
            .get(0).status());
        if (System.getProperty("generateReport") != null) {
            new File("StoryStatisticsScenariosScenarios.html").delete();
            listener.storiesList().remove(listener.getStory());
        }
    }

    @Test
    public void shouldSetFailingStatusInScenarioThatFailed() throws Exception {
        Given("description for pending scenario");
        ScenarioManager.cleanScenario();
        final Description description = Description.createTestDescription(
            StoryStatisticsScenarios.class, "shouldBePending");
        listener.testRunStarted(description);

        When("test is finished and test run is finished");
        listener.testFinished(description);
        listener.testRunFinished(new Result() {
            @Override
            public List<Failure> getFailures() {
                return Arrays.asList(new Failure(description,
                    new RuntimeException("error msg")));
            }
        });

        Then("scenario status is " + ScenarioStatus.FAILED);
        ScenarioStatus status = listener.getStory().scenarios().get(0).status();
        assertEquals(ScenarioStatus.FAILED, status);
        assertEquals("error msg", ((Exception) status.details()).getMessage());
        if (System.getProperty("generateReport") != null) {
            new File("StoryStatisticsScenariosScenarios.html").delete();
            listener.storiesList().remove(listener.getStory());
        }
    }

    @Test
    public void shouldUpdateProcessedStoriesList() throws Exception {
        Given("scenario listener with empty stories list");
        Description description = Description.createTestDescription(
            StoryStatisticsScenarios.class, "shouldBePending");
        listener.testRunStarted(description);

        When("a test is finished (story processed)");
        listener.testRunFinished(new Result());

        Then("scenario list should contain the processed story");
        assertTrue(listener.storiesList().contains(listener.getStory()));
        if (System.getProperty("generateReport") != null) {
            new File("StoryStatisticsScenariosScenarios.html").delete();
            listener.storiesList().remove(listener.getStory());
        }
    }

    // @Test
    // public void shouldGenerateTableOfContentsAtTestFinish() throws Exception
    // {
    // Given("running test class and generateReport property set to scenarios");
    // String originalGenerateReport = System.getProperty("generateReport");
    // System.setProperty("generateReport", "scenarios");
    // listener.testRunStarted(Description
    // .createSuiteDescription(StoryStatisticsScenarios.class));
    //
    // When("test class run is finished");
    // listener.testRunFinished(new Result());
    //
    // Then("an TOC file should be created");
    // assertReportExisted("Stories.toc");
    // if (originalGenerateReport != null) {
    // System.setProperty("generateReport", originalGenerateReport);
    // new File("StoryStatisticsScenariosScenarios.html").delete();
    // listener.storiesList().remove(listener.getStory());
    // } else
    // System.clearProperty("generateReport");
    // deleteReportFolder();
    // }

    // @Test
    // public void shouldGenerateFilesInDefinedOutputFolder() throws Exception {
    // Given("running test class and generateReport property set to scenarios and outputFolder set to 'OutputFolder'");
    // String originalGenerateReport = System.getProperty("generateReport");
    // String originalOutputFolder = System.getProperty("outputFolder");
    // System.setProperty("generateReport", "scenarios");
    // System.setProperty("outputFolder", "OutputFolder");
    // listener.testRunStarted(Description
    // .createSuiteDescription(StoryStatisticsScenarios.class));
    //
    // When("test class run is finished");
    // listener.testRunFinished(new Result());
    //
    // Then("files should be created in 'OutputFolder'");
    // assertReportExisted("Stories.toc", listener.getWriter());
    //
    // if (originalGenerateReport != null) {
    // System.setProperty("generateReport", originalGenerateReport);
    // new File("StoryStatisticsScenariosScenarios.html").delete();
    // listener.storiesList().remove(listener.getStory());
    // } else {
    // System.clearProperty("generateReport");
    // }
    //
    // System.clearProperty("outputFolder");
    // if (originalOutputFolder != null) {
    // System.setProperty("outputFolder", originalOutputFolder);
    // }
    //
    // deleteReportDirectory(listener.getWriter());
    // assertTrue(deleteDirectoryRecursively(new File("OutputFolder")));
    // }
}
