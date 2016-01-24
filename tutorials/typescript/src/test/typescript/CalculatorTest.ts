import {Suite, Fact, Theory} from 'type-unit';
import assert = require("assert");

import {Calculator} from "../../main/typescript/Calculator";
import {UnitTest} from "./UnitTest";

@Suite("Calculator")
class CalculatorTests extends UnitTest {

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