package org.apache.maven.siteindexer;

import org.apache.maven.plugin.MojoExecutionException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;

/**
 * Contains utility methods for getting {@code Path} instances
 */
public class PathUtils {

    /**
     * Finds a resource with a given name.
     *
     * @param name name of the desired resource
     * @return A  {@link java.nio.file.Path} object or {@code null} if no
     * resource with this name is found
     */
    public static final Path getResourceAsPath(String name) {
        Path resourcePath = null;
        try {
            // Get path search box from classpath
            URI uriToSearchBox = PathUtils.class.getResource(name).toURI();

            final String[] parts = uriToSearchBox.toString().split("!");

            FileSystem fs = null;
            try {
                fs = FileSystems.getFileSystem(URI.create(parts[0]));
            } catch (FileSystemNotFoundException err) {
                fs = FileSystems.newFileSystem(URI.create(parts[0]), new HashMap<String, String>());
            }

            resourcePath = fs.getPath(parts[1]);

        } catch (URISyntaxException err) {
            return null;
        } catch (IOException err) {
            return null;
        }
        return resourcePath;
    }
}
