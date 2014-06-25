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

import gherkin.formatter.Formatter;
import gherkin.formatter.Reporter;
import gherkin.formatter.model.*;

import java.io.File;
import java.util.List;

/**
 * Cucumber formatter to generate confluence report.
 */
public class ConfluenceFormatter implements Formatter, Reporter {

    /**
     * Contains index of current step
     */
    private int currentStep = 0;

    /**
     * Directory to which cucumber results will be written
     */
    private File reportDir;

    /**
     * Name of File which will be generated and contains the test results
     */
    private static final String CONFLUENCE_FILENAME = "cucumber.confluence";

    /**
     * Contains confluence report containing test results
     */
    private final ConfluenceReport report;

    /**
     * Constructor Confluence formatter
     */
    public ConfluenceFormatter(File confluenceReportDir) {
        this.reportDir = confluenceReportDir;

        // Create file to which test results will be written
        confluenceReportDir.mkdirs();
        File results = new File(confluenceReportDir, CONFLUENCE_FILENAME);

        // Create report containing cucumber results in confluence wiki markup
        report = new ConfluenceReport(results);
    }

    /**
     * Is called in case any syntax error was detected during the parsing of the
     * feature files.
     * 
     * @param state
     *            the current state of the parser machine
     * @param event
     *            detected event
     * @param legalEvents
     *            expected event
     * @param uri
     *            the URI of the feature file
     * @param line
     *            the line number of the event
     */
    public void syntaxError(String state, String event,
        List<String> legalEvents, String uri, Integer line) {

    }

    /**
     * Called at the beginning of each feature.
     * 
     * @param uri
     *            the feature's URI
     */
    public void uri(String uri) {

    }

    /**
     * Called after the {@link Formatter#uri(String)}, but before the actual
     * feature execution.
     * 
     * @param feature
     *            the to be executed {@linkplain Feature}
     */
    public void feature(Feature feature) {
        System.out.println("Adding feature '" + feature + "'to report");
        report.feature(feature);
    }

    /**
     * Called before the actual execution of the scenario outline step
     * container.
     * 
     * @param scenarioOutline
     *            the to be executed {@link ScenarioOutline}
     */
    public void scenarioOutline(ScenarioOutline scenarioOutline) {

    }

    /**
     * Called before the actual execution of the scenario examples. This is
     * called after the
     * {@link Formatter#scenarioOutline(gherkin.formatter.model.ScenarioOutline)}
     * , but before any actual scenario example.
     * 
     * @param examples
     *            the to be executed
     */
    public void examples(Examples examples) {

    }

    /**
     * Is called at the beginning of the scenario life cycle, meaning before the
     * first "before" hook.
     * 
     * @param scenario
     *            the {@link Scenario} of the current lifecycle
     */
    public void startOfScenarioLifeCycle(Scenario scenario) {

    }

    /**
     * Called before the actual execution of the background step container.
     * 
     * @param background
     *            the to be executed {@link Background}
     */
    public void background(Background background) {

    }

    /**
     * Called before the actual execution of the scenario step container.
     * 
     * @param scenario
     *            the to be executed {@link Scenario}
     */
    public void scenario(Scenario scenario) {
        report.scenario(scenario);
        currentStep = 0;
    }

    /**
     * Is called for each step of a step container. <b>Attention:</b> All steps
     * are iterated through this method before any step is actually executed.
     * 
     * @param step
     *            the {@link Step} to be executed
     */
    public void step(Step step) {
        report.step(step);
    }

    /**
     * Is called at the end of the scenario life cycle, meaning after the last
     * "after" hook. * @param scenario the {@link Scenario} of the current
     * lifecycle
     */
    public void endOfScenarioLifeCycle(Scenario scenario) {

    }

    /**
     * Indicates that the last file has been processed. This should print out
     * any closing output, such as completing the JSON string, but it should
     * *not* close any underlying streams/writers.
     */
    public void done() {
        report.done();
    }

    /**
     * Closes all underlying streams.
     */
    public void close() {
        report.close();
    }

    /**
     * Indicates the End-Of-File for a Gherkin document (.feature file)
     */
    public void eof() {

    }

    @Override
    public void before(Match match, Result result) {
        System.out.println("MATCH LOCATION: " + match.getLocation());
        // To change body of implemented methods use File | Settings | File
        // Templates.
    }

    @Override
    public void result(Result result) {
        System.out.println("Adding result for step " + currentStep);
        // report.currentStep.setResult(result);
        report.currentScenario.getStep(currentStep).setResult(result);
        currentStep++;
        // To change body of implemented methods use File | Settings | File
        // Templates.
    }

    @Override
    public void after(Match match, Result result) {

        // To change body of implemented methods use File | Settings | File
        // Templates.
    }

    @Override
    public void match(Match match) {
        System.out.println("Adding match " + match.getArguments());
        // To change body of implemented methods use File | Settings | File
        // Templates.
    }

    @Override
    public void embedding(String s, byte[] bytes) {
        // To change body of implemented methods use File | Settings | File
        // Templates.
    }

    @Override
    public void write(String s) {
        // To change body of implemented methods use File | Settings | File
        // Templates.
    }
}
