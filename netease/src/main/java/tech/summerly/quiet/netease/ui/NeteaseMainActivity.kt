package tech.summerly.quiet.netease.ui

import android.animation.Animator
import android.animation.ObjectAnimator
import android.os.Bundle
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import kotlinx.android.synthetic.main.netease_activity_main.*
import kotlinx.android.synthetic.main.netease_header_playlist.view.*
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import me.drakeet.multitype.MultiTypeAdapter
import org.jetbrains.anko.startActivity
import tech.summerly.quiet.commonlib.base.BaseActivity
import tech.summerly.quiet.commonlib.fragments.BottomControllerFragment
import tech.summerly.quiet.commonlib.items.CommonItemA
import tech.summerly.quiet.commonlib.items.CommonItemAViewBinder
import tech.summerly.quiet.commonlib.utils.multiTypeAdapter
import tech.summerly.quiet.netease.R
import tech.summerly.quiet.netease.api.NeteaseCloudMusicApi
import tech.summerly.quiet.netease.ui.items.NeteasePlaylistHeader
import tech.summerly.quiet.netease.ui.items.NeteasePlaylistHeaderViewBinder

/**
 * Created by summer on 17-12-30
 */
class NeteaseMainActivity : BaseActivity(), BottomControllerFragment.BottomControllerContainer {

    private val navItems by lazy {
        listOf(
                CommonItemA(getString(R.string.netease_nav_title_local), R.drawable.netease_ic_library_music_black_24dp),
                CommonItemA(getString(R.string.netease_nav_title_latest), R.drawable.netease_ic_access_time_black_24dp),
                CommonItemA(getString(R.string.netease_nav_title_download), R.drawable.netease_ic_file_download_black_24dp),
                CommonItemA(getString(R.string.netease_nav_title_daily), R.drawable.netease_ic_today_black_24dp),
                CommonItemA(getString(R.string.netease_nav_title_fm), R.drawable.netease_ic_radio_black_24dp)
        )
    }

    private val items = ArrayList<Any>()

    private val playlistCreate = ArrayList<Any>()

    private val playlistCollect = ArrayList<Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.netease_activity_main)
        recycler.adapter = MultiTypeAdapter(items)
        recycler.multiTypeAdapter.register(CommonItemA::class.java, object : CommonItemAViewBinder(this::onNavItemClicked) {
            override fun onCreateViewHolder(image: AppCompatImageView, title: AppCompatTextView) {
                @Suppress("DEPRECATION")
                image.setColorFilter(resources.getColor(R.color.netease_color_primary))
            }
        })
        recycler.multiTypeAdapter.register(NeteasePlaylistHeader::class.java,
                NeteasePlaylistHeaderViewBinder(this::onPlaylistsHeaderClick))
        recycler.itemAnimator = object : DefaultItemAnimator() {

            var animatorExpandIndicator: Animator? = null

            override fun onChangeStarting(holder: RecyclerView.ViewHolder, oldItem: Boolean) {
//                start change animation
                items[holder.adapterPosition].takeIf { it is NeteasePlaylistHeader }?.let {
                    it as NeteasePlaylistHeader
                    if (it.isExpanded) {
                        holder.itemView.iconExpandIndicator.animateToRotation(90f)
                    } else {
                        holder.itemView.iconExpandIndicator.animateToRotation(0f)
                    }
                }
            }

            override fun onChangeFinished(item: RecyclerView.ViewHolder?, oldItem: Boolean) {
                super.onChangeFinished(item, oldItem)
                animatorExpandIndicator?.end()
            }

            private fun ImageView.animateToRotation(r: Float) {
                if (rotation == r) {
                    return
                }
                animatorExpandIndicator = ObjectAnimator.ofFloat(this, "rotation", rotation, r)
                        .setDuration(changeDuration)
                animatorExpandIndicator?.start()
            }
        }
        loadData()
    }

    private fun loadData() {
        items.clear()
        items.addAll(navItems)
        items.add(NeteasePlaylistHeader(getString(R.string.netease_playlist_header_create), true))
        items.add(NeteasePlaylistHeader(getString(R.string.netease_playlist_header_collect), true))
        recycler.multiTypeAdapter.notifyDataSetChanged()
    }

    private suspend fun getItems(): Deferred<List<Any>> = async {
        return@async navItems + NeteaseCloudMusicApi(this@NeteaseMainActivity).getUserPlaylists(1L)
    }

    private suspend fun getUserPlaylists(): List<Any> {
        val id = 1000L
        val playlists = NeteaseCloudMusicApi(this).getUserPlaylists(id)
        val playlistGroup = playlists.groupBy { if (it.userId == id) "personal" else "others" }
        return (playlistGroup["personal"] ?: emptyList()) + (playlistGroup["others"] ?: emptyList())
    }

    private fun onPlaylistsHeaderClick(header: NeteasePlaylistHeader, position: Int) {
        if (header.isExpanded) {
            header.isExpanded = false
            shrinkHeader(header)
        } else {
            header.isExpanded = true
            expandHeader(header)
        }
        recycler.multiTypeAdapter.notifyItemChanged(position)
    }

    private fun expandHeader(header: NeteasePlaylistHeader) {
        val index = recycler.multiTypeAdapter.items.indexOf(header)
        val playlists = when (header.title) {
            getString(R.string.netease_playlist_header_create) -> playlistCreate
            getString(R.string.netease_playlist_header_collect) -> playlistCollect
            else -> emptyList<Any>()
        }
        items.addAll(index = index + 1, elements = playlists)
        recycler.multiTypeAdapter.notifyItemRangeInserted(index + 1, playlists.size)
    }

    private fun shrinkHeader(header: NeteasePlaylistHeader) {
        //the start position need to be shrink
        val index = recycler.multiTypeAdapter.items.indexOf(header)
        //the playlists need be remove from recycler view
        val playlists = when (header.title) {
            getString(R.string.netease_playlist_header_create) -> playlistCreate
            getString(R.string.netease_playlist_header_collect) -> playlistCollect
            else -> emptyList<Any>()
        }
        //remove [playlists]
        items.removeAll(playlists)
        //更新view
        recycler.multiTypeAdapter.notifyItemRangeRemoved(index + 1, playlists.size)
    }


    private fun onNavItemClicked(item: CommonItemA) {
        when (item.title) {
            getString(R.string.netease_nav_title_local) -> {

            }
            getString(R.string.netease_nav_title_download) -> {

            }
            getString(R.string.netease_nav_title_fm) -> {
                startActivity<NeteaseFmActivity>()
            }
            getString(R.string.netease_nav_title_daily) -> {

            }
            getString(R.string.netease_nav_title_latest) -> {

            }
        }
    }

}