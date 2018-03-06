package tech.summerly.quiet.search.fragments.items

import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.search_item_history.view.*
import tech.summerly.quiet.commonlib.utils.ItemViewBinder
import tech.summerly.quiet.search.R

/**
 * Created by summer on 18-3-6
 */
internal class History(
        var text: String,
        var timeStamp: Long) {
    constructor() : this("", 0)
}


internal class SearchHistoryViewBinder(
        private val itemClick: (history: History) -> Unit,
        private val itemRemove: (history: History, position: Int) -> Unit
) : ItemViewBinder<History>() {
    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        return ViewHolder(R.layout.search_item_history, parent, inflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, item: History) = with(holder.itemView) {
        textTitle.text = item.text
        setOnClickListener { itemClick(item) }
        buttonRemove.setOnClickListener { itemRemove(item, holder.adapterPosition) }
    }
}