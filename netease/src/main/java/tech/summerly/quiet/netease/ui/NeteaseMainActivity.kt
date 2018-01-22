package tech.summerly.quiet.netease.ui

import android.animation.Animator
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import kotlinx.android.synthetic.main.netease_activity_main.*
import kotlinx.android.synthetic.main.netease_header_playlist.view.*
import kotlinx.coroutines.experimental.launch
import me.drakeet.multitype.MultiTypeAdapter
import org.jetbrains.anko.startActivity
import tech.summerly.quiet.commonlib.base.BaseActivity
import tech.summerly.quiet.commonlib.fragments.BottomControllerFragment
import tech.summerly.quiet.commonlib.items.CommonItemA
import tech.summerly.quiet.commonlib.items.CommonItemAViewBinder
import tech.summerly.quiet.commonlib.mvp.BaseView
import tech.summerly.quiet.commonlib.utils.GlideApp
import tech.summerly.quiet.commonlib.utils.multiTypeAdapter
import tech.summerly.quiet.commonlib.utils.popupMenu
import tech.summerly.quiet.netease.R
import tech.summerly.quiet.netease.api.NeteaseCloudMusicApi
import tech.summerly.quiet.netease.api.result.LoginResultBean
import tech.summerly.quiet.netease.api.result.PlaylistResultBean
import tech.summerly.quiet.netease.persistence.NeteasePreference
import tech.summerly.quiet.netease.ui.items.NeteasePlaylistHeader
import tech.summerly.quiet.netease.ui.items.NeteasePlaylistHeaderViewBinder
import tech.summerly.quiet.netease.ui.items.NeteasePlaylistItemViewBinder
import tech.summerly.quiet.netease.utils.logout

/**
 * Created by summer on 17-12-30
 */
@Route(path = "/netease/main")
class NeteaseMainActivity : BaseActivity(), BaseView, BottomControllerFragment.BottomControllerContainer {

    companion object {
        private const val REQUEST_LOGIN = 101
    }

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

    private var isLogin = false

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
        recycler.multiTypeAdapter.register(PlaylistResultBean.PlaylistBean::class.java, NeteasePlaylistItemViewBinder())
        recycler.itemAnimator = object : DefaultItemAnimator() {

            var animatorExpandIndicator: Animator? = null

            override fun onChangeStarting(holder: RecyclerView.ViewHolder, oldItem: Boolean) {
                //start change animation
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
        moreAction.setOnClickListener {
            val menu = popupMenu(it, R.menu.netease_menu_main) {
                when (it.itemId) {
                    R.id.netease_menu_main_logout -> {
                        logout()
                        loadData()
                    }
                    R.id.netease_menu_main_setting -> {
                        //TODO goto setting
                    }
                }
                true
            }
            if (NeteasePreference.getLoginUser() == null) {
                menu.menu.removeItem(R.id.netease_menu_main_logout)
            }
        }
        loadData()
    }

    private fun loadData() {
        items.clear()
        items.addAll(navItems)
        items.add(NeteasePlaylistHeader(getString(R.string.netease_playlist_header_create), true))
        items.add(NeteasePlaylistHeader(getString(R.string.netease_playlist_header_collect), true))
        loadNeteasePlaylists()
        recycler.multiTypeAdapter.notifyDataSetChanged()
    }

    private fun loadNeteasePlaylists() {
        launch(UI) {
            val user = NeteasePreference.getLoginUser()
            if (user == null) {
                setNotLogin()
                return@launch
            }
            setLogin(user)
            val id = user.userId
            val playlists = NeteaseCloudMusicApi(this@NeteaseMainActivity)
                    .getUserPlaylists(id)
            playlistCreate.clear()
            playlistCreate.addAll(playlists.filter { it.userId == id })
            playlistCollect.clear()
            playlistCollect.addAll(playlists.filter { it.userId != id })
            showPlaylists()
        }
    }

    private fun setLogin(profile: LoginResultBean.Profile) {
        isLogin = true
        textUserName.text = profile.nickname
        GlideApp.with(this).load(profile.avatarUrl).into(imageUser)
    }

    private fun setNotLogin() {
        isLogin = false
        findHeader(getString(R.string.netease_playlist_header_collect)).apply {
            isLoading = false
            isExpanded = false
        }
        findHeader(getString(R.string.netease_playlist_header_create)).apply {
            isLoading = false
            isExpanded = false
        }
        recycler.multiTypeAdapter.notifyDataSetChanged()
        playlistCreate.clear()
        playlistCollect.clear()

        textUserName.text = getString(R.string.netease_title_not_login)
        textUserName.setOnClickListener {
            startActivityForResult(Intent(this, LoginActivity::class.java), REQUEST_LOGIN)
        }
    }

    override fun onResume() {
        super.onResume()
        if (isLogin && !tech.summerly.quiet.netease.utils.isLogin()) {
            setNotLogin()
        } else if (!isLogin && tech.summerly.quiet.netease.utils.isLogin()) {
            loadData()
        }
    }


    /**
     * find the playlist'header from [items] by title
     */
    private fun findHeader(title: String): NeteasePlaylistHeader {
        return items.find { it is NeteasePlaylistHeader && it.title == title } as NeteasePlaylistHeader
    }

    /**
     * display playlists
     * insert the [playlistCollect] and [playlistCreate] into list [items] , if header is expanded
     */
    private fun showPlaylists() {
        //check if header is expanded
        val headerCreate = findHeader(getString(R.string.netease_playlist_header_create))
        headerCreate.isLoading = false
        if (headerCreate.isExpanded) {
            items.addAll(items.indexOf(headerCreate) + 1, playlistCreate)
        }

        val headerCollect = findHeader(getString(R.string.netease_playlist_header_collect))
        headerCollect.isLoading = false
        if (headerCollect.isExpanded) {
            items.addAll(items.indexOf(headerCollect) + 1, playlistCollect)
        }
        recycler.multiTypeAdapter.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_LOGIN && resultCode == Activity.RESULT_OK) {
            loadData()
            textUserName.setOnClickListener(null)
        }
    }


    /**
     * invoke when playlist'header has been clicked
     */
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
                ARouter.getInstance().build("/local/main").navigation()
            }
            getString(R.string.netease_nav_title_download) -> {

            }
            getString(R.string.netease_nav_title_fm) -> {
                ARouter.getInstance().build("/netease/fm").withBoolean("play", true).navigation()
            }
            getString(R.string.netease_nav_title_daily) -> {
                startActivity<NeteaseDailyRecommendActivity>()
            }
            getString(R.string.netease_nav_title_latest) -> {

            }
        }
    }

}