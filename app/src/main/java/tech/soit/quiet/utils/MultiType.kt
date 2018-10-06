package tech.soit.quiet.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import me.drakeet.multitype.ItemViewBinder
import me.drakeet.multitype.MultiTypeAdapter


class KViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

annotation class TypeLayoutRes(@LayoutRes val value: Int)

abstract class KItemViewBinder<T> : ItemViewBinder<T, KViewHolder>() {

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): KViewHolder {
        val layoutRes = this::class.annotations.firstOrNull { it is TypeLayoutRes } as TypeLayoutRes?
                ?: throw IllegalStateException("music override this function if you do not use Annotation")

        return KViewHolder(inflater.inflate(layoutRes.value, parent, false))
    }

}


inline fun <reified T, V : RecyclerView.ViewHolder, B : ItemViewBinder<T, V>> MultiTypeAdapter.withBinder(
        binder: B): MultiTypeAdapter {
    register(T::class.java, binder)
    return this
}

fun MultiTypeAdapter.submitItems(items: List<Any>) {
    this.items = items
}