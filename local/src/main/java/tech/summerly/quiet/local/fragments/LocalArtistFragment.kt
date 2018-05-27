package tech.summerly.quiet.local.fragments

import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import org.jetbrains.anko.dip
import tech.summerly.quiet.commonlib.fragments.StatedRecyclerFragment
import tech.summerly.quiet.commonlib.utils.support.TypedAdapter
import tech.summerly.quiet.local.LocalModule
import tech.summerly.quiet.local.fragments.items.LocalBigImageItem
import tech.summerly.quiet.local.fragments.items.LocalBigImageItemViewBinder
import tech.summerly.quiet.service.local.LocalMusicApi
import tech.summerly.quiet.service.local.database.database.Table

/**
 * Created by summer on 17-12-24
 */
internal class LocalArtistFragment : StatedRecyclerFragment<LocalBigImageItem>() {


    private val spaceDecoration get() = LocalModule.dip(4)

    private var version = 0L

    private val listAdapter by lazy {
        TypedAdapter().withBinder(LocalBigImageItem::class, LocalBigImageItemViewBinder())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Table.Artist.listenChange(this) { newVersion ->
            if (newVersion > version) {
                loadDataInternal()
                version = newVersion
            }
        }
    }

    override fun initRecyclerView(recyclerView: RecyclerView) {
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                outRect.left = spaceDecoration
                outRect.right = spaceDecoration
                outRect.bottom = spaceDecoration
                outRect.top = spaceDecoration
            }
        })
        recyclerView.adapter = listAdapter

    }

    override fun onLoadSuccess(result: List<LocalBigImageItem>) {
        super.onLoadSuccess(result)
        listAdapter.submit(result)
    }

    override suspend fun loadData(): List<LocalBigImageItem> {
        return LocalMusicApi.instance.getArtists().await().map { LocalBigImageItem(it.name, it.picUri, it) }
    }


}