package tech.summerly.quiet.search.fragments

import android.os.Bundle

/**
 * Created by summer on 18-2-18
 */
internal class MusicsResultTabFragment : BaseResultTabFragment() {

    companion object {

        fun newInstance(query: String): MusicsResultTabFragment {
            return MusicsResultTabFragment().also {
                val bundle = Bundle()
                bundle.putString(KEY_QUERY_TEXT, query)
                it.arguments = bundle
            }
        }
    }

    override suspend fun startQuery(text: String, offset: Int) {
        val results = search().searchMusic(text, offset)
        showItems(results)
    }
}