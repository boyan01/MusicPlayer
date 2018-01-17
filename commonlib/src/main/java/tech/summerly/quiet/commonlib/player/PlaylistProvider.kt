package tech.summerly.quiet.commonlib.player

import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.bean.MusicType
import tech.summerly.quiet.commonlib.player.state.PlayMode
import tech.summerly.quiet.commonlib.utils.log


abstract class MusicPlaylistProviderFactory {
    companion object {
        private val factories = HashMap<MusicType, MusicPlaylistProviderFactory>()

        fun setFactory(type: MusicType, factory: MusicPlaylistProviderFactory) {
            factories.put(type, factory)
        }

        operator fun get(type: MusicType): MusicPlaylistProvider {
            return factories[type]?.createMusicPlaylistProvider()
                    ?: throw IllegalStateException("haven't set factory for type:[$type] yet!")
        }

    }

    abstract fun createMusicPlaylistProvider(): MusicPlaylistProvider

}

interface MusicPlaylistProvider {

    val current: Music?

    var playMode: PlayMode

    fun setPlaylist(musics: List<Music>)

    fun getPlaylist(): List<Music>

    suspend fun getNextMusic(music: Music? = current): Music?

    suspend fun getPreviousMusic(music: Music? = current): Music?

    fun clear()

    fun isTypeAccept(type: MusicType): Boolean

    fun insertToNext(music: Music)
}

class SimplePlaylistProvider : MusicPlaylistProvider {

    override var playMode: PlayMode = PlayMode.Sequence

    override var current: Music? = null

    private val musicList = ArrayList<Music>()

    private val shuffleMusicList = ArrayList<Music>()

    override fun setPlaylist(musics: List<Music>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPlaylist(): List<Music> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    suspend override fun getNextMusic(music: Music?): Music? {
        if (musicList.isEmpty()) {
            log { "empty playlist!" }
            return null
        }
        if (current == null) {
            return musicList[0]
        }
        return when (playMode) {
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

    suspend override fun getPreviousMusic(music: Music?): Music? {
        if (musicList.isEmpty()) {
            log { "try too play next with empty playlist!" }
            return null
        }
        if (current == null) {
            return musicList[0]
        }
        return when (playMode) {
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

    override fun insertToNext(music: Music) {
        if (musicList.isEmpty()) {
            musicList.add(music)
            return
        }
        //check if music is playing
        if (MusicPlayerManager.playingMusic.value == music) {
            return
        }
        //remove if musicList contain this item
        musicList.remove(music)

        val index = musicList.indexOf(MusicPlayerManager.playingMusic.value) + 1
        musicList.add(index, music)

        if (playMode == PlayMode.Shuffle) {
            val indexShuffle = shuffleMusicList.indexOf(MusicPlayerManager.playingMusic.value) + 1
            shuffleMusicList.add(indexShuffle, music)
        }
    }

    override fun clear() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isTypeAccept(type: MusicType): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}