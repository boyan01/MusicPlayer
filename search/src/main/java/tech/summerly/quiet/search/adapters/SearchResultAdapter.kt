package tech.summerly.quiet.search.adapters

import me.drakeet.multitype.ItemViewBinder
import me.drakeet.multitype.MultiTypeAdapter
import tech.summerly.quiet.search.adapters.items.SearchAlbumItemBinder
import tech.summerly.quiet.search.adapters.items.SearchArtistItemBinder
import tech.summerly.quiet.search.adapters.items.SearchMusicItemBinder
import tech.summerly.quiet.search.adapters.items.SearchNotImplementBinder
import tech.summerly.quiet.search.model.SearchResult

class SearchResultAdapter(items: List<*>) : MultiTypeAdapter(items) {

    init {

        @Suppress("UNCHECKED_CAST")
        register(SearchResult::class.java)
                .to(
                        SearchMusicItemBinder() as ItemViewBinder<SearchResult, *>,
                        SearchArtistItemBinder() as ItemViewBinder<SearchResult, *>,
                        SearchAlbumItemBinder() as ItemViewBinder<SearchResult, *>,
                        SearchNotImplementBinder()
                )
                .withLinker { _, t ->
                    when (t) {
                        is SearchResult.Music -> 0
                        is SearchResult.Artist -> 1
                        is SearchResult.Album -> 2
                        else -> 3
                    }
                }
    }

}