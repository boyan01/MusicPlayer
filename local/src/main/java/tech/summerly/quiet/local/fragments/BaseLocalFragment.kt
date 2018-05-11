package tech.summerly.quiet.local.fragments

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.launcher.ARouter
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import me.drakeet.multitype.MultiTypeAdapter
import tech.summerly.quiet.commonlib.base.BaseFragment
import tech.summerly.quiet.commonlib.bean.*
import tech.summerly.quiet.commonlib.items.CommonItemA
import tech.summerly.quiet.commonlib.items.CommonItemAViewBinder
import tech.summerly.quiet.commonlib.player.MusicPlayerManager
import tech.summerly.quiet.commonlib.utils.log
import tech.summerly.quiet.commonlib.utils.multiTypeAdapter
import tech.summerly.quiet.commonlib.utils.setItemsByDiff
import tech.summerly.quiet.constraints.PlaylistDetail
import tech.summerly.quiet.local.LocalModule
import tech.summerly.quiet.local.LocalMusicFragment
import tech.summerly.quiet.local.R
import tech.summerly.quiet.local.fragments.items.*
import tech.summerly.quiet.local.utils.AlbumDetailProvider
import tech.summerly.quiet.local.utils.ArtistDetailProvider
import tech.summerly.quiet.service.local.LocalMusicApi
import tech.summerly.quiet.service.local.database.database.Table
import kotlin.coroutines.experimental.CoroutineContext

/**
 * Created by summer on 17-12-23
 * template fragment for [tech.summerly.quiet.local.LocalMusicActivity],
 * only contains a recycle view
 */
abstract class BaseLocalFragment : BaseFragment() {

    private lateinit var localMusicApi: LocalMusicApi

    private var version = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        localMusicApi = LocalMusicApi.getLocalMusicApi(LocalModule)

        //to perceive database's changes
        Table.values().filter {
            isInterestedChange(it)
        }.forEach {
            it.listenChange(this) { newVersion ->
                log { "is database changed : ${newVersion > this.version}" }
                if (newVersion > this.version) {
                    fetchDataAndDisplay()
                    this.version = newVersion
                }
            }
        }
    }

    /**
     * be interested in this table's change
     * use to refresh the data view when database is changed
     */
    protected abstract fun isInterestedChange(table: Table): Boolean

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val recyclerView = RecyclerView(requireContext())
        val layoutManager = GridLayoutManager(requireContext(), getSpanCount())
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return 1
            }
        }
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = MultiTypeAdapter(mutableListOf<Any>()).also {
            it.register(Music::class.java, LocalMusicItemViewBinder(this::onMusicClicked))
            it.register(LocalBigImageItem::class.java, LocalBigImageItemViewBinder(this::onBigImageItemClick))
            it.register(CommonItemA::class.java,
                    CommonItemAViewBinder(this::onOverviewNavClick))
            it.register(LocalPlaylistHeaderViewBinder.PlaylistHeader::class.java,
                    LocalPlaylistHeaderViewBinder())
            it.register(Playlist::class.java, LocalPlaylistItemViewBinder(this::onPlaylistClicked))
        }
        return recyclerView
    }

    private var jobLoadData: Job? = null

    /**
     * display the
     */
    private fun fetchDataAndDisplay() {
        jobLoadData?.cancel()
        jobLoadData = launch {
            val data = loadData(localMusicApi)
            coroutineContext.checkCompletion()
            showItems(data)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchDataAndDisplay()
    }

    protected abstract suspend fun loadData(localMusicApi: LocalMusicApi): List<Any>

    /**
     * set recycler view layout manager's span count
     */
    protected open fun getSpanCount() = 1

    /**
     * show data with animation which detected by diff utils
     * need to use this method after root view has been created
     */
    private fun showItems(items: List<Any>) = runWithRoot {
        this as? RecyclerView ?: return@runWithRoot
        multiTypeAdapter.setItemsByDiff(items, true)
    }

    private fun CoroutineContext.checkCompletion() {
        val job = get(Job)
        if (job != null && !job.isActive) throw job.getCancellationException()
    }

    private fun onPlaylistClicked(playlist: Playlist) {
        log { "playlist : $playlist" }
    }

    private val musicPlayer get() = MusicPlayerManager.musicPlayer(MusicType.LOCAL)

    protected open fun onMusicClicked(music: Music) = runWithRoot {
        this as? RecyclerView ?: return@runWithRoot
        val items = multiTypeAdapter.items.filterIsInstance(Music::class.java)
        musicPlayer.playlist.setMusicLists(items)
        musicPlayer.play(music)
    }

    private fun onOverviewNavClick(nav: CommonItemA) {
        val fragment = this.parentFragment as? LocalMusicFragment ?: return
        when (nav.title) {
            getString(R.string.local_overview_nav_total) -> fragment.setCurrentPage(1)
            getString(R.string.local_overview_nav_artist) -> fragment.setCurrentPage(2)
            getString(R.string.local_overview_nav_album) -> fragment.setCurrentPage(3)
            getString(R.string.local_overview_nav_trend) -> {

            }
            getString(R.string.local_overview_nav_latest) -> {

            }
        }
    }

    private fun onBigImageItemClick(item: LocalBigImageItem) {
        when (item.data) {
            is Artist -> {
                ARouter.getInstance()
                        .build(PlaylistDetail.ACTIVITY_PLAYLIST_DETAIL)
                        .withParcelable(PlaylistDetail.PARAM_PLAYLIST_PROVIDER, ArtistDetailProvider(item.data))
                        .navigation()
            }
            is Album -> {
                ARouter.getInstance()
                        .build(PlaylistDetail.ACTIVITY_PLAYLIST_DETAIL)
                        .withParcelable(PlaylistDetail.PARAM_PLAYLIST_PROVIDER, AlbumDetailProvider(item.data))
                        .navigation()
            }
        }
    }
}