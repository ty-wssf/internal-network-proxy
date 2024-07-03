/**
 * @jest-environment node
 */

import couldBeClass from "src/utils/couldBeClass";
import { formatClass, formatStyle } from "src/utils/styleClassTransformer";
import parseVModel from "src/utils/parseVModel";

describe('Test utils', () => {

  test('Test couldBeClass.js', () => {
    expect(couldBeClass({})).toBe(false)
    expect(couldBeClass(112)).toBe(false)
    expect(couldBeClass('1212')).toBe(false)
    expect(couldBeClass(null)).toBe(false)
    expect(couldBeClass(undefined)).toBe(false)
    expect(couldBeClass(function () {})).toBe(false)
    expect(couldBeClass(() => {})).toBe(false)
    expect(couldBeClass(async function () {})).toBe(false)
    expect(couldBeClass(class{})).toBe(true)

    let fun = () => {}
    fun.prototype = undefined
    expect(couldBeClass(fun)).toBe(false)

    fun = function() {}
    fun.prototype.constructor = null
    expect(couldBeClass(fun)).toBe(false)
  })

  test('Test styleClassTransformer.js', () => {
    expect(formatClass({
      'AAA': true,
      'BBB': false
    }).toString()).toBe('AAA')
    expect(formatClass('aaaa bbbb').toString()).toBe('aaaa,bbbb')
    expect(formatClass([]).toString()).toBe('')
    expect(formatClass(1212).toString()).toBe('')

    expect(JSON.stringify(formatStyle('color: red; font-weight: bold ; aaa; '))).toBe('{"color":"red","fontWeight":"bold"}')
    expect(JSON.stringify(formatStyle(1212))).toBe('{}')
  })

  test('Test parseVModel\'s exception', () => {
    expect(() => {
      parseVModel({
        'v-model': [1, null]
      })
    }).toThrow()
    expect(() => {
      parseVModel({
        'v-model': null
      })
    }).toThrow()
    expect(() => {
      parseVModel({
        'v-models': 1
      })
    }).toThrow()
  })
})
