package tech.summerly.quiet.netease.adapters

import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import tech.summerly.quiet.commonlib.utils.support.TypedAdapter
import tech.summerly.quiet.netease.adapters.main.*
import tech.summerly.quiet.service.netease.result.PlaylistResultBean

internal class MainRecyclerAdapter() : TypedAdapter() {

    private val items = ArrayList<Any>()

    private val playlistCreated = ArrayList<Any>()
    private val playlistCollected = ArrayList<Any>()

    init {
        withBinder(UserInfo::class, UserInfoBinder())
        withBinder(Navigation::class, NavigationViewBinder())
        withBinder(PlaylistHeader::class, PlaylistHeaderViewBinder(this::onPlaylistHeaderClick))
        withBinder(PlaylistResultBean.PlaylistBean::class, NeteasePlaylistItemViewBinder())
    }

    //添加用户自己创建的歌单数据
    fun addCreatedPlaylists(playlists: List<Any>) {
        playlistCreated.clear()
        playlistCreated.addAll(playlists)
    }

    //添加用户自己订阅的歌单数据
    fun addSubscribedPlaylists(playlists: List<Any>) {
        playlistCollected.clear()
        playlistCollected.addAll(playlists)
    }

    fun show() {
        items.clear()
        with(items) {
            add(UserInfo())
            add(Navigation)
            add(PlaylistHeader.CREATED)
            addAll(playlistCreated)
            add(PlaylistHeader.SUBSCRIPTION)
            addAll(playlistCollected)
        }
        submit(ArrayList(items))
    }

    private fun onPlaylistHeaderClick(header: PlaylistHeader, position: Int) {
        if (header.state == PlaylistHeader.STATE_LOADING) {
            return
        }
        if (header.state == PlaylistHeader.STATE_EXPANDED) {
            header.state = PlaylistHeader.STATE_CLOSED
            shrinkPlaylists(header)
        } else {
            header.state = PlaylistHeader.STATE_EXPANDED
            expandPlaylists(header)
        }
        notifyItemChanged(position)
    }

    fun expandPlaylists(header: PlaylistHeader) {
        val index = items.indexOf(header)
        val playlists = when (header) {
            PlaylistHeader.CREATED -> playlistCreated
            PlaylistHeader.SUBSCRIPTION -> playlistCollected
        }
        items.addAll(index = index + 1, elements = playlists)
        setList(ArrayList(items))
        notifyItemRangeInserted(index + 1, playlists.size)
    }

    fun shrinkPlaylists(header: PlaylistHeader) {
        //the start position need to be shrink
        val index = items.indexOf(header)
        //the playlists need be remove from recycler view
        val playlists = when (header) {
            PlaylistHeader.CREATED -> playlistCreated
            PlaylistHeader.SUBSCRIPTION -> playlistCollected
        }
        //remove [playlists]
        items.removeAll(playlists)
        setList(ArrayList(items))
        //更新view
        notifyItemRangeRemoved(index + 1, playlists.size)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.itemAnimator = object : DefaultItemAnimator() {
            override fun onChangeStarting(item: RecyclerView.ViewHolder, oldItem: Boolean) {
                val data = getItem(item.adapterPosition)
                if (item is PlaylistHeaderViewHolder && data is PlaylistHeader) {
                    item.animateIndicator(data.state)
                }
            }

            override fun onChangeFinished(item: RecyclerView.ViewHolder?, oldItem: Boolean) {
                super.onChangeFinished(item, oldItem)
                PlaylistHeaderViewHolder.finishAnimator()
            }
        }
    }

    init {
        setList(items)
    }

}