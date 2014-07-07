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

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA. User: mase Date: 6/3/14 Time: 2:15 PM To change
 * this template use File | Settings | File Templates.
 */
public class ConfluenceReportBuilderTest {

    /**
     * Validates IllegalArgumentException is thrown in case passed builder is
     * null
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorBuilderNull() {
        new ConfluenceReportBuilder(null);
    }

    /**
     * Validates IllegalArgumentException is thrown in case specified header
     * level <1
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBeginHeaderLevelLessThanOne() {
        StringWriter writer = new StringWriter();

        ConfluenceReportBuilder builder = new ConfluenceReportBuilder(writer);
        builder.beginDocument();
        builder.beginHeading(0, "TEST");
    }

    /**
     * Validates Header is successfully appended to confluence document
     */
    @Test
    public void addHeader() {
        StringWriter writer = new StringWriter();

        ConfluenceReportBuilder builder = new ConfluenceReportBuilder(writer);
        builder.beginDocument();
        builder.beginHeading(1, "TEST");
        builder.endHeading();
        builder.endDocument();

        assertEquals("h1. TEST\n\n", writer.toString());
    }

    /**
     * Validates paragraph is added successfully to confluence document
     */
    @Test
    public void addParagraph() {
        StringWriter writer = new StringWriter();

        ConfluenceReportBuilder builder = new ConfluenceReportBuilder(writer);
        builder.beginDocument();
        builder.beginParagraph();
        builder.characters("TEST PARAGRAPH");
        builder.endParagraph();
        builder.endDocument();

        assertEquals("TEST PARAGRAPH\n\n", writer.toString());
    }

    /**
     * Validates code block is added successfully to confluence document
     */
    @Test
    public void addCodeBlock() {
        StringWriter writer = new StringWriter();

        ConfluenceReportBuilder builder = new ConfluenceReportBuilder(writer);
        builder.beginDocument();
        builder.beginCodeBlock();
        builder.characters("TEST CODE BLOCK");
        builder.endCodeBlock();
        builder.endDocument();

        assertEquals("{code}TEST CODE BLOCK{code}\n\n", writer.toString());
    }

    /**
     * Validates numbered list is added successfully to confluence document
     */
    @Test
    public void addNumberedList() {
        List<String> items = Arrays.asList(new String[] { "First Item",
                "Second Item", "Third Item" });
        StringWriter writer = new StringWriter();

        ConfluenceReportBuilder builder = new ConfluenceReportBuilder(writer);
        builder.beginDocument();
        builder.beginNumberedList();
        builder.addListItem(items);
        builder.endNumberedList();
        builder.endDocument();

        assertEquals("# First Item\n# Second Item\n# Third Item\n\n",
            writer.toString());
    }

    /**
     * Validates table is added successfully to confluence document
     */
    @Test
    public void addTable() {
        List<String> headers = Arrays.asList(new String[] { "Header column 1",
                "Header column 2", "Header Column 3" });
        List<String> content = Arrays.asList((new String[] { "Content 1",
                "Content 2", "Content 3" }));
        StringWriter writer = new StringWriter();

        ConfluenceReportBuilder builder = new ConfluenceReportBuilder(writer);
        builder.beginDocument();
        builder.beginTable();
        builder.setTableHeaders(headers);
        builder.addTableRow(content);
        builder.endTable();
        builder.endDocument();

        // TODO: Verify whether output is correct
        System.out.println(writer.toString());

    }
}
