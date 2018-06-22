package tech.summerly.quiet.playlistdetail.items

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import kotlinx.android.synthetic.main.pd_item_music.view.*
import tech.summerly.quiet.commonlib.model.IMusic
import tech.summerly.quiet.commonlib.player.MusicPlayerManager
import tech.summerly.quiet.commonlib.utils.image.setImageUrl
import tech.summerly.quiet.commonlib.utils.log
import tech.summerly.quiet.commonlib.utils.popupMenu
import tech.summerly.quiet.commonlib.utils.support.TypedBinder
import tech.summerly.quiet.commonlib.utils.support.ViewHolder
import tech.summerly.quiet.constraints.PlaylistDetail
import tech.summerly.quiet.playlistdetail.PlaylistDetailActivity
import tech.summerly.quiet.playlistdetail.R


@Route(path = PlaylistDetail.ITEM_BINDER_MUSIC)
class MusicViewBinder : TypedBinder<IMusic>() {


    private val defaultMusicClickListener = fun(music: IMusic) {
        val musicList = adapter.list.filterIsInstance(IMusic::class.java)
        MusicPlayerManager.play(PlaylistDetailActivity.TOKEN_PLAY, musicList, music)
    }

    private var onMusicClickListener = defaultMusicClickListener


    override fun init(context: Context) {

    }

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder.from(R.layout.pd_item_music, parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, item: IMusic) = with(holder.itemView) {
        val picUri = item.artwork
        if (picUri == null) {
            image.setImageResource(R.drawable.common_image_music_disk)
        } else {
            image.setImageUrl(picUri)
        }
        music.setMusic(item)
        imageMore.setOnClickListener {
            showPopupMenu(it)
        }
        setOnClickListener {
            onMusicClickListener(item)
        }
        Unit
    }

    fun withOnItemClickListener(listener: ((IMusic) -> Unit)): MusicViewBinder {
        onMusicClickListener = listener
        return this
    }

    private fun showPopupMenu(anchor: View) {
        popupMenu(anchor, tech.summerly.quiet.commonlib.R.menu.popup_music_item) {
            log { it.itemId }
            true
        }
    }

}