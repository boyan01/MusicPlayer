package tech.summerly.quiet.netease.ui

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.transition.Fade
import android.transition.TransitionManager
import android.view.inputmethod.InputMethodManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.gson.Gson
import kotlinx.android.synthetic.main.netease_activity_playlist_detail.*
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.delay
import me.drakeet.multitype.MultiTypeAdapter
import org.jetbrains.anko.dip
import tech.summerly.quiet.commonlib.base.BaseActivity
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.bean.MusicType
import tech.summerly.quiet.commonlib.fragments.BottomControllerFragment
import tech.summerly.quiet.commonlib.player.musicPlayer
import tech.summerly.quiet.commonlib.utils.*
import tech.summerly.quiet.netease.NeteaseModule
import tech.summerly.quiet.netease.R
import tech.summerly.quiet.service.netease.NeteaseCloudMusicApi
import tech.summerly.quiet.service.netease.result.PlaylistDetailResultBean
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

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                log { "state : $newState" }
            }
        })
        imageBack.setOnClickListener {
            onBackPressed()
        }
        imageSearch.setOnClickListener {
            switchToSearchView(true)
        }
        imageClear.setOnClickListener {
            if (searchView.text.isEmpty()) {
                switchToSearchView(false)
            } else {
                searchView.setText("")
            }
        }
        searchView.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
                val text = s?.trim()
                filterText(text)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }

    /**
     * show filtered data
     */
    private fun filterText(text: CharSequence?) {
        if (text == null || text.isEmpty()) {
            if (list.multiTypeAdapter.items != items) {
                //when filter text is empty , we need to reset the RecyclerView
                list.multiTypeAdapter.items = items
                list.adapter.notifyDataSetChanged()
                list.layoutManager.scrollToPosition(0)
                scrollY = 0f
                list.setPadding(list.paddingLeft, 0, list.paddingRight, list.paddingBottom)
            }
            return
        }
        val data = items
                .filterIsInstance(Music::class.java)
                .filter {
                    it.title.contains(text, true)
                            || it.artistAlbumString().contains(text, true)
                }

        // add a top padding for RecyclerView
        list.setPadding(list.paddingLeft, dip(80), list.paddingRight, list.paddingBottom)
        //reset toolbar to disTransparent
        toolbarPlaylist.background.alpha = 0xff
        //when scrollY is less than heightHeader, toolbar might be transparent when RecyclerView scrolling
        list.layoutManager.scrollToPosition(0)
        scrollY = heightHeader.toFloat()
        //display filtered data
        list.multiTypeAdapter.items = data
        list.adapter.notifyDataSetChanged()
    }

    /**
     * to show or hide search view in toolbar
     */
    private fun switchToSearchView(show: Boolean) {
        TransitionManager.beginDelayedTransition(toolbarPlaylist, Fade())
        if (show) {
            groupNormal.gone()
            groupSearch.visible()
            asyncUI {
                //delay 0.5 seconds to waiting for transition animation complete
                delay(500)
                searchView.requestFocus()
                val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.showSoftInput(searchView, InputMethodManager.SHOW_IMPLICIT)
            }
        } else {
            groupSearch.gone()
            searchView.setText("")
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(searchView.windowToken, 0)
            groupNormal.visible()
        }
    }

    private fun onMusicClick(music: Music) {
        musicPlayer.setType(MusicType.NETEASE)
        musicPlayer.playlistProvider.setPlaylist(items.filterIsInstance(Music::class.java))
        musicPlayer.play(music)
    }

    private fun setBackgroundColor(color: Int) {
        val alpha = toolbarPlaylist.background?.alpha
        toolbarPlaylist.setBackgroundColor(color)
        toolbarPlaylist.background.alpha = alpha ?: ((scrollY / heightHeader) * 0xff).toInt()
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
            showDefaultHeader(playlistString)
            val (playlist, musics) = neteaseCloudMusicApi.getPlaylistDetail(id)
            items.clear()
            items += playlist
            items += NeteaseMusicHeader(musics.size)
            items.addAll(musics)
            list.multiTypeAdapter.notifyDataSetChanged()
        }
    }

    /**
     * show a preview header before loading data from net
     */
    private fun showDefaultHeader(detail: String?) {
        detail ?: return
        val resultBean = Gson().fromJson<PlaylistDetailResultBean.Playlist>(detail) ?: return
        items += resultBean
        list.multiTypeAdapter.notifyDataSetChanged()
    }

    override fun onBackPressed() {
        if (groupSearch.isVisible) {
            switchToSearchView(false)
            return
        }
        super.onBackPressed()
    }
}