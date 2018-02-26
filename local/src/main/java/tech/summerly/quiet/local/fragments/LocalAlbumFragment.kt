package tech.summerly.quiet.local.fragments

import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import org.jetbrains.anko.support.v4.dip
import tech.summerly.quiet.service.local.LocalMusicApi
import tech.summerly.quiet.service.local.database.database.Table
import tech.summerly.quiet.local.fragments.items.LocalBigImageItem

/**
 * Created by summer
 */
internal class LocalAlbumFragment : BaseLocalFragment() {

    override fun isInterestedChange(table: Table): Boolean {
        return table == Table.Album
    }

    override suspend fun loadData(localMusicApi: LocalMusicApi): List<Any> {
        return localMusicApi.getAlbums().await().map { LocalBigImageItem(it.name, it.picUri, it) }
    }

    private val spaceDecoration get() = dip(4)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view as? RecyclerView ?: return
        view.layoutManager = StaggeredGridLayoutManager(getSpanCount(),
                StaggeredGridLayoutManager.VERTICAL)
        view.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                outRect.left = spaceDecoration
                outRect.right = spaceDecoration
                outRect.bottom = spaceDecoration
                outRect.top = spaceDecoration
            }
        })
    }

    override fun getSpanCount(): Int = 2

}