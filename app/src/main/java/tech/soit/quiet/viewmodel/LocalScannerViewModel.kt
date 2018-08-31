package tech.soit.quiet.viewmodel

import androidx.annotation.IntDef
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import tech.soit.quiet.model.vo.Music
import tech.soit.quiet.repository.local.LocalMusicEngine
import tech.soit.quiet.utils.component.support.Status
import tech.soit.quiet.utils.component.support.map
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

        /**
         * state scanning processing
         */
        const val STATUS_SCANNING = 1

        /**
         * state scan complete
         */
        const val STATUS_SUCCESS = 2

        /**
         * idle status
         */
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

    /**
     * status [STATUS_IDLE] [STATUS_SCANNING] [STATUS_SUCCESS]
     */
    val status: LiveData<Int> = localMusicEngine.states.map { status ->
        status ?: return@map STATUS_IDLE
        if (status == Status.LOADING) {
            return@map STATUS_SCANNING
        } else {
            return@map STATUS_SUCCESS
        }
    }

    fun startScan() {
        localMusicEngine.scan()
    }

    fun stopScan() {

    }


}