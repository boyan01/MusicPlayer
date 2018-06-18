package tech.summerly.quiet.local.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.os.Environment
import kotlinx.coroutines.experimental.isActive
import kotlinx.coroutines.experimental.launch
import tech.summerly.quiet.local.repository.entity.MusicEntity
import tech.summerly.quiet.local.utils.MusicConverter
import tech.summerly.quiet.local.utils.getStoragePath
import java.io.File
import java.io.FileFilter
import kotlin.coroutines.experimental.coroutineContext

internal class MusicScannerViewModel : ViewModel() {

    companion object : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MusicScannerViewModel() as T
        }

        private val internalStorage: String = Environment.getExternalStorageDirectory().path
        private val externalStorage: String? = getStoragePath(true)
        private val disks = listOfNotNull(internalStorage, externalStorage)
    }


    val newMusic = MutableLiveData<MusicEntity>()

    val isScanning = MutableLiveData<Boolean>()

    fun startScan() {
        launch {
            if (isScanning.value == true) {
                return@launch
            }
            isScanning.postValue(true)
            disks.forEach {
                traversalDirectory(File(it))
            }
            isScanning.postValue(false)
        }
    }


    private suspend fun traversalDirectory(file: File): Unit = with(file) {
        if (!coroutineContext.isActive) {
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
                //todo check preference size
                convertFileToMusic(file)?.let {
                    newMusic.postValue(it)
                }
            }
        }
        Unit
    }

    private fun convertFileToMusic(file: File): MusicEntity? {
        val music = MusicConverter.scanFileToMusic(file) ?: return null
//        LocalMusicDatabase.instance.musicDao().insertMusic(music)
        return null
    }

    /**
     * 过滤非音频文件
     */
    private val fileFilter = FileFilter { pathname ->
        if (!pathname.isDirectory) {
            val name = pathname.name
            name.endsWith(".mp3", true)
                    || name.endsWith(".aac", true)
        } else {
            true
        }
    }


}