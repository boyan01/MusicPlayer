package tech.summerly.quiet.commonlib.fragments

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.common_fragment_playing_list.*
import kotlinx.android.synthetic.main.common_fragment_playing_list.view.*
import me.drakeet.multitype.MultiTypeAdapter
import tech.summerly.quiet.commonlib.R
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.fragments.items.PlayingMusicItemViewBinder
import tech.summerly.quiet.commonlib.player.MusicPlayerManager
import tech.summerly.quiet.commonlib.player.PlayMode
import tech.summerly.quiet.commonlib.utils.observeFilterNull

/**
 * author: summerly
 * email: yangbinyhbn@gmail.com
 * description: display current playing music list , and provider button to change playMode
 */
class PlayingListFragment : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "PlayingListFragment"
    }

    private val musicList: List<Music>
        get() = MusicPlayerManager.player.playlist.musics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MusicPlayerManager.playMode.observeFilterNull(this) {
            when (it) {
                PlayMode.Sequence -> {
                    textPlayMode.text = getString(R.string.common_playmode_sequence)
                    indicatorPlayMode.setImageResource(R.drawable.common_ic_repeat_black_24dp)
                }

                PlayMode.Shuffle -> {
                    textPlayMode.text = getString(R.string.common_playmode_shuffle)
                    indicatorPlayMode.setImageResource(R.drawable.common_ic_shuffle_black_24dp)
                }

                PlayMode.Single -> {
                    textPlayMode.text = getString(R.string.common_playmode_single)
                    indicatorPlayMode.setImageResource(R.drawable.common_ic_repeat_one_black_24dp)
                }
            }
        }

        MusicPlayerManager.musicChange.observeFilterNull(this) {
            val old = musicList.indexOf(it.first)
            val new = musicList.indexOf(it.second)
            view?.list?.adapter?.apply {
                if (old != -1) {
                    notifyItemChanged(old)
                }
                if (new != -1) {
                    notifyItemChanged(new)
                }
            }
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.cloneInContext(context).inflate(R.layout.common_fragment_playing_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(view) {
        val adapter = MultiTypeAdapter(musicList).also {
            it.register(Music::class.java, PlayingMusicItemViewBinder())
        }
        list.adapter = adapter
        adapter.notifyDataSetChanged()
        setEvent()
    }

    override fun onStart() {
        super.onStart()
        scrollToCurrentPlaying()
    }

    private fun scrollToCurrentPlaying() {
        val list = view?.list ?: return
        val current = MusicPlayerManager.player.playlist.current ?: return
        val position = musicList.indexOf(current)
        if (position < 0 || position > musicList.size) {
            return
        }
        (list.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(position, 2)
    }

    private fun View.setEvent() {
        containerPlayMode.setOnClickListener {
            MusicPlayerManager.player.playMode = MusicPlayerManager.player.playMode.next()
        }
        indicatorPlayMode.setOnClickListener {
            MusicPlayerManager.player.playMode = MusicPlayerManager.player.playMode.next()
        }
        buttonClearAll.setOnClickListener {
            //TODO clear
            MusicPlayerManager.player.destroy()
            dismiss()
        }
    }
}