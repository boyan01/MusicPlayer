package tech.soit.quiet.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import me.drakeet.multitype.ItemViewBinder
import me.drakeet.multitype.MultiTypeAdapter
import tech.soit.quiet.R


class KViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

annotation class TypeLayoutRes(@LayoutRes val value: Int)

abstract class KItemViewBinder<T> : ItemViewBinder<T, KViewHolder>() {

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): KViewHolder {
        val layoutRes = this::class.annotations.firstOrNull { it is TypeLayoutRes } as TypeLayoutRes?
                ?: throw IllegalStateException("must override this function if you do not use Annotation")

        return KViewHolder(inflater.inflate(layoutRes.value, parent, false))
    }

}


inline fun <reified T, V : RecyclerView.ViewHolder, B : ItemViewBinder<T, V>> MultiTypeAdapter.withBinder(
        binder: B): MultiTypeAdapter {
    register(T::class.java, binder)
    return this
}

fun MultiTypeAdapter.submit(items: List<Any>) {
    this.items = items
    notifyDataSetChanged()
}


/*
 * easy access for loading
 */

@TypeLayoutRes(R.layout.item_loading)
class LoadingViewBinder : KItemViewBinder<Loading>() {

    override fun onBindViewHolder(holder: KViewHolder, item: Loading) {
        //do nothing
    }
}

/**
 * object for [LoadingViewBinder]
 */
object Loading


/**
 * shortcut to register Loading
 */
fun MultiTypeAdapter.withLoadingBinder(): MultiTypeAdapter {
    return withBinder(LoadingViewBinder())
}


fun MultiTypeAdapter.setLoading() {
    items = listOf(Loading)
}

@TypeLayoutRes(R.layout.item_empty)
class EmptyViewBinder : KItemViewBinder<Empty>() {
    override fun onBindViewHolder(holder: KViewHolder, item: Empty) {
        //do nothing
    }

}


object Empty


fun MultiTypeAdapter.withEmptyBinder(): MultiTypeAdapter {
    return withBinder(EmptyViewBinder())
}

fun MultiTypeAdapter.setEmpty() {
    items = listOf(Empty)
}