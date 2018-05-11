package tech.summerly.quiet.search.repository

import tech.summerly.quiet.commonlib.objects.PortionList
import tech.summerly.quiet.search.model.SearchInterface
import tech.summerly.quiet.search.model.SearchResult
import tech.summerly.quiet.service.netease.NeteaseCloudMusicApi

class NeteaseRepository : SearchInterface {


    private val neteaseCloudMusicApi = NeteaseCloudMusicApi()

    override suspend fun searchMusic(key: String, offset: Int): PortionList<SearchResult.Music> {
        return neteaseCloudMusicApi.searchMusic(key, offset).map { SearchResult.Music(it) }
    }

    override suspend fun searchArtist(key: String, offset: Int): PortionList<SearchResult.Artist> {
        TODO()
    }

    override suspend fun searchAlbum(key: String, offset: Int): PortionList<SearchResult.Album> {
        TODO()
    }

}