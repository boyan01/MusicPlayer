package tech.summerly.quiet.commonlib.mvp

import kotlinx.coroutines.experimental.CoroutineDispatcher

/**
 * Created by summer on 17-12-17
 */
interface BaseView {

    /**
     * provider UI co-routine Dispatcher
     * for presenter to change co-routine to UI thread
     */
    @Suppress("PropertyName")
    val UI: CoroutineDispatcher
        get() = kotlinx.coroutines.experimental.android.UI

}
