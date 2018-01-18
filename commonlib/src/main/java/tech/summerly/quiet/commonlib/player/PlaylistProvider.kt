package tech.summerly.quiet.commonlib.player

import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.bean.MusicType
import tech.summerly.quiet.commonlib.player.state.BasePlayerDataListener
import tech.summerly.quiet.commonlib.player.state.PlayMode
import tech.summerly.quiet.commonlib.player.state.PlayerStateKeeper
import tech.summerly.quiet.commonlib.utils.log


abstract class MusicPlaylistProviderFactory {
    companion object {
        private val factories = HashMap<MusicType, MusicPlaylistProviderFactory>()

        fun setFactory(type: MusicType, factory: MusicPlaylistProviderFactory) {
            factories.put(type, factory)
        }

        /**
         * get MusicPlaylistProvider by type
         * @param type null means undefined, so the truly type will depend on latest music'type.
         * @param playerStateListener to listen playlistProvider's internal change.
         */
        operator fun get(type: MusicType?, playerStateListener: BasePlayerDataListener): MusicPlaylistProvider {
            val dataKeeper = PlayerStateKeeper()
            val latest = dataKeeper.getCurrentMusic()
            val playlist = dataKeeper.getPlaylist()
            val playMode = dataKeeper.getPlayMode()
            return factories[type ?: latest?.type ?: MusicType.LOCAL]?.createMusicPlaylistProvider(latest, playMode, playlist, playerStateListener)
                    ?: throw IllegalStateException("haven't set factory for type:[$type] yet!")
        }
    }

    abstract fun createMusicPlaylistProvider(current: Music?,
                                             playMode: PlayMode,
                                             musicList: ArrayList<Music>,
                                             playerStateListener: BasePlayerDataListener): MusicPlaylistProvider

}

abstract class MusicPlaylistProvider(
        current: Music?,
        playMode: PlayMode,
        open val musicList: ArrayList<Music>,
        private val playerStateListener: BasePlayerDataListener
) : BasePlayerDataListener by playerStateListener {

    var current: Music? = null
        set(value) {
            onCurrentMusicUpdated(field, value)
            field = value
        }

    var playMode: PlayMode = playMode
        set(value) {
            onPlayModeUpdated(value)
            field = value
        }

    abstract fun setPlaylist(musics: List<Music>)

    abstract fun getPlaylist(): List<Music>

    abstract suspend fun getNextMusic(music: Music? = current): Music?

    abstract suspend fun getPreviousMusic(music: Music? = current): Music?

    abstract fun clear()

    abstract fun isTypeAccept(type: MusicType): Boolean

    abstract fun insertToNext(music: Music)

    init {
        this.current = current
        this.playMode = playMode
    }
}

class SimplePlaylistProvider(current: Music?,
                             playMode: PlayMode,
                             musicList: ArrayList<Music>,
                             playerStateListener: BasePlayerDataListener
) : MusicPlaylistProvider(current, playMode, musicList, playerStateListener) {

    private val shuffleMusicList = ArrayList<Music>()

    override fun setPlaylist(musics: List<Music>) {
        musicList.clear()
        musicList.addAll(musics)
        onPlaylistUpdated(musics)
    }

    override fun getPlaylist(): List<Music> = musicList

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
        onPlaylistUpdated(musicList)
    }

    override fun clear() {
        musicList.clear()
        current = null
        onPlaylistUpdated(musicList)
    }

    override fun isTypeAccept(type: MusicType): Boolean {
        return type == MusicType.LOCAL || type == MusicType.NETEASE
    }

}