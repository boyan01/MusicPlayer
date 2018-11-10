package tech.soit.quiet.ui.adapter.viewholder

import android.view.View
import androidx.core.view.isGone
import kotlinx.android.synthetic.main.header_music_list.view.*
import tech.soit.quiet.R
import tech.soit.quiet.utils.component.support.setOnClickListenerAsync
import tech.soit.quiet.utils.component.support.string

class MusicListHeaderViewHolder(itemView: View) : BaseViewHolder(itemView) {


    fun setHeader(count: Int,
                  showSubscribeButton: Boolean,
                  subscribed: Boolean) = with(itemView) {
        textCollection.isGone = !showSubscribeButton
        textCollection.setOnClickListener {
            //TODO
        }
        if (!subscribed) {
            textCollection.setText(R.string.add_to_collection)
        } else {
            textCollection.setText(R.string.collected)
        }
        textMusicCount.text = string(R.string.template_music_list_header_subtitle, count)
    }

    /**
     * 设置按钮监听事件
     *
     * @param subscribed 收藏按钮被点击的监听，返回Boolean表示修改的结果,true按钮点击后被收藏
     * @param playAll 播放全部歌曲按钮被点击的监听
     */
    fun setListener(subscribed: suspend () -> Boolean,
                    playAll: () -> Unit) = with(itemView) {
        textCollection.setOnClickListenerAsync {
            if (subscribed()) {
                textCollection.setText(R.string.collected)
            } else {
                textCollection.setText(R.string.add_to_collection)
            }
        }
        setOnClickListener {
            playAll()
        }
    }


}