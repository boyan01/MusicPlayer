package tech.summerly.quiet.local.fragments

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.fragments.StatedRecyclerFragment
import tech.summerly.quiet.commonlib.utils.support.TypedAdapter
import tech.summerly.quiet.local.fragments.binder.MusicItemBinder
import tech.summerly.quiet.service.local.LocalMusicApi
import tech.summerly.quiet.service.local.database.database.Table

class MusicListFragment : StatedRecyclerFragment<Music>() {

    companion object {
        const val TOKEN = "local_total"
    }

    private var version = 0L

    private val listAdapter by lazy {
        TypedAdapter()
                .withBinder(Music::class, MusicItemBinder())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Table.Music.listenChange(this) { newVersion ->
            if (newVersion > this.version) {
                loadDataInternal()
            }
        }
    }

    override fun initRecyclerView(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.adapter = listAdapter
    }

    override suspend fun loadData(): List<Music> {
        return LocalMusicApi.instance.getTotalMusics()
    }

    override fun onLoadSuccess(result: List<Music>) {
        super.onLoadSuccess(result)
        listAdapter.submit(result)
    }

}