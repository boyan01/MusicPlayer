package tech.soit.quiet.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.core.view.children
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import tech.soit.quiet.AppContext
import tech.soit.quiet.R
import tech.soit.quiet.model.vo.Music
import tech.soit.quiet.model.vo.PlayListDetail
import tech.soit.quiet.model.vo.User
import tech.soit.quiet.player.MusicPlayerManager
import tech.soit.quiet.player.playlist.Playlist
import tech.soit.quiet.ui.activity.MusicPlayerActivity
import tech.soit.quiet.ui.adapter.viewholder.*
import tech.soit.quiet.utils.component.log
import tech.soit.quiet.utils.component.support.attrValue
import tech.soit.quiet.utils.component.support.px
import tech.soit.quiet.utils.component.support.string
import tech.soit.quiet.utils.event.WindowInsetsEvent

class MusicListAdapter2 : RecyclerView.Adapter<BaseViewHolder>() {

    private val musics = ArrayList<Music>()

    companion object {

        private const val TYPE_MUSIC = 0
        private const val TYPE_HEADER = 2
        private const val TYPE_LIST_DETAIL = 1
        private const val TYPE_PLACEHOLDER = 3


        /**
         * HEADER数目
         */
        private const val HEADER_COUNT = 2

    }

    private var recyclerView: RecyclerView? = null

    @ColorInt
    private var colorPrimary: Int = AppContext.attrValue(R.attr.colorPrimary)

    private var insetsTop: Int = -1

    /**
     * 是否在列表头展示收藏按钮
     */
    private var isShowSubscribeButton: Boolean = false

    /**
     * 此音乐列表是否已经被收藏了
     */
    private var isSubscribed: Boolean = false

    private var playlist: PlayListDetail? = null

    private var isPreviewMode = false

    /**
     * 当前正在播放的音乐
     */
    private var playingMusic: Music? = null

    /**
     * 占位view的高度
     */
    var placeholderHeight: Int = 300.px

    val token get() = playlist?.getToken() ?: Playlist.TOKEN_EMPTY

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
        EventBus.getDefault().register(this)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null
        EventBus.getDefault().unregister(this)
    }

    /**
     * 展示音乐列表
     */
    private fun showList(musics: List<Music>) {
        isPreviewMode = false

        this.musics.clear()
        this.musics.addAll(musics)

        //check current playing music
        if (token == MusicPlayerManager.musicPlayer.playlist.token) {
            playingMusic = MusicPlayerManager.musicPlayer.playlist.current
        }

        notifyDataSetChanged()
    }


    private fun preview() {
        if (isPreviewMode) {
            return
        }
        isPreviewMode = true
        notifyDataSetChanged()
    }


    fun showList(user: User?, playlist: PlayListDetail) {
        this.playlist = playlist

        isShowSubscribeButton = user != null && user.getId() != playlist.getCreator().getId()
        isSubscribed = user != null && user.getId() != playlist.getCreator().getId()
                && playlist.isSubscribed()


        if (playlist.getTracks() == PlayListDetail.NONE_TRACKS) {
            preview()
        } else {
            showList(playlist.getTracks())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            TYPE_MUSIC -> {
                MusicViewHolder(LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_music_1, parent, false))

            }
            TYPE_HEADER -> {
                val holder = MusicListHeaderViewHolder(LayoutInflater.from(parent.context)
                        .inflate(R.layout.header_music_list, parent, false))
                holder.setListener(
                        subscribed = {
                            //TODO
                            false
                        },
                        playAll = {
                            if (musics.isNotEmpty()) {
                                MusicPlayerManager.play(token, musics[0], musics)
                            } else {
                                Toast.makeText(parent.context, string(R.string.toast_no_music_to_play), Toast.LENGTH_SHORT).show()
                            }
                        })
                holder
            }
            TYPE_LIST_DETAIL -> {
                val holder = PlayListDetailViewHolder(LayoutInflater.from(parent.context)
                        .inflate(R.layout.header_play_list_detail, parent, false))
                holder.addPaddingTop(insetsTop)
                holder
            }
            TYPE_PLACEHOLDER -> {
                val holder = PlaceholderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_placeholder, parent, false))
                holder.itemView.updateLayoutParams { height = placeholderHeight }
                holder
            }
            else -> error("can not create view holder for type $viewType")
        }
    }

    override fun getItemCount(): Int {
        return HEADER_COUNT + if (isPreviewMode) 1 else musics.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.applyPrimaryColor(colorPrimary)
        when (holder.itemViewType) {
            TYPE_MUSIC -> {
                holder as MusicViewHolder
                val index = position - HEADER_COUNT
                val data = musics[index]

                holder.bind(data, index + 1)
                holder.setPlaying(playingMusic == data)
                holder.setListener(
                        play = {
                            val pl = MusicPlayerManager.musicPlayer.playlist
                            if (pl.token == token && pl.current == data) {
                                val context = holder.itemView.context
                                val intent = Intent(context, MusicPlayerActivity::class.java)
                                context.startActivity(intent)
                                if (!MusicPlayerManager.musicPlayer.mediaPlayer.isPlayWhenReady) {
                                    MusicPlayerManager.musicPlayer.playPause()
                                }
                            } else {
                                MusicPlayerManager.play(token, data, musics)
                            }
                        },
                        showOptions = {
                            log { "show options for $data" }
                        }
                )
            }
            TYPE_HEADER -> {
                holder as MusicListHeaderViewHolder
                holder.setHeader(playlist?.getTrackCount()
                        ?: 0, isShowSubscribeButton, isSubscribed)
            }
            TYPE_LIST_DETAIL -> {
                holder as PlayListDetailViewHolder
                playlist?.let {
                    holder.bind(it)
                }
            }
            TYPE_PLACEHOLDER -> {
                holder as PlaceholderViewHolder
                //DO nothing...
            }
        }
    }

    /**
     * 设置列表主题色
     */
    fun applyPrimaryColor(@ColorInt colorPrimary: Int) {
        if (this.colorPrimary == colorPrimary) {
            return
        }
        this.colorPrimary = colorPrimary
        recyclerView?.apply {
            children
                    .mapNotNull {
                        getChildViewHolder(it)
                    }
                    .filterIsInstance(BaseViewHolder::class.java)
                    .forEach {
                        it.applyPrimaryColor(colorPrimary)
                    }
        }
    }

    fun changePlayingMusic(music: Music?) {
        playingMusic = music
        val index = musics.indexOf(music)
        val recyclerView = recyclerView ?: return
        recyclerView.children
                .mapNotNull { recyclerView.getChildViewHolder(it) }
                .filterIsInstance(MusicViewHolder::class.java)
                .forEach {
                    it.setPlaying((it.adapterPosition - HEADER_COUNT) == index)
                }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWindowInsetsEvent(e: WindowInsetsEvent) {
        if (insetsTop != -1) {
            return
        }
        insetsTop = e.insets.systemWindowInsetTop

        recyclerView?.apply {
            children.mapNotNull { getChildViewHolder(it) }
                    .filterIsInstance(PlayListDetailViewHolder::class.java)
                    .forEach {
                        it.addPaddingTop(insetsTop)
                    }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> TYPE_LIST_DETAIL
            1 -> TYPE_HEADER
            else -> if (!isPreviewMode) TYPE_MUSIC else TYPE_PLACEHOLDER
        }
    }

}