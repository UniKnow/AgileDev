package org.apache.maven.siteindexer.mojo;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.siteindexer.Indexer;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.siteindexer.PathUtils;

/**
 * This goal will build the index.
 */
@Mojo(name = "index", defaultPhase = LifecyclePhase.SITE)
public class IndexerMojo extends AbstractMojo {

	/**
	 * Root of site that needs to be indexed.
	 */
	@Parameter(property = "projectBuildDir", defaultValue = "${project.build.directory}/site", required = true)
	private File site;

	/**
	 * Location of search box
	 */
	@Parameter(property = "searchBox")
	private String searchBox;

	/**
	 *
	 * @throws MojoExecutionException
	 * @throws MojoFailureException
	 */
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		Indexer indexer = new Indexer(getLog());
		try {
			getLog().info("Maven Site Index");
			getLog().info("------------------------------");
			getLog().info("building index.js...");

			Path searchBoxPath = null;
			if (searchBox == null) {
//				try {
					// Get path search box from classpath
					searchBoxPath = PathUtils.getResourceAsPath("/searchbox.html");

//					URI uriToSearchBox = getClass().getResource("/searchbox.html").toURI();
//
//					final String[] array = uriToSearchBox.toString().split("!");
//					final FileSystem fs = FileSystems.newFileSystem(URI.create(array[0]), new HashMap<String, String>());
//					searchBoxPath = fs.getPath(array[1]);
//				}catch(URISyntaxException err) {
//					throw new MojoExecutionException("Error occurred while determining location search box", err);
//				}
			} else {
				searchBoxPath = Paths.get(searchBox);
			}

			indexer.buildIndex(
					Paths.get(site.getAbsolutePath()),
					site.getAbsolutePath() + "/js/index.js",
					searchBoxPath);

			getLog().info("done.");
		} catch (IOException e) {
			getLog().error(e);
		}
	}
}
