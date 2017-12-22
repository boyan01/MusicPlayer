package tech.summerly.quiet.commonlib.utils

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.drakeet.multitype.ItemViewBinder as JItemViewBinder
import me.drakeet.multitype.MultiTypeAdapter

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

    abstract override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder

    open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}