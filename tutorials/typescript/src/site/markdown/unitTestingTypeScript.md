# Unit testing Type Script

For unit testing type script code we will use [type-unit](https://github.com/basespace/type-unit). TypeUnit enables TypeScript unit testing in the style of xUnit.

First we include `type-unit` and `grunt-mocha-test` within the TypeScript project by adding it to `devDependencies` in `package.json`.

    {
       "name":"tutorial-typescript",
       "version":"0.0.1-SNAPSHOT",
       ...
       "devDependencies": {
          "grunt-mocha-test":"0.12.7",
          "type-unit":"~0.1.5"
       }
    }
    
Secondly we trigger the execution of the unit tests by adding `mochaTest` to the tasks that will be executed during the 
build by Grunt. 

    module.exports = function( grunt ){
    
       // tell grunt to load task plugins.
       ...
       grunt.loadNpmTasks('grunt-mocha-test');
           
       // configure tasks
       grunt.initConfig({
    
            ...
                
            // Configure a mocha task
            mochaTest: {
                test: {
                    options: {
                        reporter: 'spec',
                        captureFile: 'results.txt', // Optionally capture the reporter output to a file
                        quiet: false, // Optionally suppress output to standard out (defaults to false)
                        clearRequireCache: false // Optionally clear the require cache before running tests (defaults to false)
                    },
                    src: ['target/generated/javascript/**/*Test.js']
                }
            }
    
          // more plugin configs go here.
       });
    
       grunt.registerTask('default',['ts','mochaTest']);
    
    };
    
`type-unit` is using decorators to define the unit tests. As of TypeScript 1.5, you must set the `experimentalDecorators` compiler option to true.

To give a short introduction on how the unit tests has to be created with `type-unit` we will test the following class:

    class Calculator {
    
        add(val1: number, val2: number) {
    		return val1 + val2;
    	}
    	
    	subtract(val1: number, val2: number) {
    		return val1 - val2;
    	}
    }
    
A unit tests comprises a `@Suite` of tests, containing individual test function as a `@Fact`. To run a tests with a series of different parameters is a `@Theory`. 

    /// <reference path="/Users/mase/Development/WorkSpace/uniknow/AgileDev/tutorials/typescript/node_modules/type-unit/dist/type-unit.d.ts" />
    /// <reference path="/Users/mase/Development/WorkSpace/uniknow/AgileDev/tutorials/typescript/node_modules/type-unit/dist/index"/>
    import {Suite, Fact, Theory} from 'type-unit';
    import assert = require("assert");
    
    import {Calculator} from "./Calculator";
    
    @Suite("Calculator")
    class CalculatorTests {
    
        private calculator = new Calculator();
    
        @Fact("Add")
        testAdd() {
            assert.equal(this.calculator.add(1,1), 2);
        }
    
        @Theory("Additions", [
          [1, 1, 2],
          [1, -1, 0],
          [-1, -1, -2]
          ])
        testMultipleAdditions(valueOne: number, valueTwo: number, result: number) {
           assert.equal(this.calculator.add(valueOne, valueTwo), result);
        }
    
    }
        
To run the unit test execute the maven build, (`mvn test`)