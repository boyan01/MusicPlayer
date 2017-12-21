package tech.summerly.quiet.commonlib.player

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders

/**
 * Created by summer on 17-12-17
 *
 */
object MusicPlayerManager : ViewModel() {

    fun getMusicPlayer(): BaseMusicPlayer = TODO()

    fun getPlayerState(): LiveData<String> = TODO()

}