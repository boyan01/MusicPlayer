package tech.summerly.quiet.search.model

import tech.summerly.quiet.commonlib.model.IAlbum
import tech.summerly.quiet.commonlib.model.IArtist
import tech.summerly.quiet.commonlib.model.IMusic

sealed class SearchResult {

    class Music(val music: IMusic) : SearchResult()

    class Artist(val artist: IArtist) : SearchResult()

    class Album(val album: IAlbum) : SearchResult()

    class Interest(val searchResult: SearchResult) : SearchResult()

}