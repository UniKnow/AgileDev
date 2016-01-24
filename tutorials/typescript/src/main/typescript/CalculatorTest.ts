/// <reference path="/Users/mase/Development/WorkSpace/uniknow/AgileDev/tutorials/typescript/node_modules/type-unit/dist/type-unit.d.ts" />
/// <reference path="/Users/mase/Development/WorkSpace/uniknow/AgileDev/tutorials/typescript/node_modules/type-unit/dist/index"/>
import typeunit = require("type-unit");
import assert = require("assert");

import {Calculator} from "./Calculator";

var Fact = typeunit.Fact,
    Theory = typeunit.Theory,
    Suite = typeunit.Suite;

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