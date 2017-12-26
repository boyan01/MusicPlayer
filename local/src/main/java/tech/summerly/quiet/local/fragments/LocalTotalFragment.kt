package tech.summerly.quiet.local.fragments

import tech.summerly.quiet.local.LocalMusicApi


/**
 * Created by summer on 17-12-23
 */
class LocalTotalFragment : BaseLocalFragment() {


    override suspend fun loadData(localMusicApi: LocalMusicApi): List<Any> {
        return localMusicApi.getTotalMusics()
    }
}