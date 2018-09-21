package tech.soit.quiet.utils

import tech.soit.quiet.ui.item.Empty
import tech.soit.quiet.ui.item.Loading
import tech.soit.typed.adapter.TypedAdapter
import kotlin.reflect.KClass

fun TypedAdapter.submitOnly(any: Any) {
    submit(listOf(any))
}

fun List<Any>.hasElementAssignableFrom(cls: KClass<*>): Boolean {
    return this.any { it.javaClass.isAssignableFrom(cls.java) }
}

fun TypedAdapter.clearStatus() {
    val data = ArrayList(list)
    data.removeAll { it is Empty || it is Loading }
    submit(data)
}