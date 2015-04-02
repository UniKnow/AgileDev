
package org.apache.maven.siteindexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
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
	
	private void addTags(String filename) {
        try {
	        File file = new File(startDir + filename);
	        BufferedReader reader = new BufferedReader(new FileReader(file));
	        String line = "", oldText = "";
	        while((line = reader.readLine()) != null)
	            {
	            oldText += line + "\r\n";
	        }
	        reader.close();
	        
	        if (oldText.indexOf("id=\"searchbox\"") > 0) {
	        	log.info("tags already added to '" + filename + "', quitting");
	        	return;	        	
	        }
	        
        	log.info("applying tags to '" + filename + "'...");

	        String newText = oldText.replaceAll("</body>", 
	    		"<div id=\"searchbox\">" +
	    		"  <iframe id=\"searchbox-frame\" src=\"searchbox.html\" width=\"100%\" style=\"border: 0\" height=\"100%\">" +
	    		"  </iframe>" +
	    		"</div>" +
	    		signature +
	    		"</body>"
	        );


        	FileWriter writer = new FileWriter(file);
	        writer.write(newText);
	        writer.close();
        	log.info("applied tags to '" + filename + "'");
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
		File dir = new File(dirName);
		FilenameFilter filter = new FilenameFilter() {
			@Override
		    public boolean accept(File dir, String name) {
				File child = new File(dir.getAbsolutePath() + File.separator + name);
		        return child.isFile() && (child.getName().endsWith("htm") || child.getName().endsWith("html"));
		    }
		};
		
		String[] files = dir.list(filter);

		if (files == null) {
		    // Either dir does not exist or is not a directory
		} else {
		    for (int i=0; i< files.length; i++) {
		        // Get filename of file or directory
		        String filename = files[i];
		        if (!"searchbox.html".equals(filename)) {
			        parseDocument(dir.getAbsolutePath() + File.separator + filename, out);
			        addTags(filename);
		        }
		    }
		}
		FilenameFilter dirFilter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				File child = new File(dir.getAbsolutePath() + File.separator + name);
		        return child.isDirectory();
			}
		};
		String[] dirs = dir.list(dirFilter);
		if (dirs == null) {
		    // Either dir does not exist or is not a directory
		} else {
		    for (int i=0; i< dirs.length; i++) {
		        // Get filename of file or directory
		        String subDirName = dirs[i];
		        crawlFolder(subDirName, out);
		    }
		}
    	log.info("done with folder '" + dirName + "'");
	}

	public void buildIndex(String startDir, String outputFile) throws IOException {
		File file = new File(outputFile);
		FileOutputStream out = new FileOutputStream(file);
		//LADDER search reference:
		//http://dev.theladders.com/archives/2006/11/introducing_javascript_fulltex_1.html
		out.write("var index = new LADDERS.search.index();\r\n".getBytes());
		out.write("var titles = new LADDERS.search.document();\r\n".getBytes());
		log.info("index.js initialized");
		this.startDir = new File(startDir).getAbsolutePath() + File.separator;
		crawlFolder(startDir, out);
	}

}
