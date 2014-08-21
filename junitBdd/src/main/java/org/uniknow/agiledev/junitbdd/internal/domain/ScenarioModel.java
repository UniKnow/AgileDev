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

import java.util.*;

import net.sf.oval.constraint.NotNull;
import org.junit.runner.*;

public class ScenarioModel {

    public enum ScenarioStatus {
        PASSED("Scenario passed"), FAILED(new Exception("")), PENDING(
            "Not implemented yet");

        private Object details;

        ScenarioStatus(Object details) {
            this.details = details;
        }

        public Object details() {
            return details;
        }

        public ScenarioStatus withDetails(Object details) {
            this.details = details;
            return this;
        }
    }

    private String name;
    private List<StepBasedModel> steps = new ArrayList<StepBasedModel>();
    private StoryModel story;
    private ScenarioStatus status;
    private Description description;

    public ScenarioModel(String name) {
        this.name = name.trim();
    }

    public String name() {
        return name;
    }

    public String given() {
        if (givenStep() == null)
            return "";
        return processStory(givenStep().text());
    }

    public GivenModel givenStep() {
        return stepOfType(GivenModel.class);
    }

    public String when() {
        if (whenStep() == null)
            return "";
        return processStory(whenStep().text());
    }

    private StepBasedModel whenStep() {
        return stepOfType(WhenModel.class);
    }

    public String then() {
        if (thenStep() == null)
            return "";
        return processStory(thenStep().text());
    }

    private StepBasedModel thenStep() {
        return stepOfType(ThenModel.class);
    }

    @SuppressWarnings("unchecked")
    private <T extends StepBasedModel> T stepOfType(Class<T> stepClass) {
        for (StepBasedModel step : steps)
            if (step.getClass().equals(stepClass))
                return (T) step;
        return null;
    }

    private String processStory(String text) {
        if (text == null)
            return "";

        return text;
    }

    public ScenarioModel withName(String name) {
        this.name = name.trim();
        return this;
    }

    public ScenarioModel withGiven(String given) {
        if (givenStep() == null)
            steps.add(new GivenModel(given));
        return this;
    }

    public ScenarioModel withAnd(String and) {
        steps.add(new AndModel(and));
        return this;
    }

    public ScenarioModel withWhen(String when) {
        if (whenStep() == null)
            steps.add(new WhenModel(when));
        return this;
    }

    public ScenarioModel withThen(String then) {
        if (thenStep() == null)
            steps.add(new ThenModel(then));
        return this;
    }

    public ScenarioModel withStatus(ScenarioStatus status) {
        this.status = status;
        return this;
    }

    public ScenarioStatus status() {
        return status;
    }

    @Override
    public boolean equals(Object obj) {
        ScenarioModel other = (ScenarioModel) obj;
        if (other == null)
            return false;
        if ((other.name == null && name != null)
            || (name == null && other.name != null))
            return false;
        if (other.name.toLowerCase().equals(name.toLowerCase()))
            return true;
        return false;
    }

    @Override
    public String toString() {
        return name;
    }

    public ScenarioModel withDescription(Description description) {
        this.description = description;
        return this;
    }

    public Description description() {
        return description;
    }

    public List<StepBasedModel> steps() {
        return steps;
    }

    public StoryModel story() {
        return story;
    }

    public ScenarioModel withStory(StoryModel story) {
        this.story = story;
        return this;
    }
}
