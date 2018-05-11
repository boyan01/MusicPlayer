package tech.summerly.quiet.search.model

import tech.summerly.quiet.commonlib.objects.PortionList

interface SearchInterface {


    suspend fun searchMusic(key: String, offset: Int): PortionList<SearchResult.Music>

    suspend fun searchArtist(key: String, offset: Int): PortionList<SearchResult.Artist>

    suspend fun searchAlbum(key: String, offset: Int): PortionList<SearchResult.Album>

}