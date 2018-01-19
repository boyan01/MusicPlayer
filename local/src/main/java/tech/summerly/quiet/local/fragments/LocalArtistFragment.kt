package tech.summerly.quiet.local.fragments

import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import org.jetbrains.anko.support.v4.dip
import tech.summerly.quiet.local.LocalMusicApi
import tech.summerly.quiet.local.database.database.Table

/**
 * Created by summer on 17-12-24
 */
class LocalArtistFragment : BaseLocalFragment() {

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

    override fun isInterestedChange(table: Table): Boolean {
        return table == Table.Artist
    }

    override suspend fun loadData(localMusicApi: LocalMusicApi): List<Any> {
        return localMusicApi.getArtists().await()
    }

    override fun getSpanCount() = 2

}