package tech.summerly.quiet.search.fragments.result

import android.os.Bundle
import tech.summerly.quiet.commonlib.objects.PortionList
import tech.summerly.quiet.search.model.SearchResult

/**
 * Created by summer on 18-2-18
 */
internal class MusicsResultTabFragment : BaseResultTabFragment() {

    override fun search(query: String?) {

    }

    companion object {


    }

    override suspend fun startQuery(text: String, offset: Int): PortionList<SearchResult.Music> {
        return search().searchMusic(text, offset)

    }
}