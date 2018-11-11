package tech.soit.quiet.ui.fragment.home.cloud

import android.content.Intent
import android.view.View
import kotlinx.android.synthetic.main.item_play_list.view.*
import tech.soit.quiet.R
import tech.soit.quiet.model.vo.PlayListDetail
import tech.soit.quiet.ui.activity.cloud.CloudPlayListDetailActivity
import tech.soit.quiet.ui.view.RoundRectOutlineProvider
import tech.soit.quiet.utils.KItemViewBinder
import tech.soit.quiet.utils.KViewHolder
import tech.soit.quiet.utils.TypeLayoutRes
import tech.soit.quiet.utils.component.ImageLoader
import tech.soit.quiet.utils.component.support.px
import tech.soit.quiet.utils.component.support.string

/**
 * cloud fragment 主页面的歌单列表 item
 */
@TypeLayoutRes(R.layout.item_play_list)
class PlayListViewBinder : KItemViewBinder<PlayListDetail>() {

    private val outlineProvider = RoundRectOutlineProvider(3.px.toFloat())

    override fun onViewCreated(view: View) {
        view.imageCover.apply {
            outlineProvider = this@PlayListViewBinder.outlineProvider
            clipToOutline = true
        }
    }

    override fun onBindViewHolder(holder: KViewHolder, item: PlayListDetail) {
        with(holder.itemView) {
            ImageLoader.with(this).load(item.getCoverUrl()).into(imageCover)
            textTitle.text = item.getName()
            textSubTitle.text = string(R.string.template_item_play_list_count, item.getTrackCount())
            setOnClickListener {
                val intent = Intent(context, CloudPlayListDetailActivity::class.java)
                intent.putExtra("id", item.getId())
                intent.putExtra(CloudPlayListDetailActivity.PARAM_PLAY_LIST, item)
                context.startActivity(intent)
            }
        }
    }

}