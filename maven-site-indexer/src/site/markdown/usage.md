# Adding the plugin to your POM

The Maven Site Indexer is a standard maven plugin and can be added in the following way to your `pom.xml`.

    <plugin>
        <groupId>org.uniknow.maven.plugins</groupId>
        <artifactId>index</artifactId>
        <version>${index.version}</version>
        <executions>
            <execution>
                <phase>post-site</phase>
                <goals>
                    <goal>index</goal>
                </goals>
            </execution>
        </executions>
    </plugin>