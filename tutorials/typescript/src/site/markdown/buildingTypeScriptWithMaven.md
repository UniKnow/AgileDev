# Building TypeScript with Maven

To realize compilation of TypeScript into normal JavaScript we will include Grunt within the maven build. [Grunt](http://gruntjs.com/) is a task runner which runs in [node.js](http://nodejs.org/) and is along with its plugins (tasks) distributed with [NPM](https://www.npmjs.org/).

## Setup

For our tutorial we are going to setup a simple module with the following folder structure and files:

    module
      |
      +--src
      |   |
      |   +--main
      |        |
      |        +--typescript
      |
      +--GruntFile.js
      |
      +--package.json
      |
      +--pom.xml
      
## Frontend maven plugin

The [frontend maven plugin](http://mvnrepository.com/artifact/com.github.eirslett/frontend-maven-plugin) adds the `node.js` binary to our project (no install required) and delegates commands to the binary. This means that you do not need to install `node.js` on your development machines or build servers.

The following execution imports `node.js` and `NPM` binaries to your project. 

    <plugin>
    	<groupId>com.github.eirslett</groupId>
    	<artifactId>frontend-maven-plugin</artifactId>
        ...
        <executions>
            ...
            <execution>
               <id>install node and npm</id>
               <phase>generate-resources</phase>
               <goals>
                   <goal>install-node-and-npm</goal>
               </goals>
               <configuration>
                   <nodeVersion>v0.10.18</nodeVersion>
                   <npmVersion>1.3.8</npmVersion>
               </configuration>
            </execution>
            ...
        </executions>
    </plugin>
    
The `package.json` has pretty much the same responsibility for `NPM` as the POM has for maven. The `package.json` contains a description of you project and it's dependencies. The primary feature we want to realize is compiling `typescript`, so we add the `grunt-ts` plugin.

    {
       "name":"tutorial-typescript",
       "version":"0.0.1-SNAPSHOT",
       "dependencies": {
          "grunt": "~0.4.5",
          "grunt-cli": "~0.1.13",
          "grunt-ts":"~5.3.2"
       },
       "devDependencies": {
    
       }
    }
    
With the `package.json` in place, the `npm-install` execution can be added to the frontend plugin executions:

	<!-- Install dependencies as defined within package.json -->
	<execution>
		<id>npm install</id>
		<phase>generate-resources</phase>
		<goals>
			<goal>npm</goal>
		</goals>
		<configuration>
			<arguments>install</arguments>
		</configuration>
	</execution>

NPM will now install dependencies as defined in `package.json` in `node_modules` when you run the maven build, in our case `grunt-ts`, `grunt` and `grunt-cli`.
  
Last step in the build process is kickstart the grunt task runner.

    <!-- Kickstart Grunt task runner executing tasks as defined within GruntFile.js -->
    <execution>
    	<id>grunt build</id>
    	<phase>generate-resources</phase>
    	<goals>
    		<goal>grunt</goal>
    	</goals>
    	<configuration>
    		<arguments>--verbose</arguments>
    	</configuration>
    </execution>

## Grunt tasks

The tasks that need to performed by Grunt are defined within `GruntFile.js`.

    module.exports = function( grunt ){
    
       // tell grunt to load task plugins.
        grunt.loadNpmTasks('grunt-ts');
    
       // configure tasks
       grunt.initConfig({
    
        ts: {
            compile: {
                src: ['src/main/typescript/**/*.ts'],
                outDir: 'target/generated/javascript/',
                options: {
                    module: 'commonjs', //or commonjs
                    target: 'es5', //or es3
                    basePath: 'src/main/typescript',
                    sourceMap: true,
                    declaration: true,
                    experimentalDecorators: true
                }
            },
        }
    
        // more plugin configs go here.
       });
    
       grunt.registerTask('default',['ts']);
    
    };
    
In the above grunt configuration, we define the `ts` task, which compiles all the `*.ts` files within `src/main/typescript/` map and writes the result within `target/generated/javascript`. More plugin configuration can be added to the `options` section. See [grunt-ts](https://github.com/TypeStrong/grunt-ts) for more options. Finally `ts` is added to the `default` task with `grunt.registerTask`.  