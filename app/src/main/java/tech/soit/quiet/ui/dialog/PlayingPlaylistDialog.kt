package tech.soit.quiet.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_playing_playlist.*
import kotlinx.android.synthetic.main.item_music_2.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import me.drakeet.multitype.MultiTypeAdapter
import tech.soit.quiet.R
import tech.soit.quiet.model.vo.Music
import tech.soit.quiet.player.MusicPlayerManager
import tech.soit.quiet.player.PlayMode
import tech.soit.quiet.player.playlist.Playlist
import tech.soit.quiet.ui.activity.base.BaseActivity
import tech.soit.quiet.utils.*
import kotlin.coroutines.CoroutineContext

class PlayingPlaylistDialog : BottomSheetDialogFragment(), CoroutineScope, LifecycleOwner {

    companion object {

        fun getInstance(): PlayingPlaylistDialog {
            return PlayingPlaylistDialog()
        }

    }

    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private var playlist = Playlist.EMPTY

    private var adapter = MultiTypeAdapter()
            .withBinder(Music2Binder())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
        MusicPlayerManager.playlist.observe(this, Observer {
            if (it == null) {
                dismiss()
                return@Observer
            }
            showPlaylist(it)
        })
        MusicPlayerManager.playingMusic.observe(this, object : Observer<Music?> {
            private var former: Int = -1

            override fun onChanged(t: Music?) {
                val current = adapter.items.indexOf(t)
                if (current != -1) {
                    adapter.notifyItemChanged(current)
                }
                if (former != -1) {
                    adapter.notifyItemChanged(former)
                }
                former = current
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.cloneInContext(context).inflate(R.layout.dialog_playing_playlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        applyPrimaryColor((requireActivity() as BaseActivity).colorPrimary)
        super.onViewCreated(view, savedInstanceState)
        recyclerView.adapter = adapter
        containerPlayMode.setOnClickListener {
            playlist.playMode = playlist.playMode.next()
            setPlayMode(playlist.playMode)
        }
        buttonClearAll.setOnClickListener {
            MusicPlayerManager.musicPlayer.playlist = Playlist.EMPTY
            dismiss()
        }

        //reset recycler view height to 2/3 screen height
        val (_, height) = (requireActivity() as BaseActivity).getWindowSize()
        recyclerView.layoutParams.height = (height * 0.6f).toInt()
    }

    private fun applyPrimaryColor(colorPrimary: Int) {
        headerLayout.setBackgroundColor(colorPrimary)
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

    /**
     * 展示 playlist
     */
    private fun showPlaylist(playlist: Playlist) {
        if (playlist == Playlist.EMPTY) {
            return
        }
        this.playlist = playlist

        adapter.submit(playlist.list)

        setPlayMode(playlist.playMode)
        textPlayMode.text = "%s(%d)".format(textPlayMode.text, playlist.list.size)

    }


    private fun setPlayMode(playMode: PlayMode) {
        when (playMode) {
            PlayMode.Sequence -> {
                textPlayMode.setText(R.string.playmode_sequence)
                indicatorPlayMode.setImageResource(R.drawable.ic_repeat_black_24dp)
            }

            PlayMode.Shuffle -> {
                textPlayMode.setText(R.string.playmode_shuffle)
                indicatorPlayMode.setImageResource(R.drawable.ic_shuffle_black_24dp)
            }

            PlayMode.Single -> {
                textPlayMode.setText(R.string.playmode_single)
                indicatorPlayMode.setImageResource(R.drawable.ic_repeat_one_black_24dp)
            }
        }
    }

    @TypeLayoutRes(R.layout.item_music_2)
    inner class Music2Binder : KItemViewBinder<Music>() {

        override fun onBindViewHolder(holder: KViewHolder, item: Music) {
            with(holder.itemView) {
                title.text = item.getTitle()
                subtitle.text = "(%s)".format(item.subTitle)
                setOnClickListener {
                    MusicPlayerManager.musicPlayer.play(item)
                }
                if (playlist.current == item) {
                    indicatorPlaying.isVisible = true
                    buttonLink.isGone = true
                } else {
                    indicatorPlaying.isGone = true
                    buttonLink.isGone = true
                }
                buttonClear.setOnClickListener {
                    launch {
                        if (item == playlist.current) {
                            val isPlayWhenReady = MusicPlayerManager.musicPlayer.mediaPlayer.isPlayWhenReady
                            MusicPlayerManager.musicPlayer.quiet()
                            playlist.getNext()?.let { next ->
                                MusicPlayerManager.musicPlayer.play(next, isPlayWhenReady)
                            }
                        }
                        (playlist.list as? ArrayList)?.remove(item)
                        adapter.notifyItemRemoved(holder.adapterPosition)
                    }
                }

                buttonLink.setOnClickListener {
                    //todo
                }
                subtitle.requestLayout()
            }
        }
    }


}