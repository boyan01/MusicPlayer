package tech.soit.quiet.repository.local

import android.os.Environment
import androidx.annotation.VisibleForTesting
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.experimental.isActive
import kotlinx.coroutines.experimental.launch
import tech.soit.quiet.model.vo.Music
import tech.soit.quiet.repository.db.dao.LocalMusicDao
import tech.soit.quiet.utils.MusicConverter
import tech.soit.quiet.utils.component.support.Resource
import tech.soit.quiet.utils.getStoragePath
import java.io.File
import kotlin.coroutines.experimental.coroutineContext

/**
 * retrieval local music to database
 *
 * @param localMusicDao local music db
 */
class LocalMusicEngine(private val localMusicDao: LocalMusicDao) {

    companion object {



        /** internal and external storage directory */
        private val disks
            get() = listOfNotNull(
                    Environment.getExternalStorageDirectory().path,
                    getStoragePath(true))

    }

    private var isEngineRunning = false

    val newMusic = MutableLiveData<Resource<Music>>()

    /**
     * start scan
     */
    fun scan() {
        if (isEngineRunning) {
            return
        }
        isEngineRunning = true

        //do scan work

        launch {
            disks.forEach {
                traversalDirectory(File(it))
            }
            newMusic.postValue(Resource.success(null))
            isEngineRunning = false
        }
        return
    }


    @WorkerThread
    @VisibleForTesting
    private suspend fun traversalDirectory(file: File): Unit = with(file) {
        if (!coroutineContext.isActive) {
            /*to check if canceled*/
            return@with
        }
        if (!exists() || !isDirectory) {
            return@with
        }
        if (path.startsWith(".") || name == "Android") {
            //TODO 文件过滤配置化
            return
        }
        val files = listFiles() ?: return@with
        files.forEach {
            if (it.isDirectory) {
                traversalDirectory(it)
            } else if (it.isFile && it.exists()) {
                onFileEmitted(file)
            }
        }
        Unit
    }

    /** on a file has been traversed */
    private fun onFileEmitted(file: File) {
        if (!isFileAccept(file)) {
            return
        }
        val music = MusicConverter.scanFileToMusic(file)
        if (music != null) {
            localMusicDao.insertMusic(music)
            newMusic.postValue(Resource.loading(music.toMusic()))
        }
    }

    private fun isFileAccept(file: File): Boolean {
        return file.path.endsWith(".mp3", true)
    }


}