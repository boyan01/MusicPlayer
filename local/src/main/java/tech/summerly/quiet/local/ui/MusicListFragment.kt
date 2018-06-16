package tech.summerly.quiet.local.ui

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.fragments.StatedRecyclerFragment
import tech.summerly.quiet.commonlib.model.IMusic
import tech.summerly.quiet.commonlib.player.listenMusicChangePosition
import tech.summerly.quiet.commonlib.utils.support.TypedAdapter
import tech.summerly.quiet.local.ui.binder.MusicItemBinder
import tech.summerly.quiet.local.repository.LocalMusicApi
import tech.summerly.quiet.local.repository.database.Table

class MusicListFragment : StatedRecyclerFragment<Music>() {

    companion object {
        const val TOKEN = "local_total"
    }

    private var version = 0L

    private val listAdapter by lazy {
        TypedAdapter()
                .withBinder(IMusic::class, MusicItemBinder())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Table.Music.listenChange(this) { newVersion ->
            if (newVersion > this.version) {
                loadDataInternal()
            }
        }
        listenMusicChangePosition({ listAdapter.list }) { from, to ->
            listAdapter.notifyItemChanged(from)
            listAdapter.notifyItemChanged(to)
        }
    }

    override fun initRecyclerView(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.adapter = listAdapter
        (recyclerView.itemAnimator as SimpleItemAnimator?)?.supportsChangeAnimations = false
    }

    override suspend fun loadData(): List<Music> {
        return LocalMusicApi.instance.getTotalMusics()
    }

    override fun onLoadSuccess(result: List<Music>) {
        super.onLoadSuccess(result)
        listAdapter.submit(result)
    }

}