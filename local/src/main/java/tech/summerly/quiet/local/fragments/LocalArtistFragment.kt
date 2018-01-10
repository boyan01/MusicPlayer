package tech.summerly.quiet.local.fragments

import tech.summerly.quiet.local.LocalMusicApi
import tech.summerly.quiet.local.database.database.Table

/**
 * Created by summer on 17-12-24
 */
class LocalArtistFragment : BaseLocalFragment() {

    override fun isInterestedChange(table: Table): Boolean {
        return table == Table.Artist
    }

    suspend override fun loadData(localMusicApi: LocalMusicApi): List<Any> {
        return localMusicApi.getArtists().await()
    }

    override fun getSpanCount() = 2

}