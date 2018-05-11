/**
 * 作为 MultiType 不支持 DataMapper 的妥协,以后支持了再改吧.
 */
package tech.summerly.quiet.search.adapters.items

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alibaba.android.arouter.launcher.ARouter
import tech.summerly.quiet.commonlib.utils.*
import tech.summerly.quiet.search.BuildConfig
import tech.summerly.quiet.search.R
import tech.summerly.quiet.search.model.SearchResult

private const val PATH_MUSIC = "/items/music"
private const val PATH_ARTIST = "/items/artist"
private const val PATH_ALBUM = "/items/album"

/**
 * 当没有找到对应的ItemBinder时,将会使用此binder
 */
private val sItemNotFoundBinder = SearchNotImplementBinder()


/**
 * 获取其他组件中的 binder
 */
private fun <T> getRemoteBinder(path: String): ItemViewBinder<T>? {
    @Suppress("UNCHECKED_CAST")
    val viewBinder = ARouter.getInstance().build(path).navigation() as? ItemViewBinder<T>
    if (viewBinder == null) {
        log(LoggerLevel.ERROR) { "can not find remote binder: $path !!!" }
    }
    return viewBinder
}


abstract class SearchResultRemoteBinder<T : SearchResult>(path: String) : ItemViewBinder<T>() {

    protected val remoteBinder = getRemoteBinder<Any>(path)

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        if (remoteBinder != null) {
            return remoteBinder.onCreateViewHolder(inflater, parent)
        } else {
            return sItemNotFoundBinder.onCreateViewHolder(inflater, parent)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, item: T) {
        if (remoteBinder == null) {
            sItemNotFoundBinder.onBindViewHolder(holder, item)
        }
    }
}

class SearchMusicItemBinder : SearchResultRemoteBinder<SearchResult.Music>(PATH_MUSIC) {

    override fun onBindViewHolder(holder: ViewHolder, item: SearchResult.Music) {
        remoteBinder?.onBindViewHolder(holder, item.music)
        super.onBindViewHolder(holder, item)
    }
}

class SearchAlbumItemBinder : SearchResultRemoteBinder<SearchResult.Album>(PATH_ALBUM) {
    override fun onBindViewHolder(holder: ViewHolder, item: SearchResult.Album) {
        remoteBinder?.onBindViewHolder(holder, item.album)
        super.onBindViewHolder(holder, item)
    }
}

class SearchArtistItemBinder : SearchResultRemoteBinder<SearchResult.Artist>(PATH_ARTIST) {

    override fun onBindViewHolder(holder: ViewHolder, item: SearchResult.Artist) {
        remoteBinder?.onBindViewHolder(holder, item.artist)
        super.onBindViewHolder(holder, item)
    }
}


class SearchNotImplementBinder : ItemViewBinder2<SearchResult>() {
    override val layoutId: Int
        get() = R.layout.search_item_result_not_implement

    override fun onBindViewHolder(holder: ViewHolder, item: SearchResult) {
        if (!BuildConfig.DEBUG) {
            holder.itemView.gone()
        }
    }
}