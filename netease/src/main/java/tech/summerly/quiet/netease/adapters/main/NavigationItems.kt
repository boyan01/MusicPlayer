package tech.summerly.quiet.netease.adapters.main

import android.view.View
import kotlinx.android.synthetic.main.netease_content_nav_item.view.*
import kotlinx.android.synthetic.main.netease_item_navigation.view.*
import tech.summerly.quiet.commonlib.utils.support.SimpleTypedBinder
import tech.summerly.quiet.commonlib.utils.support.ViewHolder
import tech.summerly.quiet.netease.R


internal object Navigation {


    val fm = R.string.netease_nav_title_fm to R.drawable.netease_ic_radio_black_24dp

    val daily = R.string.netease_nav_title_daily to R.drawable.netease_ic_today_black_24dp

    val download = R.string.netease_nav_title_download to R.drawable.netease_ic_file_download_black_24dp

    val record = R.string.netease_nav_title_latest to R.drawable.netease_ic_access_time_black_24dp

}

internal class NavigationViewBinder : SimpleTypedBinder<Navigation>() {

    override val layoutId: Int get() = R.layout.netease_item_navigation

    override fun onBindViewHolder(holder: ViewHolder, item: Navigation) = with(holder.itemView) {
        navFm.setData(Navigation.fm)
        navDaily.setData(Navigation.daily)
        navDownload.setData(Navigation.download)
        navRecord.setData(Navigation.record)
    }

    private fun View.setData(pair: Pair<Int, Int>) {
        text.setText(pair.first)
        image.setImageResource(pair.second)
    }

}