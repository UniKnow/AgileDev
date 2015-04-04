
package org.apache.maven.siteindexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.StringTokenizer;

import org.apache.maven.plugin.logging.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class Indexer {

    private String startDir;
    private Log log;

    public Indexer(Log log) {
        this.log = log;
    }

    private String relativeToStart(String filename) {
        return filename.replaceAll("\\\\", "\\/").substring(startDir.length());
    }


    private String clean(String textContent) {
        return textContent
                .replaceAll("\\.", " ")
                .replaceAll("\\\"", " ")
                .replaceAll("\\\\", " ")
                .replaceAll("\\/", " ")
                .replaceAll("\\'", " ")
                .replaceAll("\\-", " ")
                .replaceAll("\\.", " ")
                .replaceAll("\\:", " ")
                .replaceAll("\\;", " ")
                .replaceAll("\\!", " ")
                .replaceAll("\\?", " ")
                .replaceAll("\\|", " ")
                .replaceAll("\\(", " ")
                .replaceAll("\\)", " ")
                .replaceAll("\\[", " ")
                .replaceAll("\\]", " ")
                .replaceAll("\\{", " ")
                .replaceAll("\\}", " ")
                .replaceAll("\\$", " ")
                .replaceAll("\\=", " ")
                .replaceAll("\\+", " ")
                .replaceAll("\\*", " ")
                .replaceAll("\\^", " ")
                .replaceAll("\\~", " ")
                .replaceAll("\\©", " ")
                .replaceAll("\\,", " ")
                ;
    }

    private void tokenizeText(String textContent, FileOutputStream out) throws IOException {
        String clean = clean(textContent);
        StringTokenizer st = new StringTokenizer(clean);
        while (st.hasMoreTokens()) {
            out.write(st.nextToken().getBytes());
            out.write(" ".getBytes());
        }
    }

    private final String signature = "<!-- Search box courtesy of Maven Site Indexer -->";

    private void addTags(Path file) {
        try {
            //File file = new File(startDir + filename);
            BufferedReader reader = new BufferedReader(new FileReader(file.toFile()));
            String line = "", oldText = "";
            while ((line = reader.readLine()) != null) {
                oldText += line + "\r\n";
            }
            reader.close();

            if (oldText.indexOf("id=\"searchbox\"") > 0) {
                log.info("tags already added to '" + file.getFileName() + "', quitting");
                return;
            }

            log.info("applying tags to '" + file.getFileName() + "'...");

            String newText = oldText.replaceAll("</body>",
                    "<div id=\"searchbox\">" +
                            "  <iframe id=\"searchbox-frame\" src=\""+
                            startDir + "/searchbox.html\" width=\"100%\" style=\"border: 0\" height=\"100%\">" +
                            "  </iframe>" +
                            "</div>" +
                            signature +
                            "</body>"
            );


            FileWriter writer = new FileWriter(file.toFile());
            writer.write(newText);
            writer.close();
            log.info("applied tags to '" + file.getFileName() + "'");
        } catch (IOException ioe) {
            log.error(ioe);
        }
    }

    private void parseDocument(String filename, FileOutputStream out) throws IOException {
        log.info("indexing '" + relativeToStart(filename) + "'...");
        out.write("var d = new LADDERS.search.document();\r\n".getBytes());
        out.write(("d.add(\"id\", '" + relativeToStart(filename) + "');\r\n").getBytes());
        out.write("d.add(\"text\", \"".getBytes());

        Document doc;
        try {
            doc = Jsoup.parse(new File(filename), "utf-8");
            tokenizeText(doc.text(), out);
            out.write("\");\r\n".getBytes());
            out.write(("d.add(\"title\", '" + doc.title() + "');\r\n").getBytes());
            out.write(
                    ("titles.add(\"" + relativeToStart(filename) + "\", \"" + doc.title() + "\");\r\n").getBytes()
            );
        } catch (IOException e) {
            log.error(e);
        }
        out.write("index.addDocument(d);\r\n\r\n".getBytes());
        log.info("done indexing '" + relativeToStart(filename) + "'");
    }

    private void crawlFolder(String dirName, FileOutputStream out) throws IOException {
        log.info("crawling folder '" + dirName + "'...");
        Path path = Paths.get(dirName);

        // Traverse html files within directory
        DirectoryStream.Filter<Path> htmlFilter = new DirectoryStream.Filter<Path>() {

            @Override
            public boolean accept(Path entry) throws IOException {
                return !Files.isDirectory(entry) && (entry.getFileName().toString().endsWith("htm") || entry.getFileName().toString().endsWith("html"));
            }
        };
        DirectoryStream<Path> files = Files.newDirectoryStream(path, htmlFilter);
        for(Path file : files) {
            if (!file.toAbsolutePath().endsWith("searchbox.html")) {
                parseDocument(file.toAbsolutePath().toString(), out);
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
        DirectoryStream<Path> directories = Files.newDirectoryStream(path, subDirectoryFilter);
        for(Path dir : directories) {
            crawlFolder(dir.toString(), out);
        }

        log.info("done with folder '" + dirName + "'");
    }

    public void buildIndex(String startDir, String outputFile) throws IOException {
        Path path = Paths.get(outputFile);
        //File file = new File(outputFile);

        if (!path.toFile().exists()) {
            // Create index file
            log.info("creating " + path.toAbsolutePath());
//            Files.createDirectories(path);
            Files.createFile(path);
        }
        FileOutputStream out = new FileOutputStream(path.toFile());
        log.info("opened " + path.toAbsolutePath());
        //LADDER search reference:
        //http://dev.theladders.com/archives/2006/11/introducing_javascript_fulltex_1.html
        out.write("var index = new LADDERS.search.index();\r\n".getBytes());
        out.write("var titles = new LADDERS.search.document();\r\n".getBytes());
        log.info("index.js initialized");
        this.startDir = new File(startDir).getAbsolutePath() + File.separator;
        crawlFolder(startDir, out);
        out.close();
    }

}
