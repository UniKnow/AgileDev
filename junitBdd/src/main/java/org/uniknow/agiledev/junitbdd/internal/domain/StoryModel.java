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

import static org.uniknow.agiledev.junitbdd.internal.BDDStringUtils.*;

import java.io.*;
import java.util.*;

import org.junit.runner.*;

import org.springframework.validation.annotation.Validated;
import org.uniknow.agiledev.junitbdd.Narrative;
import org.uniknow.agiledev.junitbdd.internal.ParseException;
import org.uniknow.agiledev.junitbdd.internal.domain.ScenarioModel.ScenarioStatus;
import org.uniknow.agiledev.junitbdd.internal.writers.Keyword;

import javax.validation.constraints.NotNull;

@Validated
public class StoryModel implements WithText {

    /**
     * Contains name of story
     */
    private String name;

    /**
     * Optional short, introductory section of Story
     */
    private NarrativeModel narrative;

    /**
     * Contains what type of report should be generated, default is none
     */
    private String report = "none";

    /**
     * Contains scenarios which are covered by this story
     */
    @NotNull
    private final List<ScenarioModel> scenarios = new ArrayList();

    private String packageName = "";

    public StoryModel() {
    }

    public StoryModel(String name, String report) {
        this.name = name.trim();
        this.report = report.trim();
    }

    public String name() {
        return name;
    }

    public String report() {
        return report;
    }

    public StoryModel withPackage(String packageName) {
        this.packageName = packageName;
        return this;
    }

    /**
     * Returns list of scenarios which are covered by this story
     * 
     * @return list of scenarios which are covered by this story
     */
    @NotNull
    public List<ScenarioModel> scenarios() {
        return scenarios;
    }

    public String asClassPath() {
        if (!"".equals(packageName))
            return packageName.replace('.', '/') + "/" + camelisedName();
        else
            return camelisedName();
    }

    public void addScenario(ScenarioModel scenario) {
        if (scenarios.contains(scenario))
            throw new ParseException("Two scenarios with the same name: "
                + scenario.name());

        scenarios.add(scenario);
    }

    public ScenarioStatus status() {
        for (ScenarioModel scenario : scenarios) {
            if (scenario.status() != ScenarioStatus.PASSED)
                return scenario.status();
        }
        return ScenarioStatus.PASSED;
    }

    public static StoryModel createFrom(String firstLine) throws IOException {
        StoryModel story = new StoryModel();
        story.parseNameFrom(firstLine);
        return story;
    }

    private void parseNameFrom(String firstLine) throws IOException {
        checkIfLineContainsStory(firstLine);
        String storyName = retrieveStoryFromLine(firstLine);
        checkIfStoryNotEmpty(storyName);
        name = storyName;
    }

    private void checkIfStoryNotEmpty(String storyName) {
        if (storyName.isEmpty())
            throw new IllegalArgumentException("Story is empty");
    }

    private String retrieveStoryFromLine(String firstLine) {
        return firstLine.trim().substring(firstLine.indexOf(':') + 1).trim();
    }

    private void checkIfLineContainsStory(String firstLine) {
        if (firstLine == null
            || firstLine.trim().isEmpty()
            || !(firstLine.trim().startsWith("Story:") || firstLine.trim()
                .startsWith("Feature:")))
            throw new IllegalArgumentException("Scenarios have no story");
    }

    public String camelisedName() {
        String result = removeSpecialCharacters(name);
        return camelise(result);
    }

    @Override
    public boolean equals(Object obj) {
        StoryModel other = (StoryModel) obj;
        if (namesNotSame(other))
            return false;
        return (other.scenarios.containsAll(scenarios) && scenarios
            .containsAll(other.scenarios));
    }

    private boolean namesNotSame(StoryModel other) {
        return !((name == null && other.name == null) || name
            .equals(other.name));
    }

    @Override
    public String toString() {
        return "Story: " + name + "; Scenarios:" + scenarios;
    }

    public ScenarioModel scenarioDescribedBy(Description description) {
        for (ScenarioModel scenario : scenarios)
            if (scenario.description() == description)
                return scenario;
        return null;
    }

    public RunStatistics runStatistics() {
        int[] stats = new int[] { 0, 0, 0 };
        for (ScenarioModel scenario : scenarios)
            if (scenario.status() != null)
                stats[scenario.status().ordinal()]++;

        return RunStatistics.fromArray(stats);
    }

    public String packageName() {
        return packageName;
    }

    @Override
    public StoryModel withText(String text) {
        this.name = text;
        return this;
    }

    public void withNarrative(Narrative narrative) {
        this.narrative = new NarrativeModel(narrative.as(), narrative.iWant(),
            narrative.soThat());
    }

    public String as() {
        if (narrative == null) {
            return "";
        } else {
            return Keyword.keyword("As") + " " + narrative.as();
        }
    }

    public String iWant() {
        if (narrative == null) {
            return "";
        } else {
            return Keyword.keyword("I_want") + " " + narrative.iWant();
        }
    }

    public String soThat() {
        if (narrative == null) {
            return "";
        } else {
            return Keyword.keyword("So_that") + " " + narrative.soThat();
        }
    }

    private String addCommaTo(String narrative) {
        if (!"".equals(narrative))
            narrative += ", ";
        return narrative;
    }

    public boolean hasNarrative() {
        return narrative != null;
    }
}
