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
package org.uniknow.maven.index;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.StringTokenizer;

import org.apache.maven.plugin.logging.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Indexer {

    private Path startDir;
    private Log log;

    public Indexer(Log log) {
        this.log = log;
    }

    /**
     * Returns a relative path from {@code startDir} to specified {@code file}.
     * 
     * @param file
     *            file for which we want to get the relative path
     * @return relative path from {@code startDir} to specified {@code file}.
     */
    private String relativeToStart(Path file) {
        return startDir.relativize(file).toString().replaceAll("\\\\", "\\/");
    }

    /**
     * Returns a relative path from {@code file} to {@code startDir}.
     * 
     * @param file
     *            file for which we want to get the relative path
     * @return relative path from {@code file} to {@code startDir}.
     */
    private String relativeFromStart(Path file) {
        return file.relativize(startDir).toString().replaceAll("\\\\", "\\/");
    }

    private String clean(String textContent) {
        return textContent.replaceAll("\\.", " ").replaceAll("\\\"", " ")
            .replaceAll("\\\\", " ").replaceAll("\\/", " ")
            .replaceAll("\\'", " ").replaceAll("\\-", " ")
            .replaceAll("\\.", " ").replaceAll("\\:", " ")
            .replaceAll("\\;", " ").replaceAll("\\!", " ")
            .replaceAll("\\?", " ").replaceAll("\\|", " ")
            .replaceAll("\\(", " ").replaceAll("\\)", " ")
            .replaceAll("\\[", " ").replaceAll("\\]", " ")
            .replaceAll("\\{", " ").replaceAll("\\}", " ")
            .replaceAll("\\$", " ").replaceAll("\\=", " ")
            .replaceAll("\\+", " ").replaceAll("\\*", " ")
            .replaceAll("\\^", " ").replaceAll("\\~", " ")
            .replaceAll("\\©", " ").replaceAll("\\,", " ");
    }

    private void tokenizeText(String textContent, FileOutputStream out)
        throws IOException {
        String clean = clean(textContent);
        StringTokenizer st = new StringTokenizer(clean);
        while (st.hasMoreTokens()) {
            out.write(st.nextToken().getBytes());
            out.write(" ".getBytes());
        }
    }

    private final String signature = "<!-- Search box courtesy of Maven Site Indexer -->";

    /**
     * Adds tags for searchbox to specified file. If the specified file already
     * contains tags for the searchbox the file will be unmodified.
     * 
     * @param file
     *            file to which we want to add the searchbox tags.
     */
    private void addTags(Path file) {
        try {
            // File file = new File(startDir + filename);
            BufferedReader reader = new BufferedReader(new FileReader(
                file.toFile()));
            String line = "", oldText = "";
            while ((line = reader.readLine()) != null) {
                oldText += line + "\r\n";
            }
            reader.close();

            if (oldText.indexOf("id=\"searchbox\"") > 0) {
                log.info("tags already added to '" + file.getFileName()
                    + "', quitting");
                return;
            }

            log.info("applying tags to '" + file.getFileName() + "'...");
            String newText = oldText
                .replaceAll(
                    "</body>",
                    "<div id=\"searchbox\">\n"
                        + "  <iframe id=\"searchbox-frame\" src=\"./"
                        + relativeFromStart(file.getParent())
                        + "/searchbox.html\" width=\"100%\" style=\"border: 0\" height=\"100%\">\n"
                        + "  </iframe>\n" + "</div>\n" + signature
                        + "\n</body>");

            FileWriter writer = new FileWriter(file.toFile());
            writer.write(newText);
            writer.close();
            log.info("applied tags to '" + file.getFileName() + "'");
        } catch (IOException ioe) {
            log.error(ioe);
        }
    }

    private void parseDocument(Path filename, FileOutputStream out)
        throws IOException {
        log.info("indexing '" + relativeToStart(filename) + "'...");
        out.write("var d = new LADDERS.search.document();\r\n".getBytes());
        // out.write(("d.add(\"id\", '" + relativeToStart(filename) +
        // "');\r\n").getBytes());
        out.write(("d.add(\"id\", '" + relativeToStart(filename) + "');\r\n")
            .getBytes());
        out.write("d.add(\"text\", \"".getBytes());

        Document doc;
        try {
            doc = Jsoup.parse(new File(filename.toUri()), "utf-8");
            tokenizeText(doc.text(), out);
            out.write("\");\r\n".getBytes());
            out.write(("d.add(\"title\", '" + doc.title() + "');\r\n")
                .getBytes());
            out.write(("titles.add(\"" + relativeToStart(filename) + "\", \""
                + doc.title() + "\");\r\n").getBytes());
        } catch (IOException e) {
            log.error(e);
        }
        out.write("index.addDocument(d);\r\n\r\n".getBytes());
        log.info("done indexing '" + relativeToStart(filename) + "'");
    }

    private void crawlFolder(Path directory, FileOutputStream out)
        throws IOException {
        log.info("crawling folder '" + directory + "'...");
        // Path path = Paths.get(dirName);

        // Traverse html files within directory
        DirectoryStream.Filter<Path> htmlFilter = new DirectoryStream.Filter<Path>() {

            @Override
            public boolean accept(Path entry) throws IOException {
                return !Files.isDirectory(entry)
                    && (entry.getFileName().toString().endsWith("htm") || entry
                        .getFileName().toString().endsWith("html"));
            }
        };
        DirectoryStream<Path> files = Files.newDirectoryStream(directory,
            htmlFilter);
        for (Path file : files) {
            if (!file.toAbsolutePath().endsWith("searchbox.html")) {
                parseDocument(file, out);
                addTags(file.toAbsolutePath());
            }
        }

        // Traverse html files within directory
        DirectoryStream.Filter<Path> subDirectoryFilter = new DirectoryStream.Filter<Path>() {

            @Override
            public boolean accept(Path entry) throws IOException {
                return Files.isDirectory(entry);
            }
        };
        DirectoryStream<Path> directories = Files.newDirectoryStream(directory,
            subDirectoryFilter);
        for (Path dir : directories) {
            crawlFolder(dir, out);
        }

        log.info("done with folder '" + directory + "'");
    }

    /**
     * Build index
     * 
     * @param startDir
     *            root of site that needs to be indexed
     * @param outputFile
     *            location at which index file needs to be persisted
     * @param searchBox
     *            location of search box
     * @throws IOException
     */
    public void buildIndex(Path startDir, String outputFile, Path searchBox)
        throws IOException {

        // Put search box within root of site
        Files.copy(searchBox, startDir.resolve("searchbox.html"),
            StandardCopyOption.REPLACE_EXISTING);

        // Put search.js within root/js of site
        Files.copy(PathUtils.getResourceAsPath("/search.js"),
            startDir.resolve("js/search.js"),
            StandardCopyOption.REPLACE_EXISTING);

        // Create index file
        Path path = Paths.get(outputFile);
        if (!path.toFile().exists()) {
            // Create index file
            log.info("creating " + path.toAbsolutePath());
            // Files.createDirectories(path);
            Files.createFile(path);
        }
        FileOutputStream out = new FileOutputStream(path.toFile());
        log.info("opened " + path.toAbsolutePath());
        // LADDER search reference:
        // http://dev.theladders.com/archives/2006/11/introducing_javascript_fulltex_1.html
        out.write("var index = new LADDERS.search.index();\r\n".getBytes());
        out.write("var titles = new LADDERS.search.document();\r\n".getBytes());
        log.info("index.js initialized");
        this.startDir = startDir; // new File(startDir).getAbsolutePath() +
                                  // File.separator;
        crawlFolder(startDir, out);
        out.close();
    }

}
