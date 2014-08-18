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
package org.uniknow.agiledev.junitbdd.internal.writers;

import static org.uniknow.agiledev.junitbdd.BDD.*;
import static org.uniknow.agiledev.junitbdd.BDDTestUtils.*;

import java.io.*;
import java.util.*;

import org.junit.runner.*;

import org.uniknow.agiledev.junitbdd.Scenario;
import org.uniknow.agiledev.junitbdd.BDDRunner;
import org.uniknow.agiledev.junitbdd.internal.domain.*;

@RunWith(BDDRunner.class)
public class WritingFilesScenarios {

    @Scenario
    public void shouldWriteFileInDefinedOutputFolder() {
        Given("existing story and StoryFileWriter writing to the 'WritingTest' folder");
        StoryModel story = new StoryModel("test", "html");
        StoryFileWriter writer = new StoryFileWriter() {
            {
                this.fileExtension = ".test";
            }

            @Override
            String textFor(StoryModel story) {
                return "";
            }

            @Override
            protected String tocFor(List<StoryModel> storiesList) {
                return "";
            }

        };
        writer.setOutputFolder("target" + File.separator + "WritingTest");

        When("story is written to a file");
        writer.write(story);

        Then("file with proper name exists");
        assertFileExistedInTarget("WritingTest" + File.separator + "reports"
            + File.separator + "Test.test");
        deleteDirectoryRecursively(new File("target" + File.separator
            + "WritingTest"));
    }
}
