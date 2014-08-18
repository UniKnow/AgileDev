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
import freemarker.template.*;

public class FreemarkerFileWriter extends StoryFileWriter {

    private Template template;
    private Template tocTemplate;

    public FreemarkerFileWriter() {
        fileExtension = "Scenarios.html";
        tocFile = "index.html";
        setupFreeMarkerCongifuration();
    }

    private void setupFreeMarkerCongifuration() {
        try {
            Configuration cfg = new Configuration();
            cfg.setObjectWrapper(new DefaultObjectWrapper());

            if (isCustomTemplateUsed()) {
                cfg.setDirectoryForTemplateLoading(new File(System
                    .getProperty("template")));
            } else {
                cfg.setClassForTemplateLoading(this.getClass(), "html");
            }

            template = cfg.getTemplate("template.html");
            tocTemplate = cfg.getTemplate("toc-template.html");
        } catch (IOException e) {
            throw new RuntimeException(
                "Error getting HTML template - wrong template folder or no template.html in given folder.",
                e);
        }
    }

    @Override
    String textFor(StoryModel story) {
        if (story == null)
            throw new IllegalArgumentException("story cannot be null");

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("keywords", new Keyword());
        paramMap.put("story", story);
        paramMap.put("scenarios", story.scenarios());
        // paramMap.put("narrative", story.narrative());
        paramMap.put("runStatistics", story.runStatistics());
        paramMap.put("resources", resourcesLocationRelativeTo(story));

        StringWriter writer = new StringWriter();
        try {
            template.process(paramMap, writer);
        } catch (Exception e) {
            throw new RuntimeException("Error while processing HTML template",
                e);
        }
        return writer.toString();
    }

    protected String tocFor(java.util.List<StoryModel> storiesList) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("stories", storiesList);
        paramMap.put("runStoryStatistics", runStoryStatisticsFor(storiesList));
        paramMap.put("runScenarioStatistics",
            runScenarioStatisticsFor(storiesList));
        paramMap.put("resources", "resources");

        StringWriter writer = new StringWriter();
        try {
            tocTemplate.process(paramMap, writer);
        } catch (Exception e) {
            throw new RuntimeException(
                "Error while processing HTML toc template", e);
        }
        return writer.toString();
    }

    private Object runStoryStatisticsFor(List<StoryModel> stories) {
        RunStatistics stats = RunStatistics.fromArray(new int[] { 0, 0, 0 });
        for (StoryModel story : stories)
            stats.updateStoryStatistics(story);
        return stats;
    }

    private Object runScenarioStatisticsFor(List<StoryModel> stories) {
        RunStatistics stats = RunStatistics.fromArray(new int[] { 0, 0, 0 });
        for (StoryModel story : stories)
            stats.updateWith(story.runStatistics());
        return stats;
    }

    private String resourcesLocationRelativeTo(StoryModel story) {
        String packageName = story.packageName();
        if (!"".equals(packageName)) {
            packageName += ".";
            packageName = packageName.replaceAll("[^\\.]*\\.", "../");
        }

        return packageName + "resources";
    }

    @Override
    protected void writeTextToFile(StoryModel story, String text)
        throws IOException {
        super.writeTextToFile(story, text);
        copyAdditionalFiles();
    }

    private void copyAdditionalFiles() throws IOException {
        String resources = createResourcesFolder();
        if (isCustomTemplateUsed())
            copyCustomTemplateFiles(resources);
        else
            copyDefaultTemplateFiles(resources);
    }

    private boolean isCustomTemplateUsed() {
        return System.getProperty("template") != null;
    }

    private void copyCustomTemplateFiles(String resources) throws IOException {
        File resourcesDir = new File(System.getProperty("template")
            + File.separator + "resources");
        if (resourcesDir != null) {
            for (File resource : resourcesDir
                .listFiles(new OnlyFilesFileFilter())) {
                File dst = new File(resources + File.separator
                    + resource.getName());
                copy(new FileInputStream(resource), dst);
            }
        }
    }

    private void copyDefaultTemplateFiles(String resources) throws IOException {
        String[] files = { "FAILED.png", "PASSED.png", "PENDING.png",
                "report.css", "UniKnow-logo.jpg" };
        for (String file : files) {
            File dst = new File(resources + File.separator + file);
            if (!dst.exists())
                copy(getClass().getResourceAsStream("html/" + file), dst);
        }
    }

    private String createResourcesFolder() {
        File resources = new File(outputFolder + reportFolder()
            + File.separator + "resources");
        if (!resources.exists())
            resources.mkdirs();
        return resources.getAbsolutePath();
    }

    private final class OnlyFilesFileFilter implements FileFilter {
        public boolean accept(File file) {
            return !file.isDirectory();
        }
    }
}
