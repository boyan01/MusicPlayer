package tech.soit.quiet.ui.item

import tech.soit.quiet.R
import tech.soit.typed.adapter.TypedAdapter
import tech.soit.typed.adapter.TypedBinder
import tech.soit.typed.adapter.ViewHolder
import tech.soit.typed.adapter.annotation.TypeLayoutResource

@TypeLayoutResource(R.layout.item_empty)
@Deprecated("")
class EmptyViewBinder : TypedBinder<Empty>() {

    override fun onBindViewHolder(holder: ViewHolder, item: Empty) {
        //do nothing
    }

}

@Deprecated("")
object Empty


/**
 * shortcut to register Empty
 */
fun TypedAdapter.withEmptyBinder(): TypedAdapter {
    return withBinder(Empty::class, EmptyViewBinder())
}

fun TypedAdapter.submitEmpty() {
    submit(listOf(Empty))
}