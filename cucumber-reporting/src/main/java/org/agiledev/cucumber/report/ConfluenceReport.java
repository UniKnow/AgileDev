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
package org.agiledev.cucumber.report;

import gherkin.formatter.model.Scenario;
import cucumber.runtime.CucumberException;
import gherkin.formatter.model.Feature;
import gherkin.formatter.model.Step;
import org.agiledev.cucumber.report.model.FeatureWrapper;
import org.agiledev.cucumber.report.model.ScenarioWrapper;
import org.agiledev.cucumber.report.model.StepWrapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Vector;

/**
 * Report in confluence wiki markup containing Cucumber results
 */
public class ConfluenceReport {

    /**
     * Contains builder by which Confluence documents can be created
     */
    ConfluenceReportBuilder reportBuilder;

    /**
     * Contains the current feature the report is working on
     */
    FeatureWrapper currentFeature;

    /**
     * Contains the current scenario the report is working on
     */
    ScenarioWrapper currentScenario;

    /**
     * Contains the current step cucumber is processing
     */
    StepWrapper currentStep;

    /**
     * Contains list of Features which are contained within the report
     */
    private final List<FeatureWrapper> features = new Vector();

    /**
     * Constructor confluence report
     * 
     * @param file
     *            File to which test results will be written
     */
    ConfluenceReport(File file) throws CucumberException {
        try {
            System.out.println("Creating " + file.getAbsolutePath()
                + " for results");
            reportBuilder = new ConfluenceReportBuilder(new OutputStreamWriter(
                new FileOutputStream(file)));
        } catch (FileNotFoundException error) {
            throw new CucumberException("Error creating file '"
                + file.getAbsolutePath() + "'", error);
        }
    }

    /**
     * Append feature to report
     * 
     * @param feature
     */
    public void feature(Feature feature) {
        currentFeature = new FeatureWrapper(feature);
        features.add(currentFeature);
    }

    /**
     * Append scenario to current feature. The last added feature is regarded
     * the current feature.
     * 
     * @param scenario
     */
    public void scenario(Scenario scenario) {
        currentScenario = currentFeature.scenario(scenario);
    }

    /**
     * Append step to current scenario. The last added scenario is regarded the
     * current scenario
     */
    public void step(Step step) {
        currentStep = currentScenario.step(step);
    }

    public void done() {
        System.out.println("Writing results");
        startReport();
        for (FeatureWrapper feature : features) {
            System.out.println("Writing results for " + feature);
            feature.emit(reportBuilder);
        }
        endReport();
    }

    public void close() {
        reportBuilder.close();
    }

    private void startReport() {
        reportBuilder.beginDocument();
        reportBuilder.beginHeading(1, "Features");
        reportBuilder.endHeading();
    }

    private void endReport() {
        reportBuilder.endDocument();
    }

}
