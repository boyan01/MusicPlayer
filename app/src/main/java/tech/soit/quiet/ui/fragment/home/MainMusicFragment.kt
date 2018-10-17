package tech.soit.quiet.ui.fragment.home

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.content_main_music_user_info.*
import kotlinx.android.synthetic.main.fragment_main_music.*
import kotlinx.android.synthetic.main.item_main_navigation.view.*
import kotlinx.coroutines.experimental.launch
import me.drakeet.multitype.MultiTypeAdapter
import tech.soit.quiet.R
import tech.soit.quiet.model.vo.PlayList
import tech.soit.quiet.model.vo.User
import tech.soit.quiet.ui.activity.local.LocalMusicActivity
import tech.soit.quiet.ui.activity.user.LoginActivity
import tech.soit.quiet.ui.fragment.base.BaseFragment
import tech.soit.quiet.ui.fragment.home.cloud.PlayListViewBinder
import tech.soit.quiet.ui.fragment.home.viewmodel.MainMusicViewModel
import tech.soit.quiet.ui.view.CircleOutlineProvider
import tech.soit.quiet.utils.annotation.LayoutId
import tech.soit.quiet.utils.component.ImageLoader
import tech.soit.quiet.utils.component.log
import tech.soit.quiet.utils.component.support.color
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

    companion object {

        private const val REQUEST_LOGIN = 1203

    }

    private val mainMusicViewMode by lazyViewModel<MainMusicViewModel>()

    private val neteaseRepository get() = mainMusicViewMode.getNeteaseRepository()

    private lateinit var adapter: MultiTypeAdapter


    private var rangeCreated = IntRange(0, 0)
    private var rangeCollection = IntRange(0, 0)
    private var positionCollectionStart = 0

    private var isLogin = false

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
        tabLayoutPlayLists.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position == 0) {
                    recyclerView.smoothScrollToPosition(0)
                } else {
                    recyclerView.smoothScrollToPosition(positionCollectionStart)
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
        })

        loadData()
    }

    private fun loadData() {
        launch {
            val user = neteaseRepository.getLoginUser()
            checkUser(user)
            user ?: return@launch

            val playLists: List<PlayList>
            try {
                playLists = neteaseRepository.getUserPlayerList(user.getId())
                computePlayListRange(playLists, user.getId())
            } catch (e: Exception) {
                Toast.makeText(requireContext(), e.message ?: "error", Toast.LENGTH_SHORT).show()
                return@launch
            }
            adapter.submit(playLists)
        }
    }

    private fun computePlayListRange(playLists: List<PlayList>, userId: Long) {
        val createdCount = playLists.count { it.getUserId() == userId }
        positionCollectionStart = createdCount //reset started position of collection
        val height = dimen(R.dimen.height_item_play_list).toInt()
        rangeCreated = 0..(createdCount * height)
        rangeCollection = (createdCount * height)..(playLists.size * height)
    }

    /**
     * check if user has been login
     */
    private fun checkUser(user: User?) {
        isLogin = user != null
        if (user == null) {
            imageUserAvatar.setImageDrawable(ColorDrawable(color(R.color.color_gray)))
            textUserNickname.setText(R.string.user_not_login)
        } else {
            ImageLoader.with(this).load(user.getAvatarUrl()).into(imageUserAvatar)
            textUserNickname.text = user.getNickName()
        }
    }

    private fun initNavigation() {
        imageUserAvatar.outlineProvider = CircleOutlineProvider()
        imageUserAvatar.clipToOutline = true

        layoutUserInfo.setOnClickListener {
            if (!isLogin) {
                startActivityForResult(Intent(requireContext(), LoginActivity::class.java), REQUEST_LOGIN)
            } else {
                log { "has been login" }
            }
        }

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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_LOGIN && resultCode == Activity.RESULT_OK) {
            loadData()
        }
    }


}