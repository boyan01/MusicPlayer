package tech.summerly.quiet.local.ui

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator
import tech.summerly.quiet.commonlib.fragments.StatedRecyclerFragment
import tech.summerly.quiet.commonlib.model.IMusic
import tech.summerly.quiet.commonlib.player.listenMusicChangePosition
import tech.summerly.quiet.commonlib.utils.observe
import tech.summerly.quiet.commonlib.utils.support.TypedAdapter
import tech.summerly.quiet.commonlib.utils.support.await
import tech.summerly.quiet.local.ui.binder.MusicItemBinder
import tech.summerly.quiet.local.viewmodel.LocalMusicViewModel

class MusicListFragment : StatedRecyclerFragment<IMusic>() {

    companion object {
        const val TOKEN = "local_total"
    }

    private val listAdapter by lazy {
        TypedAdapter()
                .withBinder(IMusic::class, MusicItemBinder())
    }

    override val isLoadOnCreated: Boolean
        get() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listenMusicChangePosition({ listAdapter.list }) { from, to ->
            listAdapter.notifyItemChanged(from)
            listAdapter.notifyItemChanged(to)
        }
        LocalMusicViewModel.instance.allMusic.observe(this) {
            if (it == null) {
                setLoading()
            } else if (it.isEmpty()) {
                setEmpty()
            } else {
                onLoadSuccess(it)
                setComplete()
            }
        }
    }

    override fun initRecyclerView(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.adapter = listAdapter
        (recyclerView.itemAnimator as SimpleItemAnimator?)?.supportsChangeAnimations = false
    }

    override suspend fun loadData(): List<IMusic> {
        return LocalMusicViewModel.instance.allMusic.await() ?: emptyList()
    }

    override fun onLoadSuccess(result: List<IMusic>) {
        super.onLoadSuccess(result)
        listAdapter.submit(result)
    }

}