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
package org.agiledev.cucumber.report.model;

import gherkin.formatter.model.Result;
import gherkin.formatter.model.Step;
import org.agiledev.cucumber.report.ConfluenceReportBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper cucumber Step
 */
public class StepWrapper implements Wrapper {

    /*
     * Contains description of step
     */
    private final Step step;

    /*
     * Contains result of execution step
     */
    private Result result;

    /**
     * Appends the test results of wrapped cucumber entity to the confluence
     * report
     * 
     * @param reportBuilder
     *            Builder by which tests results can be appended to document
     */
    @Override
    public void emit(ConfluenceReportBuilder reportBuilder) {
        System.out.println("Adding step " + step.getKeyword() + ":"
            + step.getName() + " to report");
        List<String> row = new ArrayList();
        row.add(step.getKeyword());
        row.add(step.getName());

        if (result == null) {
            row.add(" ");
        } else {
            row.add(result.getStatus());

            // TODO: Add details failure to table
        }

        reportBuilder.addTableRow(row);

        // reportBuilder.beginParagraph();
        // reportBuilder.characters(step.getKeyword());
        // reportBuilder.characters(": ");
        // reportBuilder.characters(step.getName());
        // reportBuilder.endParagraph();
        //
        // if (result != null) {
        // reportBuilder.beginParagraph();
        // reportBuilder.characters("RESULT : ");
        // reportBuilder.characters(result.getStatus());
        // reportBuilder.endParagraph();
        // if (result.getStatus() == Result.FAILED) {
        // reportBuilder.beginParagraph();
        // reportBuilder.characters("\t");
        // reportBuilder.characters(result.getErrorMessage());
        // reportBuilder.endParagraph();
        // }
        // }

    }

    /**
     * Constructor
     * 
     * @param step
     *            Step which will be wrapped
     */
    StepWrapper(Step step) {
        this.step = step;
    }

    /**
     * Set result of step
     */
    public void setResult(Result result) {
        this.result = result;
    }
}
