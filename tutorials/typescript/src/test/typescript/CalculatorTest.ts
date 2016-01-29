import {Suite, Fact, Theory} from 'type-unit';
import assert = require("assert");

///ts:import=Example
import Example = require('../../main/typescript/Example'); ///ts:import:generated

///ts:import=UnitTest
import UnitTest = require('./UnitTest'); ///ts:import:generated

@Suite("Calculator")
class CalculatorTests extends UnitTest {

    private calculator = new Example.Calculator();

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