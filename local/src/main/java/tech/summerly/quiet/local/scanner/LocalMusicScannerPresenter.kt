package tech.summerly.quiet.local.scanner

import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.newSingleThreadContext
import org.jetbrains.anko.coroutines.experimental.asReference
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.local.scanner.persistence.ILocalMusicScannerSetting
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
class LocalMusicScannerPresenter(
        override val view: LocalMusicScannerContract.View,
        //需要扫描的根目录
        private val paths: Array<String>,
        //解析音乐文件的接口
        private val actionConvertFile: (File) -> Music? = MusicConverter::scanFileToMusic,
        //保存音乐文件的操作
        private val actionSaveMusic: (Music) -> Long = TODO(),
        /**
         * 获取音乐配置
         */
        private val preference: ILocalMusicScannerSetting = TODO()
) : LocalMusicScannerContract.Presenter {

    private val viewRef = view.asReference()

    private var scannerJob: Job? = null

    override fun scanMusics() {
        scannerJob?.let {
            stopScanMusics()
        }
        view.onScannerStart()
        //从指定根目录开始扫描音乐
        scannerJob = launch(newSingleThreadContext("scanner")) {
            paths.forEach { path ->
                File(path).traversalDirectory {
                    musicFileProcessor(it)
                }
            }
            launch(viewRef().UI) {
                viewRef().onScannerComplete()
            }
        }
    }

    override fun stopScanMusics() {
        val scannerJob = scannerJob ?: return
        if (scannerJob.isCompleted) {
            return
        }
        if (scannerJob.cancel()) {
            view.onScannerComplete()
        }
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
     * 遍历当前目录及其子目录 , 对符合[fileFilter]的文件执行[action]
     */
    private suspend fun File.traversalDirectory(fileFilter: FileFilter = audioFileFilter, action: suspend (File) -> Unit) {
        if (!exists()
                || !isDirectory) {
            return
        }
        //fixme use preference save it
        if (path.startsWith(".") || name == "Android") {
            return
        }
        //过滤掉不扫描的路径
        if (preference.getAllFilterFolder().contains(path)) {
            return
        }
        val files = listFiles(fileFilter) ?: return
        files.forEach {
            if (it.isDirectory) {
                it.traversalDirectory(fileFilter, action)
            } else if (it.isFile && it.exists()) {
                action(it)
            }
        }
    }


    private suspend fun musicFileProcessor(file: File) {
        val music = actionConvertFile(file) ?: return
        if (!isValidMusic(music)) {
            return
        }
        actionSaveMusic(music)
        launch(viewRef().UI) {
            viewRef().onMusicScanned(music)
        }
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

