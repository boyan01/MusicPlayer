package tech.summerly.quiet.local.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.local_fragment_music_list.view.*
import me.drakeet.multitype.MultiTypeAdapter
import tech.summerly.quiet.commonlib.base.BaseFragment
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.bean.MusicType
import tech.summerly.quiet.commonlib.player.musicPlayer
import tech.summerly.quiet.commonlib.utils.multiTypeAdapter
import tech.summerly.quiet.local.R
import tech.summerly.quiet.local.fragments.items.LocalMusicItemViewBinder

/**
 * Created by summer
 * to display local music list
 */
internal class LocalMusicListFragment : BaseFragment() {

    companion object {
        private const val KEY_TITLE = "TITLE"
        private const val KEY_MUSICS = "musics"

        operator fun invoke(title: String, musics: ArrayList<Music>): LocalMusicListFragment {
            val fragment = LocalMusicListFragment()
            fragment.arguments = Bundle().also {
                it.putString(KEY_TITLE, title)
                it.putSerializable(KEY_MUSICS, musics)
            }
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.local_fragment_music_list, container, false)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(view) {
        super.onViewCreated(view, savedInstanceState)
        val musics: List<Music> = arguments?.getSerializable(KEY_MUSICS) as? ArrayList<Music>
                ?: emptyList()
        val title = (arguments?.getString(KEY_TITLE) ?: "NULL") + "(共${musics.size}首)"
        textTitle.text = title
        list.adapter = MultiTypeAdapter(musics)
        list.multiTypeAdapter.register(Music::class.java, LocalMusicItemViewBinder(this@LocalMusicListFragment::onMusicClick))
        list.multiTypeAdapter.notifyDataSetChanged()
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }


    private fun onMusicClick(music: Music) = runWithRoot {
        musicPlayer.setType(MusicType.LOCAL)
        val musicList = list.multiTypeAdapter.items.filterIsInstance(Music::class.java)
        musicPlayer.playlistProvider.setPlaylist(musicList)
        musicPlayer.play(music)
    }


}