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

import static org.uniknow.agiledev.junitbdd.internal.BDDStringUtils.*;

import javax.validation.constraints.NotNull;

import java.util.*;

import junitparams.*;

import org.junit.runner.*;
import org.junit.runner.notification.*;

import org.uniknow.agiledev.dbc4java.Validated;
import org.uniknow.agiledev.junitbdd.Narrative;
import org.uniknow.agiledev.junitbdd.Scenario;
import org.uniknow.agiledev.junitbdd.Story;
import org.uniknow.agiledev.junitbdd.internal.domain.*;
import org.uniknow.agiledev.junitbdd.internal.domain.ScenarioModel.ScenarioStatus;
import org.uniknow.agiledev.junitbdd.internal.writers.*;

public class ScenarioListener extends RunListener {

    private StoryModel story;
    private static List<StoryModel> storiesList = new LinkedList<StoryModel>();
    private StoryFileWriter fileWriter;

    @Override
    public void testRunStarted(Description description) throws Exception {
        createStory(description);
    }

    private void createStory(Description description) {
        if (description.getAnnotation(Story.class) != null)
            createStoryFromAnnotation(description.getAnnotation(Story.class));
        else {
            createStoryFromDescription(description.getDisplayName());
        }

        // Check whether Narrative is defined for story and if so append to
        // story
        Narrative narrative = description.getAnnotation(Narrative.class);
        if (narrative != null) {
            getStory().withNarrative(narrative);
        }
    }

    @Override
    public void testRunFinished(Result result) throws Exception {
        setScenariosStatuses(result);
        updateStoriesList();
        generateReport();
    }

    private void updateStoriesList() {
        storiesList.add(getStory());
    }

    private void generateReport() {
        fileWriter = null;
        if ("html".equals(shouldGenerateFile()))
            fileWriter = new FreemarkerFileWriter();

        if (fileWriter != null) {
            fileWriter.setOutputFolder(System.getProperty("outputFolder",
                "target/JUnitBDD"));
            fileWriter.write(story);
            fileWriter.writeToc(storiesList);
        }
    }

    private void setScenariosStatuses(@NotNull Result result) {
        setAllFailedScenariosToStatusFailed(result);
        setAllPassedAndPendingScenariosToProperStatus();
    }

    private void setAllPassedAndPendingScenariosToProperStatus() {
        for (ScenarioModel scenario : story.scenarios()) {
            setStatusToPassedIfNotFailed(scenario);
            setStatusToPendingIfAnnotatedAsPending(scenario);
        }
    }

    private void setAllFailedScenariosToStatusFailed(@NotNull Result result) {
        for (Failure failure : result.getFailures())
            story.scenarioDescribedBy(failure.getDescription()).withStatus(
                ScenarioStatus.FAILED.withDetails(failure.getException()));
    }

    private void setStatusToPendingIfAnnotatedAsPending(ScenarioModel scenario) {
        Scenario scenarioAnnotation = scenario.description().getAnnotation(
            Scenario.class);
        if (scenarioAnnotation != null && scenarioAnnotation.pending())
            scenario.withStatus(ScenarioStatus.PENDING);
    }

    private void setStatusToPassedIfNotFailed(ScenarioModel scenario) {
        if (scenario.status() == null)
            scenario.withStatus(ScenarioStatus.PASSED);
    }

    private String shouldGenerateFile() {
        // return System.getProperty(GENERATE_FILES);
        return story.report();
    }

    @Override
    public void testStarted(Description description) throws Exception {
        if (isItSingleScenario(description)) {
            ScenarioManager.startScenario();
            ScenarioManager.currentScenario().withStory(story);
        }
    }

    private boolean isItSingleScenario(Description description) {
        try {
            return description.getMethodName() != null;
        } catch (NoSuchMethodError e) { // junit 4.5
            return description.getDisplayName() != null;
        }
    }

    @Override
    public void testFinished(Description description) throws Exception {
        if (isItSingleScenario(description)) {
            ScenarioModel namedScenario = namedScenario(description);
            story.addScenario(namedScenario);
        }
    }

    private ScenarioModel namedScenario(Description description) {
        Scenario scenarioAnnotation = description.getAnnotation(Scenario.class);
        Parameters parametersAnnotation = description
            .getAnnotation(Parameters.class);
        String name = null;

        if (scenarioHasDefinedName(scenarioAnnotation))
            name = scenarioAnnotation.value();
        else
            name = createScenarioNameFromTestMethodName(description,
                parametersAnnotation);

        if (parametersAnnotation != null && !scenarioAnnotation.pending())
            name = name.substring(name.indexOf('(') + 1, name.indexOf(')'))
                + " ("
                + description.getMethodName().substring(0,
                    description.getMethodName().indexOf('(') - 1) + ")";

        return ScenarioManager.currentScenario().withName(name)
            .withDescription(description);
    }

    private String createScenarioNameFromTestMethodName(
        Description description, Parameters parametersAnnotation) {
        try {
            return decamelise(description.getMethodName());
        } catch (NoSuchMethodError e) { // junit 4.5
            return decamelise(description.getDisplayName());
        }
    }

    private boolean scenarioHasDefinedName(Scenario scenarioAnnotation) {
        return scenarioAnnotation != null && scenarioAnnotation.value() != null
            && !scenarioAnnotation.value().equals("");
    };

    void createStoryFromAnnotation(Story behaviours) {
        story = new StoryModel(behaviours.value(), behaviours.report());
    }

    void createStoryFromDescription(String className) {
        if (className.contains(" should")) {
            className = className.substring(0, className.indexOf(" should"));
        } else {
            className = removeSuffixFromClassName(className, "Test");
            className = removeSuffixFromClassName(className, "Scenarios");
            className = removePackage(className);
        }
        story = new StoryModel(BDDStringUtils.decamelise(className), "none");
    }

    private String removePackage(String className) {
        if (className.contains("."))
            return className.substring(className.lastIndexOf('.') + 1);
        return className;
    }

    private String removeSuffixFromClassName(String className, String suffix) {
        if (className.endsWith(suffix))
            className = className.substring(0, className.indexOf(suffix));
        return className;
    };

    public StoryModel getStory() {
        return story;
    }

    public List<StoryModel> storiesList() {
        return storiesList;
    }

    public StoryFileWriter getWriter() {
        return fileWriter;
    }
}