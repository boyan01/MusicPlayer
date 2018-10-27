package tech.soit.quiet.repository.local

import android.os.Environment
import androidx.annotation.VisibleForTesting
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.experimental.CancellationException
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.isActive
import kotlinx.coroutines.experimental.launch
import tech.soit.quiet.model.vo.Music
import tech.soit.quiet.repository.db.dao.LocalMusicDao
import tech.soit.quiet.utils.MusicConverter
import tech.soit.quiet.utils.component.log
import tech.soit.quiet.utils.component.logError
import tech.soit.quiet.utils.component.support.Resource
import tech.soit.quiet.utils.component.support.Status
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

    val states = MutableLiveData<Status>()

    init {
        states.postValue(null)
    }

    val newMusic = MutableLiveData<Resource<Music>>()

    /**
     * start scan
     */
    fun scan() {
        if (states.value == Status.LOADING) {
            return
        }
        states.postValue(Status.LOADING)
        //do scan work
        GlobalScope.launch(
                onCompletion = {
                    if (it != null && it !is CancellationException) {
                        logError(it)
                        states.postValue(Status.ERROR)
                    } else {
                        states.postValue(Status.SUCCESS)
                    }
                }
        ) {
            disks.forEach {
                traversalDirectory(File(it))
            }
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
        files.forEach { child ->
            if (child.isDirectory) {
                traversalDirectory(child)
            } else if (child.isFile && child.exists()) {
                onFileEmitted(child)
            }
        }
        Unit
    }

    /** on a file has been traversed */
    private fun onFileEmitted(file: File) {
        log { "scanning file : ${file.path}" }
        if (!isFileAccept(file)) {
            return
        }
        val music = MusicConverter.scanFileToMusic(file)
        if (music != null) {
            localMusicDao.insertMusic(music)
            newMusic.postValue(Resource.success(music))
        }
    }

    private fun isFileAccept(file: File): Boolean {
        return file.path.endsWith(".mp3", true)
    }


}