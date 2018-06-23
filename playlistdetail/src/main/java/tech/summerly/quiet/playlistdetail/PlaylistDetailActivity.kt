package tech.summerly.quiet.playlistdetail

import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator
import androidx.core.animation.doOnEnd
import androidx.core.view.updateLayoutParams
import com.alibaba.android.arouter.facade.annotation.Route
import kotlinx.android.synthetic.main.pd_activity_playlist_deatil.*
import me.drakeet.multitype.Items
import org.jetbrains.anko.backgroundDrawable
import org.jetbrains.anko.dimen
import tech.summerly.quiet.commonlib.component.activities.NoIsolatedActivity
import tech.summerly.quiet.commonlib.component.callback.BottomControllerHost
import tech.summerly.quiet.commonlib.model.IMusic
import tech.summerly.quiet.commonlib.model.PlaylistProvider
import tech.summerly.quiet.commonlib.player.MusicPlayerManager
import tech.summerly.quiet.commonlib.utils.*
import tech.summerly.quiet.commonlib.utils.support.TypedAdapter
import tech.summerly.quiet.constraints.Main
import tech.summerly.quiet.constraints.PlaylistDetail
import tech.summerly.quiet.playlistdetail.items.MusicHeader
import tech.summerly.quiet.playlistdetail.items.MusicHeaderViewBinder
import tech.summerly.quiet.playlistdetail.items.MusicViewBinder
import tech.summerly.quiet.playlistdetail.items.PlaylistHeaderViewBinder

/**
 * author: summerly
 * email: yangbinyhbn@gmail.com
 */
@Route(path = PlaylistDetail.ACTIVITY_PLAYLIST_DETAIL)
class PlaylistDetailActivity : NoIsolatedActivity(), BottomControllerHost {

    companion object {

        private const val PARAM_PLAYLIST_PROVIDER = PlaylistDetail.PARAM_PLAYLIST_PROVIDER

        internal const val TOKEN_PLAY = "playlist_detail_"

    }

    override val parentPath: String = Main.ACTIVITY_MAIN

    private var description: PlaylistProvider.Description? = null

    private val mScrollListener = object : RecyclerView.OnScrollListener() {


        private var scrollY = 0f

        private var heightHeader = 500

        private var animatorTargetFind: ValueAnimator? = null

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

            if (isNeedShowIndicatorFindLocation) {
                val position = findCurrentPlayingMusic() ?: return
                val holder = recyclerView.findViewHolderForAdapterPosition(position)
                if (holder == null) {
                    indicatorMyLocation.show()
                } else {
                    indicatorMyLocation.hide()
                }
            }
            scrollY += dy
            if (description != null) {
                //只有 description 不为空时,才显示一个详情信息的Header在最上面
                //所有当 description 为空时,就不再对Toolbar进行透明化处理了.
                if (scrollY > heightHeader) {
                    toolbarPlaylist.background.alpha = 0xff
                } else {
                    val alpha = (scrollY / heightHeader) * 0xff
                    toolbarPlaylist.background.alpha = alpha.toInt()
                }
            }
        }


        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            val needAnimation = isNeedShowIndicatorFindLocation
                    && newState == RecyclerView.SCROLL_STATE_IDLE
                    && isScrollByFindPosition
            if (needAnimation) {
                //找到当前音乐的ItemView并播放一个渐变动画
                val index = findCurrentPlayingMusic() ?: return
                val view = recyclerView.findViewHolderForAdapterPosition(index)?.itemView
                        ?: return
                //end previous animation first
                animatorTargetFind?.end()

                val old = view.background
                view.setBackgroundColor(getAttrColor(R.attr.colorPrimaryLight))

                val animator = ValueAnimator.ofInt(0, 255, 0)
                animator.duration = 1000
                animatorTargetFind = animator
                animator.addUpdateListener {
                    view.background.alpha = it.animatedValue as Int
                }
                animator.doOnEnd {
                    view.backgroundDrawable = old
                }
                animator.start()
                isScrollByFindPosition = false
            }
        }

        //根据滚动的偏移和标题栏的高度来计算 toolbar 背景色的透明度。
        val alpha get() = ((scrollY / heightHeader) * 0xff).toInt()

    }

    internal val adapter = TypedAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pd_activity_playlist_deatil)
        setEvent()
        loadData()
    }

    //记录toolbar的点击次数以实现双击回到顶部功能
    private val mTitleHits = LongArray(2)

    private var isScrollByFindPosition = false

    private var isNeedShowIndicatorFindLocation = false

    private fun setEvent() {
        list.adapter = adapter
                .withBinder(PlaylistProvider.Description::class, PlaylistHeaderViewBinder(setBackgroundColor))
                .withBinder(MusicHeader::class, MusicHeaderViewBinder())
                .withBinder(IMusic::class,
                        MusicViewBinder())

        list.addOnScrollListener(mScrollListener)
        (list.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        imageBack.setOnClickListener {
            onBackPressed()
        }
        imageSearch.setOnClickListener {
            val searchFragment = PlaylistInternalSearchFragment
                    .getInstance(ArrayList<IMusic>().also { adapter.list.filterIsInstanceTo(it) })
            supportFragmentManager.intransaction {
                replace(android.R.id.content, searchFragment)
                addToBackStack("search")
            }
        }
        toolbarPlaylist.setOnClickListener {
            //double hit scroll to first
            System.arraycopy(mTitleHits, 1, mTitleHits, 0, mTitleHits.size - 1)
            mTitleHits[mTitleHits.size - 1] = SystemClock.uptimeMillis()
            if (mTitleHits[0] > SystemClock.uptimeMillis() - 500) {
                list.smoothScrollToPosition(0)
            }
        }
        indicatorMyLocation.setOnClickListener {
            val index = findCurrentPlayingMusic() ?: return@setOnClickListener
            isScrollByFindPosition = true
            val scroller = CenterSmoothScroller(this)
            scroller.targetPosition = index
            list.layoutManager?.startSmoothScroll(scroller)
        }
        MusicPlayerManager.musicChange.observeFilterNull(this) { (old, new) ->
            val list = adapter.list
            if (old != null) {
                val index = list.indexOf(old)
                if (index != -1) {
                    adapter.notifyItemChanged(index)
                }
            }
            if (new != null) {
                val index = list.indexOf(new)
                if (index != -1) {
                    adapter.notifyItemChanged(index)
                }
            }
            checkPlayingMusicIsInList()
        }
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                log { "onChanged" }
                checkPlayingMusicIsInList()
            }
        })
    }

    /**
     * 检查当前播放的音乐是否存在与此列表中
     */
    private fun checkPlayingMusicIsInList() {
        val current = MusicPlayerManager.player.playlist.current
        //todo token check
        if (current != null && MusicPlayerManager.player.playlist.token == TOKEN_PLAY && adapter.list.contains(current)) {
            isNeedShowIndicatorFindLocation = true
        } else {
            isNeedShowIndicatorFindLocation = false
            indicatorMyLocation.hide()
        }
    }

    private fun loadData() = asyncUI {
        val playlistProvider = intent.getParcelableExtra<PlaylistProvider>(PARAM_PLAYLIST_PROVIDER)
                ?: error("缺少参数")
        val result = Items()

        //加载标题
        textTitle.text = playlistProvider.title

        //加载头部
        description = playlistProvider.getDescription()
        if (description == null) {
            toolbarPlaylist.setBackgroundColor(getAttrColor(R.attr.colorPrimary))
            list.updateLayoutParams<CoordinatorLayout.LayoutParams> {
                topMargin = dimen(R.dimen.common_toolbar_height_with_status_bar)
            }
        } else {
            result += description
            showPlaylists(ArrayList(result))
        }

        //加载歌曲列表
        val list = playlistProvider.getMusicList()
        result += MusicHeader(list.size)
        result.addAll(list)
        showPlaylists(ArrayList(result))
    }

    private fun showPlaylists(items: List<*>) {
        adapter.submit(items)
    }

    private val setBackgroundColor = fun(color: Int) {
        toolbarPlaylist.setBackgroundColor(color)
        toolbarPlaylist.background.alpha = mScrollListener.alpha
    }

    private fun findCurrentPlayingMusic(): Int? {
        val current = MusicPlayerManager.player.playlist.current ?: return null
        val index = adapter.list.indexOf(current)
        if (index == -1) {
            return null
        }
        return index
    }

    private class CenterSmoothScroller(context: Context) : LinearSmoothScroller(context) {
        override fun calculateDtToFit(viewStart: Int, viewEnd: Int, boxStart: Int, boxEnd: Int, snapPreference: Int): Int {
            return (boxStart + (boxEnd - boxStart) / 2) - (viewStart + (viewEnd - viewStart) / 2)
        }
    }

}