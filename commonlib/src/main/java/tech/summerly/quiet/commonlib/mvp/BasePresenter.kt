package tech.summerly.quiet.commonlib.mvp

import org.jetbrains.anko.coroutines.experimental.Ref
import org.jetbrains.anko.coroutines.experimental.asReference

/**
 * Created by summer on 17-12-17
 */
abstract class BasePresenter<out VIEW : BaseView>(view: VIEW) {

    /**
     * to avoid leak
     */
    protected val viewRef: Ref<VIEW> = view.asReference()

}