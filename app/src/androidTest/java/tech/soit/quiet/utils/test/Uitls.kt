package tech.soit.quiet.utils.test

import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible

/**
 * @author : summer
 * @date : 18-8-29
 */

fun <T> Any.getPropertyValue(name: String): T {
    val find = this::class.declaredMemberProperties.find { it.name == name }!!

    @Suppress("UNCHECKED_CAST")
    find as KProperty1<Any, T>
    find.isAccessible = true

    return find.get(this)

}