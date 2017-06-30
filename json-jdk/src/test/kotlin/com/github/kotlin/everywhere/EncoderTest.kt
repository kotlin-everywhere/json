package com.github.kotlin.everywhere

import com.github.kotlin.everywhere.json.encode.Encoders.array
import com.github.kotlin.everywhere.json.encode.Encoders.bool
import com.github.kotlin.everywhere.json.encode.Encoders.float
import com.github.kotlin.everywhere.json.encode.Encoders.int
import com.github.kotlin.everywhere.json.encode.Encoders.long
import com.github.kotlin.everywhere.json.encode.Encoders.nul
import com.github.kotlin.everywhere.json.encode.Encoders.nullable
import com.github.kotlin.everywhere.json.encode.Encoders.object_
import com.github.kotlin.everywhere.json.encode.Encoders.string
import com.github.kotlin.everywhere.json.encode.encode
import org.junit.Assert.assertEquals
import org.junit.Test

class EncoderTest {
    @Test
    fun testString() {
        assertEquals("\"hello\"", encode(string("hello")))
    }

    @Test
    fun testInt() {
        assertEquals("42", encode(int(42)))
    }

    @Test
    fun testLong() {
        assertEquals("42", encode(long(42)))
    }

    @Test
    fun testFloat() {
        assertEquals("3.14", encode(float(3.14f)))
    }

    @Test
    fun testBool() {
        assertEquals("false", encode(bool(false)))
    }

    @Test
    fun testNull() {
        assertEquals("null", encode(nul))
    }

    @Test
    fun testObject() {
        assertEquals(
                """{"message":"hello","age":42}""",
                encode(object_("message" to string("hello"), "age" to int(42)))
        )
    }

    @Test
    fun testArray() {
        assertEquals("[1,2,3]", encode(array(listOf(1, 2, 3).map(int))))
    }

    @Test
    fun testNullable() {
        assertEquals("1", encode(nullable(int)(1)))
        assertEquals("null", encode(nullable(int)(null)))
    }
}