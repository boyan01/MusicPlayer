package tech.summerly.quiet.local.ui

import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import org.jetbrains.anko.dip
import tech.summerly.quiet.commonlib.fragments.StatedRecyclerFragment
import tech.summerly.quiet.commonlib.utils.support.TypedAdapter
import tech.summerly.quiet.local.LocalModule
import tech.summerly.quiet.local.ui.items.LocalBigImageItem
import tech.summerly.quiet.local.ui.items.LocalBigImageItemViewBinder
import tech.summerly.quiet.local.repository.LocalMusicApi
import tech.summerly.quiet.local.repository.database.Table

/**
 * Created by summer
 */
internal class LocalAlbumFragment : StatedRecyclerFragment<LocalBigImageItem>() {


    private var version = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Table.Album.listenChange(this) { newVersion ->
            if (newVersion > version) {
                loadDataInternal()
                version = newVersion
            }
        }
    }

    private val listAdapter by lazy {
        TypedAdapter().withBinder(LocalBigImageItem::class, LocalBigImageItemViewBinder())
    }

    override suspend fun loadData(): List<LocalBigImageItem> {
        return LocalMusicApi.instance.getAlbums().await().map { LocalBigImageItem(it.name, it.picUri, it) }
    }

    override fun onLoadSuccess(result: List<LocalBigImageItem>) {
        super.onLoadSuccess(result)
        listAdapter.submit(result)
    }

    private val spaceDecoration get() = LocalModule.dip(4)

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


}