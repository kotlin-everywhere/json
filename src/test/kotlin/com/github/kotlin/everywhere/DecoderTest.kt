package com.github.kotlin.everywhere

import com.github.kotlin.everywhere.json.decode.Decoders.boolean
import com.github.kotlin.everywhere.json.decode.Decoders.field
import com.github.kotlin.everywhere.json.decode.Decoders.float
import com.github.kotlin.everywhere.json.decode.Decoders.int
import com.github.kotlin.everywhere.json.decode.Decoders.long
import com.github.kotlin.everywhere.json.decode.Decoders.nul
import com.github.kotlin.everywhere.json.decode.Decoders.nullable
import com.github.kotlin.everywhere.json.decode.Decoders.string
import com.github.kotlin.everywhere.json.decode.Decoders.unit
import com.github.kotlin.everywhere.json.decode.Err
import com.github.kotlin.everywhere.json.decode.Ok
import com.github.kotlin.everywhere.json.decode.decodeString
import com.github.kotlin.everywhere.json.decode.map
import org.junit.Assert.assertEquals
import org.junit.Test

class DecoderTest {
    @Test
    fun testString() {
        assertEquals(Err.of("Expecting a String but instead got: null"), decodeString(string, "null"))
        assertEquals(Err.of("Expecting a String but instead got: true"), decodeString(string, "true"))
        assertEquals(Err.of("Expecting a String but instead got: 42"), decodeString(string, "42"))
        assertEquals(Err.of("Expecting a String but instead got: 3.14"), decodeString(string, "3.14"))
        assertEquals(Ok.of("hello"), decodeString(string, "\"hello\""))
        assertEquals(Err.of("Expecting a String but instead got: {\"hello\":42}"), decodeString(string, "{ \"hello\": 42 }"))
    }

    @Test
    fun testBoolean() {
        assertEquals(Err.of("Expecting a Boolean but instead got: null"), decodeString(boolean, "null"))
        assertEquals(Ok.of(true), decodeString(boolean, "true"))
        assertEquals(Err.of("Expecting a Boolean but instead got: 42"), decodeString(boolean, "42"))
        assertEquals(Err.of("Expecting a Boolean but instead got: 3.14"), decodeString(boolean, "3.14"))
        assertEquals(Err.of("Expecting a Boolean but instead got: \"hello\""), decodeString(boolean, "\"hello\""))
        assertEquals(Err.of("Expecting a Boolean but instead got: {\"hello\":42}"), decodeString(boolean, "{ \"hello\": 42 }"))
    }

    @Test
    fun testInt() {
        assertEquals(Err.of("Expecting a Int but instead got: null"), decodeString(int, "null"))
        assertEquals(Err.of("Expecting a Int but instead got: true"), decodeString(int, "true"))
        assertEquals(Ok.of(42), decodeString(int, "42"))
        assertEquals(Err.of("Expecting a Int but instead got: 3.14"), decodeString(int, "3.14"))
        assertEquals(Err.of("Expecting a Int but instead got: \"hello\""), decodeString(int, "\"hello\""))
        assertEquals(Err.of("Expecting a Int but instead got: {\"hello\":42}"), decodeString(int, "{ \"hello\": 42 }"))
    }

    @Test
    fun testLong() {
        assertEquals(Err.of("Expecting a Long but instead got: null"), decodeString(long, "null"))
        assertEquals(Err.of("Expecting a Long but instead got: true"), decodeString(long, "true"))
        assertEquals(Ok.of(42L), decodeString(long, "42"))
        assertEquals(Err.of("Expecting a Long but instead got: 3.14"), decodeString(long, "3.14"))
        assertEquals(Err.of("Expecting a Long but instead got: \"hello\""), decodeString(long, "\"hello\""))
        assertEquals(Err.of("Expecting a Long but instead got: {\"hello\":42}"), decodeString(long, "{ \"hello\": 42 }"))
    }

    @Test
    fun testFloat() {
        assertEquals(Err.of("Expecting a Float but instead got: null"), decodeString(float, "null"))
        assertEquals(Err.of("Expecting a Float but instead got: true"), decodeString(float, "true"))
        assertEquals(Err.of("Expecting a Float but instead got: 42"), decodeString(float, "42"))
        assertEquals(Ok.of(3.14f), decodeString(float, "3.14"))
        assertEquals(Err.of("Expecting a Float but instead got: \"hello\""), decodeString(float, "\"hello\""))
        assertEquals(Err.of("Expecting a Float but instead got: {\"hello\":42}"), decodeString(float, "{ \"hello\": 42 }"))
    }

    @Test
    fun testNull() {
        val nullInt = nul<Int>()
        assertEquals(Ok.of(null), decodeString(nullInt, "null"))
        assertEquals(Err.of("Expecting a Null but instead got: true"), decodeString(nullInt, "true"))
        assertEquals(Err.of("Expecting a Null but instead got: 42"), decodeString(nullInt, "42"))
        assertEquals(Err.of("Expecting a Null but instead got: 3.14"), decodeString(nullInt, "3.14"))
        assertEquals(Err.of("Expecting a Null but instead got: \"hello\""), decodeString(nullInt, "\"hello\""))
        assertEquals(Err.of("Expecting a Null but instead got: {\"hello\":42}"), decodeString(nullInt, "{ \"hello\": 42 }"))
    }

    @Test
    fun testUnit() {
        assertEquals(Ok.of(Unit), decodeString(unit, "\"Unit\""))
        assertEquals(Err.of("Expecting a Unit but instead got: true"), decodeString(unit, "true"))
        assertEquals(Err.of("Expecting a Unit but instead got: 42"), decodeString(unit, "42"))
        assertEquals(Err.of("Expecting a Unit but instead got: 3.14"), decodeString(unit, "3.14"))
        assertEquals(Err.of("Expecting a Unit but instead got: \"hello\""), decodeString(unit, "\"hello\""))
        assertEquals(Err.of("Expecting a Unit but instead got: {\"hello\":42}"), decodeString(unit, "{ \"hello\": 42 }"))
    }

    @Test
    fun testNullable() {
        assertEquals(Ok.of(null), decodeString(nullable(int), "null"))
        assertEquals(Err.of("Expecting a Int but instead got: true"), decodeString(int, "true"))
    }

    @Test
    fun testField() {
        assertEquals(Ok.of(3), decodeString(field("x", int), "{ \"x\": 3 }"))
        assertEquals(Ok.of(3), decodeString(field("x", int), "{ \"x\": 3, \"y\": 4 }"))
        assertEquals(Err.of("Expecting a Int but instead got: true"), decodeString(field("x", int), "{ \"x\": true }"))
        assertEquals(Err.of("Expecting an object with a field named `x` but instead got: {\"y\":4}"), decodeString(field("x", int), "{ \"y\": 4 }"))

        assertEquals(Ok.of("tom"), decodeString(field("name", string), "{ \"name\": \"tom\" }"))
    }

    @Test
    fun map1() {
        assertEquals(Ok.of("life"), decodeString(map(int) { if (it == 42) "life" else "no life" }, "42"))
        assertEquals(Ok.of("no life"), decodeString(map(int) { if (it == 42) "life" else "no life" }, "41"))
    }

    @Test
    fun map2() {
        data class Point(val x: Int, val y: Int)

        val decoder = map(field("x", int), field("y", int), ::Point)
        assertEquals(Ok.of(Point(x = 3, y = 4)), decodeString(decoder, """{ "x": 3, "y": 4 }"""))
    }

    @Test
    fun map3() {
        data class Point(val x: Int, val y: Int, val z: Int)

        val decoder = map(field("x", int), field("y", int), field("z", int), ::Point)
        assertEquals(Ok.of(Point(x = 3, y = 4, z = 5)), decodeString(decoder, """{ "x": 3, "y": 4, "z": 5 }"""))
    }
}
