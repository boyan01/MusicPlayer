package tech.summerly.quiet.commonlib.utils

import android.support.annotation.LayoutRes
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.alibaba.android.arouter.launcher.ARouter
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import me.drakeet.multitype.MultiTypeAdapter
import tech.summerly.quiet.commonlib.R
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberFunctions
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

private val sUnImplementedItemViewBinder = UnImplementedItemViewBinder()

class RemoteItemBinderWrapper<T>(
        private val binder: ItemViewBinder<T>?
) : ItemViewBinder<T>() {

    companion object {

        fun <T> withPath(path: String): RemoteItemBinderWrapper<T> {
            val obj = ARouter.getInstance().build(path).navigation()
            @Suppress("UNCHECKED_CAST")
            return RemoteItemBinderWrapper<T>(obj as ItemViewBinder<T>)
        }

    }

    fun setAdapter(adapter: MultiTypeAdapter) {
        if (binder == null) {
            return
        }
        try {
            val clazz = me.drakeet.multitype.ItemViewBinder::class.java
            val field = clazz.getDeclaredField("adapter")
            field.isAccessible = true
            field.set(binder, adapter)
        } catch (e: Exception) {
            log { e.printStackTrace() }
        }

    }

    /**
     * 反射的封装，便于调用远程方法
     */
    fun invoke(methodName: String, vararg parameter: Any) {
        binder ?: return
        try {
            val function = binder::class.declaredMemberFunctions.find { it.name == methodName }
            if (function == null) {
                log(LoggerLevel.ERROR) { "can not find function $methodName" }
                return
            }
            function.call(binder, *parameter)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        return if (binder == null) {
            sUnImplementedItemViewBinder.onCreateViewHolder(inflater, parent)
        } else {
            binder.onCreateViewHolder(inflater, parent)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, item: T) {
        binder?.onBindViewHolder(holder, item)
    }
}


private class UnImplementedItemViewBinder : ItemViewBinder<Any>() {
    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        val textView = TextView(parent.context)
        textView.setText(R.string.unimplemented_view)
        return ViewHolder(textView)
    }

    override fun onBindViewHolder(holder: ViewHolder, item: Any) {
        //do nothing
    }

}
