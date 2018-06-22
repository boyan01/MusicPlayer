package tech.summerly.quiet.netease.ui.items

import android.view.ViewGroup
import kotlinx.android.synthetic.main.netease_header_daily_recommend.view.*
import tech.summerly.quiet.commonlib.utils.GlideApp
import tech.summerly.quiet.commonlib.utils.support.TypedBinder
import tech.summerly.quiet.commonlib.utils.support.ViewHolder
import tech.summerly.quiet.netease.R

class NeteaseDailyHeader(
        val date: String,
        val background: Any? = null
)

class NeteaseDailyHeaderViewBinder : TypedBinder<NeteaseDailyHeader>() {
    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(R.layout.netease_header_daily_recommend, parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, item: NeteaseDailyHeader) = with(holder.itemView) {
        item.background?.let {
            GlideApp.with(this).load(it).into(imageBackground)
        }
        textDay.text = item.date
    }

}