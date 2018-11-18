package tech.soit.quiet.utils.component.support

import com.google.gson.JsonElement
import com.google.gson.JsonNull
import java.math.BigDecimal
import java.math.BigInteger

inline fun <reified T> JsonElement.value(): T? {
    if (this is JsonNull) {
        return null
    }

    return try {
        val a: Any? = when (T::class.java) {
            Boolean::class.java -> asBoolean
            Short::class.java -> asShort
            Char::class.java -> asCharacter
            String::class.java -> asString
            Float::class.java -> asFloat
            Double::class.java -> asDouble
            BigDecimal::class.java -> asBigDecimal
            BigInteger::class.java -> asBigDecimal
            Number::class.java -> asNumber
            else -> null
        }
        a as? T
    } catch (e: Exception) {
        null
    }
}