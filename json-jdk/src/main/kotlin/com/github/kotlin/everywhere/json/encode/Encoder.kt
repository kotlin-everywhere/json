package com.github.kotlin.everywhere.json.encode

import com.google.gson.*

typealias Value = JsonElement

typealias Encoder<T> = (T) -> Value

object Encoders {
    val string: Encoder<String> = ::JsonPrimitive
    val int: Encoder<Int> = ::JsonPrimitive
    val long: Encoder<Long> = ::JsonPrimitive
    val float: Encoder<Float> = ::JsonPrimitive
    val bool: Encoder<Boolean> = ::JsonPrimitive
    val nul: Value = JsonNull.INSTANCE
    val unit: Value = JsonPrimitive("Unit")

    fun object_(vararg fields: Pair<String, Value>): Value {
        return fields.fold(JsonObject()) { obj, (name, value) -> obj.add(name, value); obj }
    }

    val array: Encoder<Collection<Value>> = {
        it.fold(JsonArray()) { arr, value -> arr.add(value); arr }
    }

    fun <T> nullable(encoder: Encoder<T>): Encoder<T?> {
        return {
            if (it != null) encoder(it)
            else nul
        }
    }
}

private val gson = GsonBuilder().create()!!

fun encode(value: Value): String {
    return gson.toJson(value)
}