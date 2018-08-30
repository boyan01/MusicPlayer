package tech.soit.quiet.viewmodel

import androidx.annotation.IntDef
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tech.soit.quiet.model.vo.Music
import tech.soit.quiet.repository.local.LocalMusicEngine
import tech.soit.quiet.utils.component.support.Status
import tech.soit.quiet.utils.testing.OpenForTesting

/**
 * @author : summer
 * @date : 18-8-29
 */
@OpenForTesting
class LocalScannerViewModel(
        private val localMusicEngine: LocalMusicEngine
) : ViewModel() {

    companion object {

        const val STATUS_SCANNING = 1

        const val STATUS_SUCCESS = 2

        const val STATUS_IDLE = 0


    }


    @IntDef(value = [STATUS_IDLE, STATUS_SUCCESS, STATUS_SCANNING])
    @Retention(AnnotationRetention.SOURCE)
    @Target(AnnotationTarget.TYPE)
    annotation class ScannerStatus

    private val result = ArrayList<Music>()

    private var isCompleted = false

    init {
        localMusicEngine.newMusic.observeForever {
            it ?: return@observeForever
            if (it.status == Status.LOADING) {
                if (isCompleted) {
                    isCompleted = false
                    result.clear()
                } else {
                    result.add(it.requireData())
                }

            } else {
                isCompleted = true
            }
            resultCount = result.size
        }
    }

    var resultCount = 0

    val newAdded get() = localMusicEngine.newMusic

    val status = MutableLiveData<@ScannerStatus Int>()

    fun startScan() {
        localMusicEngine.scan()
    }

    fun stopScan() {

    }


}