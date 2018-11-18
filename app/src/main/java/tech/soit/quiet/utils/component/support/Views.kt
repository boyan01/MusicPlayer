package tech.soit.quiet.utils.component.support

import android.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * add suspend function support for clicked callback
 */
fun View.setOnClickListenerAsync(
        scope: CoroutineScope = context as? CoroutineScope ?: ContextScope(Dispatchers.Main),
        listener: suspend View.() -> Unit) {

    val l = CoroutineOnClickListener(scope, listener)
    setOnClickListener(l)
}


private class CoroutineOnClickListener(
        private val scope: CoroutineScope,
        private val suspendListener: suspend View.() -> Unit
) : View.OnClickListener {

    private var isClicked = false

    override fun onClick(v: View) {
        if (isClicked) {
            return
        }
        scope.launch {
            isClicked = true
            suspendListener(v)
            isClicked = false
        }
    }


}

private class ContextScope(override val coroutineContext: CoroutineContext) : CoroutineScope



