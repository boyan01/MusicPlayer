package tech.summerly.quiet.player.ui.music

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import tech.summerly.quiet.constraints.Player
import tech.summerly.quiet.player.R
import tech.summerly.quiet.player.ui.InsetsFragment

@Route(path = Player.FRAGMENT_MUSIC_PLAYER)
class MusicPlayerFragment : InsetsFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.player_fragment_music, container, false)
    }

}