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

import java.io.*;
import java.util.*;

import org.uniknow.agiledev.junitbdd.internal.domain.*;

public abstract class StoryFileWriter {

    public static final String REPORT_DIR = "reports";

    protected String fileExtension;
    protected String tocFile;
    protected String outputFolder = "target";

    private File writtenFile;

    public void write(StoryModel story) {
        String text = textFor(story);
        try {
            writeTextToFile(story, text);
        } catch (Exception e) {
            throw new RuntimeException("File could not be generated.", e);
        }
    }

    abstract String textFor(StoryModel story);

    public void writeToc(List<StoryModel> storiesList) {
        String text = tocFor(storiesList);
        try {
            writeTocToFile(text);
        } catch (Exception e) {
            throw new RuntimeException("File could not be generated.", e);
        }
    }

    protected abstract String tocFor(List<StoryModel> storiesList);

    protected void writeTextToFile(StoryModel story, String text)
        throws IOException {
        createOutputFolder();
        String fullDir = createPackageFolders(story.packageName());
        writtenFile = new File(fullDir + File.separator + story.camelisedName()
            + fileExtension);
        Writer writer = new OutputStreamWriter(new FileOutputStream(
            getWrittenFile(), false), "UTF-8");
        writer.append(text);
        writer.flush();
        writer.close();
    }

    protected void writeTocToFile(String text) throws IOException {
        FileWriter writer = new FileWriter(new File(buildFullPath(tocFile)),
            false);
        writer.append(text);
        writer.flush();
        writer.close();
    }

    private String createPackageFolders(String packageName) {
        packageName = packageName.replace('.', File.separatorChar);
        String fullDir = buildFullPath(packageName);
        if (!new File(fullDir).exists())
            new File(fullDir).mkdirs();
        return fullDir;
    }

    private String buildFullPath(String file) {
        return outputFolder + reportFolder() + File.separator + file;
    }

    public String reportFolder() {
        return File.separator + REPORT_DIR;
    }

    private void createOutputFolder() {
        if (!new File(buildFullPath("")).exists())
            new File(buildFullPath("")).mkdirs();
    }

    protected void copy(InputStream in, File dst) throws IOException {
        OutputStream out = new FileOutputStream(dst);

        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }

        in.close();
        out.close();
    }

    public File getWrittenFile() {
        return writtenFile;
    }

    public void setOutputFolder(String outputFolder) {
        if (outputFolder == null)
            this.outputFolder = "target";
        else
            this.outputFolder = outputFolder;
    }

    public String outputFolder() {
        return outputFolder;
    }
}