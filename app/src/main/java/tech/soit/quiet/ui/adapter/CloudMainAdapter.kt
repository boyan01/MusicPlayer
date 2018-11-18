package tech.soit.quiet.ui.adapter

import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import me.drakeet.multitype.MultiTypeAdapter
import tech.soit.quiet.R
import tech.soit.quiet.repository.netease.source.NeteaseGlideUrl
import tech.soit.quiet.ui.activity.cloud.CloudDailyRecommendActivity
import tech.soit.quiet.ui.activity.cloud.CloudPlayListDetailActivity
import tech.soit.quiet.ui.activity.cloud.TopDetailActivity
import tech.soit.quiet.ui.adapter.viewholder.CloudMainNav2ViewHolder
import tech.soit.quiet.ui.view.CircleOutlineProvider
import tech.soit.quiet.utils.KItemViewBinder
import tech.soit.quiet.utils.KViewHolder
import tech.soit.quiet.utils.TypeLayoutRes
import tech.soit.quiet.utils.component.log
import tech.soit.quiet.utils.component.support.value
import tech.soit.quiet.utils.withBinder

/**
 * associated with [tech.soit.quiet.ui.fragment.home.MainCloudFragment]
 */
class CloudMainAdapter : MultiTypeAdapter() {


    companion object {

        const val SPAN_COUNT = 3

        private val NAVIGATORS = listOf(
                ItemNavigator(R.string.nav_radio, R.drawable.ic_radio_black_24dp),
                ItemNavigator(R.string.nav_daily_recommend, R.drawable.ic_today_black_24dp),
                ItemNavigator(R.string.nav_tends, R.drawable.ic_show_chart_black_24dp)
        )

        private val HEADER_RECOMMEND_PLAYLIST = ItemHeader(R.string.recommend_playlists)

    }


    init {
        withBinder(ItemNavigatorBinder())
        withBinder(ItemHeaderBinder())
        withBinder(ItemPlaylistBinder())
    }


    private var playlist: JsonArray? = null

    /**
     * 推荐歌单
     */
    fun setRecommendPlaylist(playlist: JsonArray) {
        this.playlist = playlist
        refresh()
    }

    /**
     * 刷新列表
     */
    fun refresh() {
        val items = ArrayList<Any>()

        items.addAll(NAVIGATORS)//导航

        //推荐歌单
        playlist?.let { playlist ->
            val itemPlaylist = playlist.take(6).map { ItemPlaylist(it as JsonObject) }
            items.add(HEADER_RECOMMEND_PLAYLIST)
            items.addAll(itemPlaylist)
        }

        this.items = items
        notifyDataSetChanged()
    }


    class ItemNavigator(
            @StringRes val title: Int,
            @DrawableRes val icon: Int
    )

    @TypeLayoutRes(R.layout.item_cloud_nav)
    private class ItemNavigatorBinder : KItemViewBinder<ItemNavigator>() {

        private val circleOutlineProvider = CircleOutlineProvider()

        override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): KViewHolder {
            val holder = super.onCreateViewHolder(inflater, parent)
            holder.itemView.findViewById<View>(R.id.imageIcon).apply {
                outlineProvider = circleOutlineProvider
                clipToOutline = true
            }
            return holder
        }

        override fun onBindViewHolder(holder: KViewHolder, item: ItemNavigator) {
            with(holder.itemView) {
                val imageIcon: ImageView = findViewById(R.id.imageIcon)
                val textTitle: TextView = findViewById(R.id.textTitle)
                imageIcon.setImageResource(item.icon)
                textTitle.setText(item.title)
                setOnClickListener {
                    when (item.title) {
                        R.string.nav_radio -> {
                            log { "to radio" }
                        }
                        R.string.nav_daily_recommend -> {
                            context.startActivity(Intent(context, CloudDailyRecommendActivity::class.java))
                        }
                        R.string.nav_tends -> {
                            context.startActivity(Intent(context, TopDetailActivity::class.java))
                        }
                    }
                }
            }
        }
    }


    class ItemHeader(
            @StringRes val title: Int
    )

    @TypeLayoutRes(R.layout.header_item_cloud_main)
    private class ItemHeaderBinder : KItemViewBinder<ItemHeader>() {

        override val spanSize: Int
            get() = SPAN_COUNT

        override fun onBindViewHolder(holder: KViewHolder, item: ItemHeader) = holder.itemView.run {
            findViewById<TextView>(R.id.textTitle).setText(item.title)
        }
    }

    class ItemPlaylist(jsonObject: JsonObject) {

        val id: Long = jsonObject["id"].asLong

        val playCount: Long = jsonObject["playCount"].asLong

        val picUrl: NeteaseGlideUrl = NeteaseGlideUrl(jsonObject["picUrl"].asString)

        val name: String = jsonObject["name"].value() ?: ""

        val copywriter: String? = jsonObject["copywriter"].value()
    }

    private class ItemPlaylistBinder : KItemViewBinder<ItemPlaylist>() {

        override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): KViewHolder {
            return CloudMainNav2ViewHolder(inflater.inflate(R.layout.item_cloud_nav_2, parent, false))
        }

        override fun onBindViewHolder(holder: KViewHolder, item: ItemPlaylist) {
            holder as CloudMainNav2ViewHolder
            holder.setIsRightTopVisible(true)

            holder.setPlayCount(item.playCount)
            holder.set(item.name, item.picUrl)
            holder.itemView.setOnClickListener {
                val intent = Intent(it.context, CloudPlayListDetailActivity::class.java)
                intent.putExtra(CloudPlayListDetailActivity.PARAM_ID, item.id)
                it.context.startActivity(intent)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                holder.itemView.tooltipText = item.copywriter
            }
        }

    }


}