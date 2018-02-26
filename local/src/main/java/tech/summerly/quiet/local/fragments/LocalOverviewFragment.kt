package tech.summerly.quiet.local.fragments

import tech.summerly.quiet.commonlib.items.CommonItemA
import tech.summerly.quiet.service.local.LocalMusicApi
import tech.summerly.quiet.local.R
import tech.summerly.quiet.service.local.database.database.Table
import tech.summerly.quiet.local.fragments.items.LocalPlaylistHeaderViewBinder

/**
 * provide an overview for LocalMusic : LocalFavMusic , LocalMusicMonitor
 */
class LocalOverviewFragment : BaseLocalFragment() {

    override fun isInterestedChange(table: Table): Boolean {
        return table == Table.Playlist || table == Table.PlaylistMusic
    }


    private val navItems by lazy {
        listOf(
                CommonItemA(getString(R.string.local_overview_nav_total), R.drawable.local_ic_library_music_black_24dp),
                CommonItemA(getString(R.string.local_overview_nav_artist), R.drawable.local_ic_artist_black_24dp),
                CommonItemA(getString(R.string.local_overview_nav_album), R.drawable.local_ic_album_black_24dp),
                CommonItemA(getString(R.string.local_overview_nav_trend), R.drawable.local_ic_trending_up_black_24dp),
                CommonItemA(getString(R.string.local_overview_nav_latest), R.drawable.local_ic_latest_black_24dp)
        )
    }
    private val header = LocalPlaylistHeaderViewBinder.PlaylistHeader()

    suspend override fun loadData(localMusicApi: LocalMusicApi): List<Any> {
        return navItems + header + localMusicApi.getPlaylists().await()
    }
}