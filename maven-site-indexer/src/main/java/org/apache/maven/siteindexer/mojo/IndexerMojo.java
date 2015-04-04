package org.apache.maven.siteindexer.mojo;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.siteindexer.Indexer;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * This goal will build the index.
 */
@Mojo(name = "index", defaultPhase = LifecyclePhase.SITE)
public class IndexerMojo extends AbstractMojo {

	@Parameter(property = "projectBuildDir", defaultValue = "${project.build.directory}/site", required = true)
	private File site;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		Indexer indexer = new Indexer(getLog());
		try {
			getLog().info("Maven Site Index");
			getLog().info("------------------------------");
			getLog().info("building index.js...");

			indexer.buildIndex(
					site.getAbsolutePath(),
					site.getAbsolutePath() + "/js/index.js");
			getLog().info("done.");
		} catch (IOException e) {
			getLog().error(e);
		}
	}
}
