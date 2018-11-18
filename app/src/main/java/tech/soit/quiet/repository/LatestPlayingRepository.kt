package tech.soit.quiet.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tech.soit.quiet.model.vo.Music
import tech.soit.quiet.utils.component.log
import tech.soit.quiet.utils.component.persistence.KeyValue
import tech.soit.quiet.utils.component.persistence.KeyValuePersistence
import tech.soit.quiet.utils.component.persistence.get
import java.util.*
import kotlin.collections.ArrayList

/**
 * 最近播放列表 Repository
 */
class LatestPlayingRepository(
        private val keyValue: KeyValuePersistence
) {

    companion object {

        private val instance = LatestPlayingRepository(KeyValue)

        fun getInstance(): LatestPlayingRepository {
            return instance
        }

        private const val KEY_PLAYING = "LatestPlayingRepository_lasted_playing"

    }

    val musics = LinkedList<Music>()

    private val liveMusics = MutableLiveData<List<Music>>()

    /**
     * on new music has been played
     */
    fun hit(music: Music) {
        musics.removeAll { it == music }

        if (musics.size >= 100) {//确保最多只能有100首音乐
            musics.removeLast()
        }
        musics.add(0, music)
        liveMusics.postValue(getMusicList())

        persistence()
    }

    private fun persistence() {
        GlobalScope.launch {
            val str = KeyValue.objectToString(musics)
            keyValue.put(KEY_PLAYING, str)
        }
    }

    /**
     * get latest play music list
     */
    fun getLatestPlayMusic(): LiveData<List<Music>> {
        return liveMusics
    }

    private fun getMusicList(): List<Music> {
        return ArrayList(musics)
    }

    init {
        GlobalScope.launch {
            try {
                val m = KeyValue.objectFromString<LinkedList<Music>>(keyValue.get(KEY_PLAYING))
                if (m != null) {
                    musics.addAll(m)
                }
                liveMusics.postValue(getMusicList())
            } catch (e: Exception) {
                log { e.printStackTrace();"读取 $keyValue 失败" }
            }
        }
    }

}