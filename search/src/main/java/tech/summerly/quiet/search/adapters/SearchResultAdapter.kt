package tech.summerly.quiet.search.adapters

import me.drakeet.multitype.MultiTypeAdapter
import tech.summerly.quiet.search.adapters.items.SearchAlbumItemBinder
import tech.summerly.quiet.search.adapters.items.SearchArtistItemBinder
import tech.summerly.quiet.search.adapters.items.SearchMusicItemBinder
import tech.summerly.quiet.search.adapters.items.SearchNotImplementBinder
import tech.summerly.quiet.search.model.SearchResult

class SearchResultAdapter(items: List<*>) : MultiTypeAdapter(items) {

    init {
        val musicItemBinder = SearchMusicItemBinder()
        val artistItemBinder = SearchArtistItemBinder()
        val albumItemBinder = SearchAlbumItemBinder()

        register(SearchResult.Music::class.java, musicItemBinder)
        register(SearchResult.Artist::class.java, artistItemBinder)
        register(SearchResult.Album::class.java, albumItemBinder)
        register(SearchResult::class.java, SearchNotImplementBinder())

        musicItemBinder.setRemoteBinderAdapter(this)
        artistItemBinder.setRemoteBinderAdapter(this)
        albumItemBinder.setRemoteBinderAdapter(this)
    }

}