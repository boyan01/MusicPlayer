package tech.summerly.quiet.commonlib.utils

import android.view.View
import android.view.ViewGroup

/**
 * Created by summer on 18-1-6
 */
class Varity {

    fun of(container: ViewGroup, from: View, to: View) {

    }

}


internal class ViewChanges {

    private var translateX: Change<Int>? = null
    private var translateY: Change<Int>? = null
    private var background: Change<Int>? = null
    private var width: Change<Int>? = null
    private var height: Change<Int>? = null
    private var rotation: Change<Int>? = null

    fun dispatchTo(view: View, friction: Float) {
        val currentX = translateX?.current(friction)
        val currentY = translateY?.current(friction)
        val currentBackground = background?.current(friction)
        val currentWidth = width?.current(friction)
        val currentHeight = height?.current(friction)
        val rotation = rotation?.current(friction)
    }

    private fun Change<Int>.current(friction: Float): Int = ((end - start) * friction).toInt()
}

private class Change<out T : Any>(
        val start: T,
        val end: T
)