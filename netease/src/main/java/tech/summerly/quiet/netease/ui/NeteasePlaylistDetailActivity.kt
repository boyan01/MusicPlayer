package tech.summerly.quiet.netease.ui

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import kotlinx.android.synthetic.main.netease_activity_playlist_detail.*
import kotlinx.coroutines.experimental.Job
import me.drakeet.multitype.MultiTypeAdapter
import org.jetbrains.anko.dip
import tech.summerly.quiet.commonlib.base.BaseActivity
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.bean.MusicType
import tech.summerly.quiet.commonlib.fragments.BottomControllerFragment
import tech.summerly.quiet.commonlib.player.musicPlayer
import tech.summerly.quiet.commonlib.utils.LoggerLevel
import tech.summerly.quiet.commonlib.utils.asyncUI
import tech.summerly.quiet.commonlib.utils.log
import tech.summerly.quiet.commonlib.utils.multiTypeAdapter
import tech.summerly.quiet.netease.NeteaseModule
import tech.summerly.quiet.netease.R
import tech.summerly.quiet.netease.api.NeteaseCloudMusicApi
import tech.summerly.quiet.netease.api.result.PlaylistDetailResultBean
import tech.summerly.quiet.netease.ui.items.NeteaseMusicHeader
import tech.summerly.quiet.netease.ui.items.NeteaseMusicHeaderViewBinder
import tech.summerly.quiet.netease.ui.items.NeteaseMusicItemViewBinder
import tech.summerly.quiet.netease.ui.items.NeteasePlaylistDetailHeaderViewBinder

/**
 * author: summerly
 * email: yangbinyhbn@gmail.com
 */
@Route(path = "/netease/playlist_detail")
class NeteasePlaylistDetailActivity : BaseActivity(), BottomControllerFragment.BottomControllerContainer {

    companion object {
        const val KEY_PLAYLIST_ID = "playlist_id"
    }

    private val items = ArrayList<Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.netease_activity_playlist_detail)
        initView()
        loadData()
    }

    private var scrollY = 0f

    private var heightHeader = NeteaseModule.dip(220)

    private fun initView() {
        list.adapter = MultiTypeAdapter(items).also {
            it.register(PlaylistDetailResultBean.Playlist::class.java,
                    NeteasePlaylistDetailHeaderViewBinder(this::setBackgroundColor))
            it.register(NeteaseMusicHeader::class.java, NeteaseMusicHeaderViewBinder())
            it.register(Music::class.java,
                    NeteaseMusicItemViewBinder(this::onMusicClick))
        }
        list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                scrollY += dy
                if (scrollY > heightHeader) {
                    toolbarPlaylist.background.alpha = 0xff
                } else {
                    val alpha = (scrollY / heightHeader) * 0xff
                    toolbarPlaylist.background.alpha = alpha.toInt()
                }
            }
        })
        toolbarPlaylist.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun onMusicClick(music: Music) {
        musicPlayer.setType(MusicType.NETEASE)
        musicPlayer.playlistProvider.setPlaylist(items.filterIsInstance(Music::class.java))
        musicPlayer.play(music)
    }

    private fun setBackgroundColor(color: Int) {
        toolbarPlaylist.setBackgroundColor(color)
        toolbarPlaylist.background.alpha = ((scrollY / heightHeader) * 0xff).toInt()
    }

    private val neteaseCloudMusicApi by lazy {
        NeteaseCloudMusicApi()
    }

    private fun loadData(): Job {
        return asyncUI {
            val id = intent.getLongExtra(KEY_PLAYLIST_ID, 0)
            if (id == 0L) {
                //todo set error
                log(LoggerLevel.ERROR) { "id :$id" }
                return@asyncUI
            }
            val playlistString = intent.getStringExtra("playlist_detail")
            log { playlistString }
            val (playlist, musics) = neteaseCloudMusicApi.getPlaylistDetail(id)
            items += playlist
            items += NeteaseMusicHeader(musics.size)
            items.addAll(musics)
            list.multiTypeAdapter.notifyDataSetChanged()
        }
    }
}