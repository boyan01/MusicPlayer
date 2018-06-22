package tech.summerly.quiet.netease.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.SimpleItemAnimator
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.launcher.ARouter
import kotlinx.android.synthetic.main.netease_fragment_main.view.*
import tech.summerly.quiet.commonlib.base.BaseFragment
import tech.summerly.quiet.commonlib.component.callback.BottomControllerHost
import tech.summerly.quiet.commonlib.utils.asyncUI
import tech.summerly.quiet.constraints.Search
import tech.summerly.quiet.constraints.Setting
import tech.summerly.quiet.netease.R
import tech.summerly.quiet.netease.adapters.MainRecyclerAdapter
import tech.summerly.quiet.netease.persistence.NeteasePreference
import tech.summerly.quiet.netease.utils.logout
import tech.summerly.quiet.service.netease.NeteaseCloudMusicApi

class NeteaseMainFragment : BaseFragment(), BottomControllerHost, Toolbar.OnMenuItemClickListener {

    private val adapter by lazy { MainRecyclerAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.netease_fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(view) {
        super.onViewCreated(view, savedInstanceState)
        recycler.adapter = adapter
        (recycler.itemAnimator as SimpleItemAnimator?)?.supportsChangeAnimations = false
        toolbar.inflateMenu(R.menu.netease_menu_main)
        toolbar.setOnMenuItemClickListener(this@NeteaseMainFragment)
        adapter.show()
    }


    override fun onMenuItemClick(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.netease_menu_search -> {
                val fragment = ARouter.getInstance().build(Search.FRAGMENT_SEARCH_MAIN).navigation() as Fragment
                requireFragmentManager().beginTransaction()
                        .replace(android.R.id.content, fragment)
                        .addToBackStack(null)
                        .commit()
            }
            R.id.netease_menu_main_logout -> {
                logout()
                onStart()
            }
            R.id.netease_menu_main_setting -> {
                ARouter.getInstance().build(Setting.ACTIVITY_SETTING_MAIN).navigation()
            }
        }
        return true
    }


    override fun onStart() {
        super.onStart()
        loadNeteasePlaylists()
        initBottomController()
    }

    private var isLoading = false

    private fun loadNeteasePlaylists() = asyncUI {
        if (isLoading) {
            return@asyncUI
        }
        isLoading = true
        val user = NeteasePreference.getLoginUser() ?: return@asyncUI
        val playlists = NeteaseCloudMusicApi().getUserPlaylists(user.userId)
        adapter.addCreatedPlaylists(playlists.filter { it.userId == user.userId })
        adapter.addSubscribedPlaylists(playlists.filter { it.userId != user.userId })
        adapter.show()
        isLoading = false
    }

    fun getToolbar(): Toolbar? = view?.toolbar
}