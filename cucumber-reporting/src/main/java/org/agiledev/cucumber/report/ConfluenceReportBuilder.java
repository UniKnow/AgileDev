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

import cucumber.runtime.CucumberException;
import org.eclipse.mylyn.internal.wikitext.confluence.core.ConfluenceDocumentBuilder;
import org.eclipse.mylyn.wikitext.core.parser.Attributes;
import org.uniknow.agiledev.dbc4java.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * Created with IntelliJ IDEA. User: mase Date: 6/3/14 Time: 12:09 PM To change
 * this template use File | Settings | File Templates.
 */
@Validated
public class ConfluenceReportBuilder extends ConfluenceDocumentBuilder {

    @NotNull
    private final Writer writer;

    /**
     * Constructor
     * 
     * @param writer
     *            Writer that will be used to persist confluence report
     */
    public ConfluenceReportBuilder(Writer writer) {
        super(writer);
        this.writer = writer;
    }

    /**
     * 
     * @param level
     *            Level of header. Minimal value is 1
     * @param title
     *            Title of header
     */
    public void beginHeading(@Min(1) int level, String title) {
        Attributes attributes = new Attributes();
        beginHeading(level, attributes);
        characters(title);
    }

    public void beginParagraph() {
        beginBlock(BlockType.PARAGRAPH, null);
    }

    public void endParagraph() {
        endBlock();
    }

    public void beginCodeBlock() {
        beginBlock(BlockType.CODE, null);
    }

    public void endCodeBlock() {
        endBlock();
    }

    public void beginNumberedList() {
        beginBlock(BlockType.NUMERIC_LIST, null);
    }

    /**
     * Appends specified items to List
     * 
     * @param items
     *            List of Strings containing the items that should be appended
     *            to the list
     */
    public void addListItem(List<String> items) {
        for (String item : items) {
            beginBlock(BlockType.LIST_ITEM, null);
            characters(item);
            endBlock();
        }
    }

    public void endNumberedList() {
        endBlock();
    }

    public void beginTable() {
        beginBlock(BlockType.TABLE, null);
    }

    public void setTableHeaders(List<String> headers) {

        // Set column headers table (if any)
        beginBlock(BlockType.TABLE_ROW, null);
        for (String header : headers) {
            beginBlock(BlockType.TABLE_CELL_HEADER, null);
            characters(header);
            endBlock();
        }
        endBlock();
    }

    public void addTableRow(List<String> content) {
        // Set content table row
        beginBlock(BlockType.TABLE_ROW, null);
        for (String valueCell : content) {
            beginBlock(BlockType.TABLE_CELL_NORMAL, null);
            characters(valueCell);
            endBlock();
        }
        endBlock();
    }

    public void endTable() {
        endBlock();
    }

    public void close() {
        try {
            writer.flush();
            writer.close();
        } catch (IOException error) {
            throw new CucumberException(
                "Exception occurred while persisting results", error);
        }
    }

}
