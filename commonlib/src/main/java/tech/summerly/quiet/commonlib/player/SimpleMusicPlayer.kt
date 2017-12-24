package tech.summerly.quiet.commonlib.player

import android.content.Context
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.player.state.PlayMode
import tech.summerly.quiet.commonlib.utils.log

/**
 * Created by summer on 17-12-22
 */
class SimpleMusicPlayer(context: Context) : BaseMusicPlayer(context) {

    override val musicList = ArrayList<Music>()

    private val shuffleMusicList = ArrayList<Music>()

    override suspend fun getNextMusic(current: Music?): Music? {
        if (musicList.isEmpty()) {
            log { "empty playlist!" }
            return null
        }
        if (current == null) {
            return musicList[0]
        }
        return when (playMode.value) {
            PlayMode.Single -> {
                current
            }
            PlayMode.Sequence -> {
                //if can not find ,index will be zero , it will right too
                val index = musicList.indexOf(current) + 1
                if (index == musicList.size) {
                    musicList[0]
                } else {
                    musicList[index]
                }
            }
            PlayMode.Shuffle -> {
                ensureShuffleListGenerate()
                //if can not find ,index will be zero , it will right too
                val index = shuffleMusicList.indexOf(current)
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

    private fun ensureShuffleListGenerate() {
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

    override suspend fun getPreviousMusic(current: Music?): Music? {

        if (musicList.isEmpty()) {
            log { "try too play next with empty playlist!" }
            return null
        }
        if (current == null) {
            return musicList[0]
        }
        return when (playMode.value) {
            PlayMode.Single -> {
                current
            }
            PlayMode.Sequence -> {
                val index = musicList.indexOf(current)
                when (index) {
                    -1 -> musicList[0]
                    0 -> musicList[musicList.size - 1]
                    else -> musicList[index - 1]
                }
            }
            PlayMode.Shuffle -> {
                ensureShuffleListGenerate()
                val index = shuffleMusicList.indexOf(current)
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
}