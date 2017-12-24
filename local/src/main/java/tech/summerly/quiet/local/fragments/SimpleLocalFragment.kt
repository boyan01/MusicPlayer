package tech.summerly.quiet.local.fragments

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.drakeet.multitype.MultiTypeAdapter
import tech.summerly.quiet.commonlib.base.BaseFragment
import tech.summerly.quiet.commonlib.bean.Artist
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.player.BaseMusicPlayer
import tech.summerly.quiet.commonlib.player.MusicPlayerManager
import tech.summerly.quiet.commonlib.utils.multiTypeAdapter
import tech.summerly.quiet.commonlib.utils.setItemsByDiff
import tech.summerly.quiet.local.LocalMusicApi
import tech.summerly.quiet.local.fragments.items.LocalArtistItemViewBinder
import tech.summerly.quiet.local.fragments.items.LocalMusicItemViewBinder

/**
 * Created by summer on 17-12-23
 * template fragment for [tech.summerly.quiet.local.LocalMusicActivity],
 * only contains a recycle view
 * need to set data by [setItems]
 */
@Suppress("MemberVisibilityCanPrivate")
open class SimpleLocalFragment : BaseFragment() {

    protected lateinit var localMusicApi: LocalMusicApi

    override fun onAttach(context: Context) {
        super.onAttach(context)
        localMusicApi = LocalMusicApi.getLocalMusicApi(context)
    }

    protected val musicPlayer: BaseMusicPlayer
        get() = MusicPlayerManager.INSTANCE.getMusicPlayer()

    private val data = ArrayList<Any>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val recyclerView = RecyclerView(context)
        val layoutManager = GridLayoutManager(context, getSpanCount())
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = MultiTypeAdapter(data).also {
            it.register(Music::class.java, LocalMusicItemViewBinder(this::checkMusicIsPlaying))
            it.register(Artist::class.java, LocalArtistItemViewBinder())
        }
        return recyclerView
    }

    /**
     * set recycler view layout manager's span count
     */
    protected open fun getSpanCount() = 1

    /**
     * show data with animation which detected by diff utils
     * need to use this method after root view has been created
     */
    fun showItems(items: List<Any>) = runWithRoot {
        this as? RecyclerView ?: return@runWithRoot
        multiTypeAdapter.setItemsByDiff(items)
    }

    /**
     * only set data , and do not assure to refresh the layout
     */
    fun setItems(items: List<Any>) {
        this.data.clear()
        this.data.addAll(items)
        runWithRoot {
            (this as? RecyclerView)?.multiTypeAdapter?.notifyDataSetChanged()
        }
    }

    protected fun checkMusicIsPlaying(music: Music): Boolean {
        return musicPlayer.getPlayingMusic().value == music
    }
}