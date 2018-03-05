package tech.summerly.quiet.netease.ui

import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.support.v7.widget.LinearSmoothScroller
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
import kotlinx.coroutines.experimental.launch
import me.drakeet.multitype.MultiTypeAdapter
import org.jetbrains.anko.coroutines.experimental.asReference
import org.jetbrains.anko.dip
import tech.summerly.quiet.commonlib.base.BaseActivity
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.bean.MusicType
import tech.summerly.quiet.commonlib.fragments.BottomControllerFragment
import tech.summerly.quiet.commonlib.player.MusicPlayerManager
import tech.summerly.quiet.commonlib.player.musicPlayer
import tech.summerly.quiet.commonlib.utils.*
import tech.summerly.quiet.netease.NeteaseModule
import tech.summerly.quiet.netease.R
import tech.summerly.quiet.netease.ui.items.NeteaseMusicHeader
import tech.summerly.quiet.netease.ui.items.NeteaseMusicHeaderViewBinder
import tech.summerly.quiet.netease.ui.items.NeteaseMusicItemViewBinder
import tech.summerly.quiet.netease.ui.items.NeteasePlaylistDetailHeaderViewBinder
import tech.summerly.quiet.service.netease.NeteaseCloudMusicApi
import tech.summerly.quiet.service.netease.result.PlaylistDetailResultBean


/**
 *
 * 网易云音乐歌单详情界面,显示歌单内的所有歌曲.
 * author: summerly
 * email: yangbinyhbn@gmail.com
 */
@Route(path = "/netease/playlist_detail")
internal class NeteasePlaylistDetailActivity : BaseActivity(), BottomControllerFragment.BottomControllerContainer {

    companion object {
        const val KEY_PLAYLIST_ID = "playlist_id"
    }

    private val items = ArrayList<Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.netease_activity_playlist_detail)
        initView()
        loadData()
        listenEvent()
    }

    //计算RecyclerView向上滑动的距离,以实现ToolBar的透明度改变.
    private var scrollY = 0f

    private var heightHeader = NeteaseModule.dip(220)

    private var isScrollByFindPosition = false

    private var isNeedShowIndicatorFindLocation = false

    private val mTitleHits = LongArray(2)

    private fun initView() {
        list.adapter = MultiTypeAdapter(items).also {
            it.register(PlaylistDetailResultBean.Playlist::class.java,
                    NeteasePlaylistDetailHeaderViewBinder(this::setBackgroundColor))
            it.register(NeteaseMusicHeader::class.java, NeteaseMusicHeaderViewBinder())
            it.register(Music::class.java,
                    NeteaseMusicItemViewBinder(this::onMusicClick))
        }

        val scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                scrollY += dy
                if (scrollY > heightHeader) {
                    toolbarPlaylist.background.alpha = 0xff
                } else {
                    val alpha = (scrollY / heightHeader) * 0xff
                    toolbarPlaylist.background.alpha = alpha.toInt()
                }

                if (!isNeedShowIndicatorFindLocation) {
                    return
                }
                val position = findCurrentPlayingMusic() ?: return
                val holder = recyclerView.findViewHolderForAdapterPosition(position)
                if (holder == null) {
                    indicatorMyLocation.show()
                } else {
                    indicatorMyLocation.hide()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (isNeedShowIndicatorFindLocation && newState == RecyclerView.SCROLL_STATE_IDLE && isScrollByFindPosition) {
                    //找到当前音乐的ItemView并播放一个渐变动画
                    val index = findCurrentPlayingMusic() ?: return
                    val view = recyclerView.findViewHolderForAdapterPosition(index)?.itemView?.asReference()
                            ?: return
                    launch(UI) {
                        view().isPressed = true
                        delay(1000)
                        view().isPressed = false
                    }
                    isScrollByFindPosition = false
                }
            }
        }
        list.addOnScrollListener(scrollListener)
        indicatorMyLocation.setOnClickListener {
            val index = findCurrentPlayingMusic() ?: return@setOnClickListener
            isScrollByFindPosition = true
            val scroller = CenterSmoothScroller(this)
            scroller.targetPosition = index
            list.layoutManager.startSmoothScroll(scroller)
        }
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
        toolbarPlaylist.setOnClickListener {
            //double hit scroll to first
            System.arraycopy(mTitleHits, 1, mTitleHits, 0, mTitleHits.size - 1)
            mTitleHits[mTitleHits.size - 1] = SystemClock.uptimeMillis()
            if (mTitleHits[0] > SystemClock.uptimeMillis() - 500) {
                list.smoothScrollToPosition(0)
            }
        }
    }

    private fun findCurrentPlayingMusic(): Int? {
        val current = musicPlayer.current ?: return null
        val index = items.indexOf(current)
        if (index == -1) {
            return null
        }
        return index
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
                if (!searchView.isVisible) {
                    return@asyncUI
                }
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
        val musicPlayer = MusicPlayerManager.musicPlayer(MusicType.NETEASE)
        musicPlayer.playlist.setMusicLists(items.filterIsInstance(Music::class.java))
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
            checkPlayingMusicIsInList()
            list.multiTypeAdapter.notifyDataSetChanged()
        }
    }

    private fun listenEvent() {
        MusicPlayerManager.musicChange.observeFilterNull(this) { (old, new) ->
            if (old != null) {
                val index = items.indexOf(old)
                if (index != -1) {
                    list.adapter.notifyItemChanged(index)
                }
            }
            if (new != null) {
                val index = items.indexOf(new)
                if (index != -1) {
                    list.adapter.notifyItemChanged(index)
                }
            }
            checkPlayingMusicIsInList()
        }
    }


    /**
     * 检查当前播放的音乐是否存在与此列表中
     */
    private fun checkPlayingMusicIsInList() {
        val current = musicPlayer.current
        if (current != null && items.contains(current)) {
            isNeedShowIndicatorFindLocation = true
        } else {
            isNeedShowIndicatorFindLocation = false
            indicatorMyLocation.hide()
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

    private class CenterSmoothScroller(context: Context) : LinearSmoothScroller(context) {
        override fun calculateDtToFit(viewStart: Int, viewEnd: Int, boxStart: Int, boxEnd: Int, snapPreference: Int): Int {
            return (boxStart + (boxEnd - boxStart) / 2) - (viewStart + (viewEnd - viewStart) / 2)
        }
    }
}