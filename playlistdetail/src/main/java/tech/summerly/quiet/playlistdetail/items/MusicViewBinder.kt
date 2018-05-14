package tech.summerly.quiet.playlistdetail.items

import android.content.Context
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.template.IProvider
import kotlinx.android.synthetic.main.pd_item_music.view.*
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.player.MusicPlayerManager
import tech.summerly.quiet.commonlib.utils.ItemViewBinder2
import tech.summerly.quiet.commonlib.utils.image.setImageUrl
import tech.summerly.quiet.commonlib.utils.log
import tech.summerly.quiet.commonlib.utils.popupMenu
import tech.summerly.quiet.constraints.PlaylistDetail
import tech.summerly.quiet.playlistdetail.R


@Route(path = PlaylistDetail.ITEM_BINDER_MUSIC)
class MusicViewBinder : ItemViewBinder2<Music>(), IProvider {


    private val defaultMusicClickListener = fun(music: Music) {
        val musicList = adapter.items.filterIsInstance(Music::class.java)
        MusicPlayerManager.play("playlist detail", musicList, music)
    }

    private var onMusicClickListener = defaultMusicClickListener

    override val layoutId: Int = R.layout.pd_item_music

    override fun init(context: Context) {

    }

    override fun onBindViewHolder(holder: ViewHolder, item: Music) = with(holder.itemView) {
        val picUri = item.picUri
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

    fun withOnItemClickListener(listener: ((Music) -> Unit)): MusicViewBinder {
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