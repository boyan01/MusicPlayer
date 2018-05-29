package tech.summerly.quiet.netease.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.netease_fragment_main.view.*
import tech.summerly.quiet.commonlib.base.BaseFragment
import tech.summerly.quiet.commonlib.utils.asyncUI
import tech.summerly.quiet.netease.R
import tech.summerly.quiet.netease.adapters.MainRecyclerAdapter
import tech.summerly.quiet.netease.persistence.NeteasePreference
import tech.summerly.quiet.service.netease.NeteaseCloudMusicApi

class NeteaseMainFragment : BaseFragment() {


    private val adapter by lazy { MainRecyclerAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.netease_fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(view) {
        super.onViewCreated(view, savedInstanceState)
        recycler.adapter = adapter
        adapter.show()
    }

    override fun onStart() {
        super.onStart()
        loadNeteasePlaylists()
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
}