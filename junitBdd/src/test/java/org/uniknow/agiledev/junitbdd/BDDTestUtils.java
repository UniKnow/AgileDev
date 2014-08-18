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
package org.uniknow.agiledev.junitbdd;

import static org.junit.Assert.*;

import java.io.*;

import org.uniknow.agiledev.junitbdd.internal.writers.*;

public class BDDTestUtils {
    private static StoryFileWriter writer = new FreemarkerFileWriter();

    public static void assertReportExisted(String fileName) {
        assertReportExisted(fileName, writer);
    }

    public static void assertReportExisted(String fileName,
        StoryFileWriter storyWriter) {
        fileName = storyWriter.outputFolder() + storyWriter.reportFolder()
            + File.separator + fileName;

        assertTrue(new File(fileName).delete());
    }

    public static void assertFileExistedInTarget(String fileName) {
        fileName = "target" + File.separator + fileName;

        assertTrue(new File(fileName).delete());
    }

    public static boolean deleteDirectoryRecursively(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory())
                    deleteDirectoryRecursively(files[i]);
                else
                    files[i].delete();
            }
        }
        return path.delete();
    }

    public static void deleteReportFolder() {
        deleteReportDirectory(writer);
    }

    public static void deleteReportDirectory(StoryFileWriter storyWriter) {
        if (System.getProperty("generateReport") == null)
            deleteDirectoryRecursively(new File(storyWriter.outputFolder()
                + writer.reportFolder()));
    }
}
