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
package org.uniknow.maven.index.plugins;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.uniknow.maven.index.Indexer;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.uniknow.maven.index.PathUtils;

/**
 * This goal will build the index.
 */
@Mojo(name = "index", defaultPhase = LifecyclePhase.SITE)
public class IndexerMojo extends AbstractMojo {

    /**
     * Root of site that needs to be indexed.
     */
    @Parameter(property = "projectBuildDir",
        defaultValue = "${project.build.directory}/site", required = true)
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
                // try {
                // Get path search box from classpath
                searchBoxPath = PathUtils.getResourceAsPath("/searchbox.html");

                // URI uriToSearchBox =
                // getClass().getResource("/searchbox.html").toURI();
                //
                // final String[] array = uriToSearchBox.toString().split("!");
                // final FileSystem fs =
                // FileSystems.newFileSystem(URI.create(array[0]), new
                // HashMap<String, String>());
                // searchBoxPath = fs.getPath(array[1]);
                // }catch(URISyntaxException err) {
                // throw new
                // MojoExecutionException("Error occurred while determining location search box",
                // err);
                // }
            } else {
                searchBoxPath = Paths.get(searchBox);
            }

            indexer.buildIndex(Paths.get(site.getAbsolutePath()),
                site.getAbsolutePath() + "/js/index.js", searchBoxPath);

            getLog().info("done.");
        } catch (IOException e) {
            getLog().error(e);
        }
    }
}
