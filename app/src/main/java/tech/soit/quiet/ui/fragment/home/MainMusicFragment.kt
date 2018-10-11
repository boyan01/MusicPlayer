package tech.soit.quiet.ui.fragment.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_main_music.*
import kotlinx.android.synthetic.main.item_main_navigation.view.*
import kotlinx.coroutines.experimental.launch
import me.drakeet.multitype.MultiTypeAdapter
import tech.soit.quiet.R
import tech.soit.quiet.model.vo.PlayList
import tech.soit.quiet.ui.activity.LocalMusicActivity
import tech.soit.quiet.ui.fragment.base.BaseFragment
import tech.soit.quiet.ui.fragment.home.cloud.PlayListViewBinder
import tech.soit.quiet.ui.fragment.home.viewmodel.MainMusicViewModel
import tech.soit.quiet.utils.annotation.LayoutId
import tech.soit.quiet.utils.component.log
import tech.soit.quiet.utils.component.support.dimen
import tech.soit.quiet.utils.submit
import tech.soit.quiet.utils.withBinder
import tech.soit.quiet.utils.withEmptyBinder
import tech.soit.quiet.utils.withLoadingBinder

/**
 * main Fragment of music
 */
@LayoutId(R.layout.fragment_main_music)
class MainMusicFragment : BaseFragment() {

    private val mainMusicViewMode by lazyViewModel<MainMusicViewModel>()

    private val neteaseRepository get() = mainMusicViewMode.getNeteaseRepository()

    private lateinit var adapter: MultiTypeAdapter


    private var rangeCreated = IntRange(0, 0)
    private var rangeCollection = IntRange(0, 0)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //init navigation items
        initNavigation()

        //init recycler view
        adapter = MultiTypeAdapter()
                .withBinder(PlayListViewBinder())
                .withEmptyBinder()
                .withLoadingBinder()
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            private var totalY = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                totalY += dy
                val position = if (totalY in rangeCreated) {
                    0
                } else {
                    1
                }
                val offset = totalY.toFloat() / rangeCollection.last
                log { "offset = $offset" }
                tabLayoutPlayLists.setScrollPosition(position, offset, true)
            }
        })

        launch {
            val user = neteaseRepository.getLoginUser()
            enterNotLoginMode(user == null)
            user ?: return@launch
            val playLists = neteaseRepository.getUserPlayerList(user.getId())
            computePlayListRange(playLists, user.getId())
            adapter.submit(playLists)
        }
    }

    private fun computePlayListRange(playLists: List<PlayList>, userId: Long) {
        val createdCount = playLists.count { it.getUserId() == userId }
        val height = dimen(R.dimen.height_item_play_list).toInt()
        rangeCreated = 0..(createdCount * height)
        rangeCollection = (createdCount * height)..(playLists.size * height)
    }

    private fun enterNotLoginMode(enter: Boolean) {

    }

    private fun initNavigation() {
        with(navLayoutLocal) {
            imageIcon.setImageResource(R.drawable.ic_music_note_black_24dp)
            textTitle.setText(R.string.nav_local_music)
            setOnClickListener {
                startActivity(Intent(requireActivity(), LocalMusicActivity::class.java))
            }
        }

        with(navLayoutHistory) {
            imageIcon.setImageResource(R.drawable.ic_history_black_24dp)
            textTitle.setText(R.string.nav_history)
        }

        with(navLayoutDownload) {
            imageIcon.setImageResource(R.drawable.ic_file_download_black_24dp)
            textTitle.setText(R.string.nav_download)
        }

        with(navLayoutCollection) {
            imageIcon.setImageResource(R.drawable.ic_collections_black_24dp)
            textTitle.setText(R.string.nav_collection)
        }

    }


}