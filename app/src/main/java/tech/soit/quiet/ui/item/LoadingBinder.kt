package tech.soit.quiet.ui.item

import tech.soit.quiet.R
import tech.soit.typed.adapter.TypedAdapter
import tech.soit.typed.adapter.TypedBinder
import tech.soit.typed.adapter.ViewHolder
import tech.soit.typed.adapter.annotation.TypeLayoutResource

/**
 * show a load view for RecyclerView
 */
@TypeLayoutResource(R.layout.item_loading)
@Deprecated("")
class LoadingViewBinder : TypedBinder<Loading>() {

    override fun onBindViewHolder(holder: ViewHolder, item: Loading) {
        //do nothing
    }
}

/**
 * object for [LoadingViewBinder]
 */
@Deprecated("")
object Loading


/**
 * shortcut to register Loading
 */
fun TypedAdapter.withLoadingBinder(): TypedAdapter {
    return withBinder(Loading::class, LoadingViewBinder())
}