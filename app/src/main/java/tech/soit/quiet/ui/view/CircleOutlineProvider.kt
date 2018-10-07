package tech.soit.quiet.ui.view

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider

/**
 * set view to clip to Oval
 *
 * View will clip to Circle when it is Square
 */
class CircleOutlineProvider : ViewOutlineProvider() {

    override fun getOutline(view: View, outline: Outline) {
        outline.setOval(0, 0, view.width, view.height)
    }
}