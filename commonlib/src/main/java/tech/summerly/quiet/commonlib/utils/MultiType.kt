package tech.summerly.quiet.commonlib.utils

import android.support.annotation.LayoutRes
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import me.drakeet.multitype.MultiTypeAdapter
import me.drakeet.multitype.ItemViewBinder as JItemViewBinder

/**
 * Created by summer on 17-12-17
 */
val RecyclerView.multiTypeAdapter: MultiTypeAdapter
    get() = adapter as? MultiTypeAdapter
            ?: throw IllegalStateException("must set multiType adapter first!")

/**
 * author : summerly
 * e-mail : yangbinyhbn@gmail.com
 * time   : 2017/7/22
 * desc   : ItemViewBinder的Kotlin封装类
 */
abstract class ItemViewBinder<T> : me.drakeet.multitype.ItemViewBinder<T, ItemViewBinder.ViewHolder>() {

    public abstract override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder

    public abstract override fun onBindViewHolder(holder: ViewHolder, item: T)

    open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        companion object {
            operator fun invoke(@LayoutRes layoutId: Int, parent: ViewGroup, inflater: LayoutInflater): ViewHolder {
                return ViewHolder(inflater.inflate(layoutId, parent, false))
            }
        }
    }
}

abstract class ItemViewBinder2<T> : ItemViewBinder<T>() {

    protected abstract val layoutId: Int

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        return ViewHolder(layoutId, parent, inflater)
    }

}

fun MultiTypeAdapter.setItemsByDiff(items: List<Any>, detectMove: Boolean = false) {
    @Suppress("UNCHECKED_CAST")//fuck type checker
    val old = this.items as MutableList<Any>
    launch {
        val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return old[oldItemPosition] == items[newItemPosition]
            }

            override fun getOldListSize(): Int = old.size

            override fun getNewListSize(): Int = items.size

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return old[oldItemPosition] == items[newItemPosition]
            }
        }, detectMove)
        launch(UI) {
            old.clear()
            old.addAll(items)
            result.dispatchUpdatesTo(this@setItemsByDiff)
        }
    }
}


fun MultiTypeAdapter.setItems2(items: List<*>,
                               detectDiff: Boolean = true,
                               detectMove: Boolean = false,
                               isContentsTheSame: (old: Any?, new: Any?) -> Boolean = { old, new -> old == new }) {
    if (!detectDiff) {
        this.items = items
        notifyDataSetChanged()
        return
    }
    async {
        val old = this@setItems2.items
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return old[oldItemPosition] == items[newItemPosition]
            }

            override fun getOldListSize(): Int = old.size

            override fun getNewListSize(): Int = items.size

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return isContentsTheSame(old[oldItemPosition], items[newItemPosition])
            }

        }, detectMove)
        asyncUI {
            this@setItems2.items = items
            diffResult.dispatchUpdatesTo(this@setItems2)
        }
    }

}