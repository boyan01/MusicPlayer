package tech.summerly.quiet.commonlib.player.playlist

import tech.summerly.quiet.commonlib.player.MusicPlayerManager
import tech.summerly.quiet.commonlib.player.PlayMode
import tech.summerly.quiet.commonlib.player.PlayerType
import tech.summerly.quiet.commonlib.utils.log

internal class NormalPlaylist2<T>(
        token: String,
        musicList: ArrayList<T>
) : Playlist2<T>(token, musicList) {

    override val type: PlayerType = PlayerType.NORMAL

    private val shuffleMusicList = ArrayList<T>()


    override suspend fun getNext(anchor: T?): T? {
        if (mList.isEmpty()) {
            log { "empty playlist!" }
            return null
        }
        if (anchor == null) {
            return mList[0]
        }
        return when (MusicPlayerManager.player.playMode) {
            PlayMode.Single -> {
                anchor
            }
            PlayMode.Sequence -> {
                //if can not find ,index will be zero , it will right too
                val index = mList.indexOf(anchor) + 1
                if (index == mList.size) {
                    mList[0]
                } else {
                    mList[index]
                }
            }
            PlayMode.Shuffle -> {
                ensureShuffleListGenerate()
                val index = shuffleMusicList.indexOf(anchor)
                when (index) {
                    -1 -> mList[0]
                    mList.size - 1 -> {
                        generateShuffleList()
                        shuffleMusicList[0]
                    }
                    else -> shuffleMusicList[index + 1]
                }
            }
        }
    }


    private fun ensureShuffleListGenerate() {
        if (shuffleMusicList.size != mList.size) {
            generateShuffleList()
        }
    }

    private fun generateShuffleList() {
        val list = ArrayList(mList)
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


    override suspend fun getPrevious(anchor: T?): T? {
        if (mList.isEmpty()) {
            log { "try too play next with empty playlist!" }
            return null
        }
        if (anchor == null) {
            return mList[0]
        }
        return when (MusicPlayerManager.player.playMode) {
            PlayMode.Single -> {
                anchor
            }
            PlayMode.Sequence -> {
                val index = mList.indexOf(anchor)
                when (index) {
                    -1 -> mList[0]
                    0 -> mList[mList.size - 1]
                    else -> mList[index - 1]
                }
            }
            PlayMode.Shuffle -> {
                ensureShuffleListGenerate()
                val index = shuffleMusicList.indexOf(anchor)
                when (index) {
                    -1 -> mList[0]
                    0 -> {
                        generateShuffleList()
                        shuffleMusicList[shuffleMusicList.size - 1]
                    }
                    else -> shuffleMusicList[index - 1]
                }
            }
        }
    }


    override fun insertToNext(next: T) {
        if (mList.isEmpty()) {
            mList.add(next)
            return
        }
        //check if music is playing
        if (current == next) {
            return
        }
        //remove if musicList contain this item
        mList.remove(next)

        val index = mList.indexOf(current) + 1
        mList.add(index, next)

        if (MusicPlayerManager.playMode == PlayMode.Shuffle) {
            val indexShuffle = shuffleMusicList.indexOf(current) + 1
            shuffleMusicList.add(indexShuffle, next)
        }
        onPlaylistChanged()
    }
}