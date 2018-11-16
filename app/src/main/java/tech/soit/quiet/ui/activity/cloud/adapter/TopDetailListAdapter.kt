package tech.soit.quiet.ui.activity.cloud.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import tech.soit.quiet.R
import tech.soit.quiet.repository.netease.source.NeteaseGlideUrl
import tech.soit.quiet.ui.activity.cloud.CloudPlayListDetailActivity
import tech.soit.quiet.ui.adapter.viewholder.BaseViewHolder
import tech.soit.quiet.ui.view.RoundRectOutlineProvider
import tech.soit.quiet.utils.annotation.LayoutId
import tech.soit.quiet.utils.component.ImageLoader
import tech.soit.quiet.utils.component.support.px
import tech.soit.quiet.utils.component.support.string

/**
 * @param list 榜单json数据列表
 */
class TopDetailListAdapter(private val list: JsonArray) : RecyclerView.Adapter<BaseViewHolder>() {

    companion object {

        /**
         * [list] 中官方榜起始位置
         */
        const val INDEX_OFFICIAL = 0

        /**
         * [list] 全球榜起始位置
         */
        const val INDEX_GLOBAL = 5


        private const val TYPE_HEADER = 0
        private const val TYPE_FULL = 1
        private const val TYPE_SIMPLE = 2

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                HeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.header_item_cloud_top, parent, false))
            }
            TYPE_FULL -> {
                FullViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_cloud_top_type_1, parent, false))
            }
            TYPE_SIMPLE -> {
                SimpleViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_cloud_top_type_2, parent, false))
            }
            else -> error("不支持的ViewType : $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            INDEX_OFFICIAL, INDEX_GLOBAL -> TYPE_HEADER
            in INDEX_OFFICIAL..INDEX_GLOBAL -> TYPE_FULL
            else -> TYPE_SIMPLE
        }
    }

    override fun getItemCount(): Int {
        return 2 /*2标题*/ + list.size()
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when (holder.itemViewType) {
            TYPE_HEADER -> {
                holder as HeaderViewHolder
                val title = if (position == 0) string(R.string.leader_board_official) else string(R.string.leader_board_global)
                holder.update(title)
            }
            TYPE_FULL -> {
                holder as FullViewHolder
                val jsonObject = list[position - 1].asJsonObject
                holder.update(jsonObject)
                setBoardClickListener(holder, jsonObject)
            }

            TYPE_SIMPLE -> {
                holder as SimpleViewHolder
                val jsonObject = list[position - 2].asJsonObject
                holder.update(jsonObject)
                setBoardClickListener(holder, jsonObject)
            }
        }
    }

    private fun setBoardClickListener(holder: BaseViewHolder, jsonObject: JsonObject) {
        with(holder.itemView) {
            val id = jsonObject["id"].asLong
            setOnClickListener {
                val intent = Intent(context, CloudPlayListDetailActivity::class.java)
                intent.putExtra(CloudPlayListDetailActivity.PARAM_ID, id)
                context.startActivity(intent)
            }
        }
    }


    @LayoutId(R.layout.item_cloud_top_type_1)
    private class FullViewHolder(itemView: View) : BaseViewHolder(itemView) {

        init {
            val view: View = itemView.findViewById(R.id.imageCover)
            view.outlineProvider = RoundRectOutlineProvider(3.px.toFloat())
            view.clipToOutline = true
        }

        fun update(jsonObject: JsonObject) = with(itemView) {
            findViewById<AppCompatTextView>(R.id.textUpdateFrequency).text = jsonObject["updateFrequency"].asString
            val url = NeteaseGlideUrl(jsonObject["coverImgUrl"].asString)
            ImageLoader.with(this).load(url).into(findViewById(R.id.imageCover))

            val tracks = jsonObject["tracks"].asJsonArray

            findViewById<AppCompatTextView>(R.id.textSong1).text = getTrack(tracks[0].asJsonObject)
            findViewById<AppCompatTextView>(R.id.textSong2).text = getTrack(tracks[1].asJsonObject)
            findViewById<AppCompatTextView>(R.id.textSong3).text = getTrack(tracks[2].asJsonObject)
        }

        private fun getTrack(jsonObject: JsonObject): String {
            return "%s - %s".format(jsonObject["first"].asString, jsonObject["second"].asString)
        }

    }

    @LayoutId(R.layout.item_cloud_top_type_2)
    private class SimpleViewHolder(itemView: View) : BaseViewHolder(itemView) {

        init {
            val view: View = itemView.findViewById(R.id.imageCover)
            view.outlineProvider = RoundRectOutlineProvider(3.px.toFloat())
            view.clipToOutline = true
        }

        fun update(jsonObject: JsonObject) = with(itemView) {

            findViewById<AppCompatTextView>(R.id.textUpdateFrequency).text = jsonObject["updateFrequency"].asString

            val url = NeteaseGlideUrl(jsonObject["coverImgUrl"].asString)
            ImageLoader.with(this).load(url).into(findViewById(R.id.imageCover))

            findViewById<TextView>(R.id.textTitle).text = jsonObject["name"].asString
        }

    }

    @LayoutId(R.layout.header_item_cloud_top)
    private class HeaderViewHolder(itemView: View) : BaseViewHolder(itemView) {

        fun update(title: String) = with(itemView) {
            findViewById<TextView>(R.id.textTitle).text = title
        }
    }


}