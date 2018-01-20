package tech.summerly.quiet.local.scanner

import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.yield
import org.jetbrains.anko.coroutines.experimental.asReference
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.local.LocalModule
import tech.summerly.quiet.local.LocalMusicApi
import tech.summerly.quiet.local.scanner.persistence.ScannerSetting
import tech.summerly.quiet.local.utils.LocalMusicUrlGetter
import tech.summerly.quiet.local.utils.MusicConverter
import java.io.File
import java.io.FileFilter

/**
 * author : summerly
 * e-mail : yangbinyhbn@gmail.com
 * time   : 2017/7/23
 * desc   : 一个可测试的本地音乐扫描Presenter! (花了好几天才将kotlin协程梳理清晰,哇,学的太慢,真的很难受.共勉)
 *
 */
internal class LocalMusicScannerPresenter(
        override val view: LocalMusicScannerContract.View,
        //解析音乐文件的接口
        private val actionConvertFile: (File) -> Music? = MusicConverter::scanFileToMusic,
        //保存音乐文件的操作
        private val actionSaveMusic: (Music) -> Unit = LocalMusicApi.getLocalMusicApi()::insertMusic,
        /**
         * 获取音乐配置
         */
        private val preference: ScannerSetting = ScannerSetting(LocalModule)
) : LocalMusicScannerContract.Presenter {

    private val viewRef = view.asReference()

    private val filters = preference.getAllFilterFolder()


    override fun scan(path: String) = launch {
        //在开始扫描之前, 需要移除处于待过虑名单中的音乐
        val localMusicApi = LocalMusicApi.getLocalMusicApi()
        localMusicApi.getTotalMusics().forEach {
            val musicPath = LocalMusicUrlGetter.getPlayableUrl(it)
            if (musicPath == null) {
                localMusicApi.deleteMusic(it)
                return@launch
            }
            val file = File(musicPath)
            if (!file.exists() || filters.contains(file.parent)) {
                localMusicApi.deleteMusic(it)
            }
        }
        //
        //真正的开始扫描
        traversalDirectory(File(path))
    }

    /**
     * 过滤非音频文件
     */
    private val audioFileFilter = FileFilter { pathname ->
        if (!pathname.isDirectory) {
            val name = pathname.name
            name.endsWith(".mp3", true)
                    || name.endsWith(".aac", true)
        } else {
            true
        }
    }

    /**
     * 遍历[file]目录及其子目录
     */
    private suspend fun traversalDirectory(
            file: File,
            fileFilter: FileFilter = audioFileFilter
    ): Unit = with(file) {
        yield()
        if (!exists() || !isDirectory || filters.contains(path)) {
            return
        }
        if (path.startsWith(".") || name == "Android") {
            return
        }
        val files = listFiles(fileFilter) ?: return
        files.forEach {
            if (it.isDirectory) {
                traversalDirectory(it, fileFilter)
            } else if (it.isFile && it.exists()) {
                if (preference[ScannerSetting.KEY_FILTER_SIZE] && it.length() < ScannerSetting.SIZE_MAX) {
                    return@forEach
                }
                viewRef().onMusicScan(it.parent, it.name)
                if (musicFileProcessor(it)) {
                    preference.put(it.parent, true)
                }
            }
        }
    }


    private suspend fun musicFileProcessor(file: File): Boolean {
        yield()
        val music = actionConvertFile(file) ?: return false
        if (!isValidMusic(music)) {
            return false
        }
        actionSaveMusic(music)
        return true
    }

    /**
     * @return true:accept
     */
    private fun isValidMusic(music: Music): Boolean {
        if (preference.isFilterByDuration() && music.duration < preference.getLimitDuration()) {
            return false
        }
        return true
    }
}

