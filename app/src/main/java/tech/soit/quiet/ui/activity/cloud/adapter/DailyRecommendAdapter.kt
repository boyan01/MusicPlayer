package tech.soit.quiet.ui.activity.cloud.adapter

import android.widget.TextView
import tech.soit.quiet.R
import tech.soit.quiet.model.vo.Music
import tech.soit.quiet.ui.adapter.MusicListAdapter
import tech.soit.quiet.utils.KItemViewBinder
import tech.soit.quiet.utils.KViewHolder
import tech.soit.quiet.utils.TypeLayoutRes
import tech.soit.quiet.utils.withBinder
import java.util.*

class DailyRecommendAdapter : MusicListAdapter(TOKEN) {
    companion object {
        private const val TOKEN = "netease_daily_recommend"
    }

    init {
        withBinder(DailyHeaderBinder())
    }

    override fun buildShowList(musics: List<Music>, isShowSubscribeButton: Boolean, isSubscribed: Boolean): ArrayList<Any> {
        val list = super.buildShowList(musics, isShowSubscribeButton, isSubscribed)

        //添加推荐头
        list.add(0, DailyRecommendHeader)
        return list
    }


    object DailyRecommendHeader

    @TypeLayoutRes(R.layout.item_header_daily_recommend)
    class DailyHeaderBinder : KItemViewBinder<DailyRecommendHeader>() {

        override fun onBindViewHolder(holder: KViewHolder, item: DailyRecommendHeader) {
            holder.itemView.findViewById<TextView>(R.id.textDay).apply {
                text = Calendar.getInstance().get(Calendar.DAY_OF_MONTH).toString()
            }
        }

    }


}