package tech.summerly.quiet.local.fragments

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import me.drakeet.multitype.MultiTypeAdapter
import tech.summerly.quiet.commonlib.player.MusicPlayerManager
import tech.summerly.quiet.commonlib.utils.multiTypeAdapter
import tech.summerly.quiet.commonlib.utils.observe
import tech.summerly.quiet.service.local.LocalMusicApi
import tech.summerly.quiet.service.local.database.database.Table


/**
 * Created by summer on 17-12-23
 */
class LocalTotalFragment : BaseLocalFragment() {

    private val adapter: MultiTypeAdapter?
        get() = (view as? RecyclerView)?.multiTypeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MusicPlayerManager.musicChange.observe(this) {
            val (old, new) = it ?: return@observe
            val oldIndex = adapter?.items?.indexOf(old)
            val newIndex = adapter?.items?.indexOf(new)
            oldIndex?.let { adapter?.notifyItemChanged(it) }
            newIndex?.let { adapter?.notifyItemChanged(it) }
        }
    }

    override fun isInterestedChange(table: Table): Boolean {
        return table == Table.Music
    }


    override suspend fun loadData(localMusicApi: LocalMusicApi): List<Any> {
        return localMusicApi.getTotalMusics()
    }
}