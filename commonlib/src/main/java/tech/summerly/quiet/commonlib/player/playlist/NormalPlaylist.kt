package tech.summerly.quiet.commonlib.player.playlist

import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.model.IMusic
import tech.summerly.quiet.commonlib.player.MusicPlayerManager
import tech.summerly.quiet.commonlib.player.MusicPlayerManager.playMode
import tech.summerly.quiet.commonlib.player.PlayMode
import tech.summerly.quiet.commonlib.player.PlayerType
import tech.summerly.quiet.commonlib.utils.log

/**
 * Created by summer on 18-3-4
 */

internal class NormalPlaylist(current: Music?,
                              playMode: PlayMode,
                              musicList: ArrayList<Music>
) : Playlist(current, playMode, musicList) {

    constructor() : this(null, PlayMode.Sequence, ArrayList())

    override val type: PlayerType = PlayerType.NORMAL

    private val shuffleMusicList = ArrayList<Music>()


    override suspend fun getNextMusic(music: Music?): Music? {
        if (musicList.isEmpty()) {
            log { "empty playlist!" }
            return null
        }
        if (current == null) {
            return musicList[0]
        }
        return when (MusicPlayerManager.player.playMode) {
            PlayMode.Single -> {
                current
            }
            PlayMode.Sequence -> {
                //if can not find ,index will be zero , it will right too
                val index = current?.let { musicList.indexOf(it) + 1 } ?: 1
                if (index == musicList.size) {
                    musicList[0]
                } else {
                    musicList[index]
                }
            }
            PlayMode.Shuffle -> {
                ensureShuffleListGenerate()
                val index = current?.let { shuffleMusicList.indexOf(it) } ?: -1
                when (index) {
                    -1 -> musicList[0]
                    musicList.size - 1 -> {
                        generateShuffleList()
                        shuffleMusicList[0]
                    }
                    else -> shuffleMusicList[index + 1]
                }
            }
        }
    }

    protected fun ensureShuffleListGenerate() {
        if (shuffleMusicList.size != musicList.size) {
            generateShuffleList()
        }
    }

    private fun generateShuffleList() {
        val list = ArrayList(musicList)
        var position = list.size - 1
        while (position > 0) {
            //生成一个随机数
            val random = (Math.random() * (position + 1)).toInt()
            //将random和position两个元素交换
            val temp = list[position]
            list[position] = list[random]
            list[random] = temp
            position--
        }
        shuffleMusicList.clear()
        shuffleMusicList.addAll(list)
    }

    override suspend fun getPreviousMusic(music: Music?): Music? {
        if (musicList.isEmpty()) {
            log { "try too play next with empty playlist!" }
            return null
        }
        if (current == null) {
            return musicList[0]
        }
        return when (MusicPlayerManager.player.playMode) {
            PlayMode.Single -> {
                current
            }
            PlayMode.Sequence -> {
                val index = current?.let { musicList.indexOf(it) } ?: -1
                when (index) {
                    -1 -> musicList[0]
                    0 -> musicList[musicList.size - 1]
                    else -> musicList[index - 1]
                }
            }
            PlayMode.Shuffle -> {
                ensureShuffleListGenerate()
                val index = current?.let { shuffleMusicList.indexOf(it) } ?: -1
                when (index) {
                    -1 -> musicList[0]
                    0 -> {
                        generateShuffleList()
                        shuffleMusicList[shuffleMusicList.size - 1]
                    }
                    else -> shuffleMusicList[index - 1]
                }
            }
        }
    }

    override fun insertToNext(music: IMusic) {
        music as Music
        if (musicList.isEmpty()) {
            musicList.add(music)
            return
        }
        //check if music is playing
        if (current == music) {
            return
        }
        //remove if musicList contain this item
        musicList.remove(music)

        val index = musicList.indexOf(current) + 1
        musicList.add(index, music)

        if (playMode == PlayMode.Shuffle) {
            val indexShuffle = shuffleMusicList.indexOf(current) + 1
            shuffleMusicList.add(indexShuffle, music)
        }
        onPlaylistChanged()
    }

}