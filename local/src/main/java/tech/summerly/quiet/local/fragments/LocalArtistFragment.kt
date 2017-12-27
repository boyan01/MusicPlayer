package tech.summerly.quiet.local.fragments

import tech.summerly.quiet.local.LocalMusicApi

/**
 * Created by summer on 17-12-24
 */
class LocalArtistFragment : BaseLocalFragment() {

    suspend override fun loadData(localMusicApi: LocalMusicApi): List<Any> {
        return localMusicApi.getArtists()
    }

    override fun getSpanCount() = 2

}