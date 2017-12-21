package tech.summerly.quiet.commonlib.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Build
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.DecelerateInterpolator
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * Created by summer on 17-12-17
 */
fun buildCircularAnimation(
        view: View,
        startX: Int = 0,
        startY: Int = 0,
        show: Boolean = true,
        duration: Long = 500): Animator {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        return ObjectAnimator()
    }
    //这一段算法只是简单的计算 startX 和 startY 到 view这个矩形最远的距离
    //没有做精细计算，选择了有误差的方案的原因是这样简便，不需要进行大量比较和计算
    val centerX = view.width / 2f
    val centerY = view.height / 2f
    val minRadius = sqrt(centerX * centerX + centerY * centerY)
    val offsetX = abs(centerX - startX)
    val offsetY = abs(centerY - startY)

    val radius = minRadius + sqrt(offsetX * offsetX + offsetY * offsetY)

    val startRadius: Float
    val endRadius: Float
    if (show) {
        startRadius = 0f
        endRadius = radius
    } else {
        startRadius = radius
        endRadius = 0f
    }
    val animator = ViewAnimationUtils.createCircularReveal(view, startX, startY, startRadius, endRadius)
    animator.duration = duration
    animator.interpolator = DecelerateInterpolator()
    view.visibility = View.VISIBLE
    animator.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            view.visibility = if (show) View.VISIBLE else View.GONE
        }
    })
    return animator
}

//计算triggerView中心点相对于showView的位置
private fun computeCircularCenter(triggerView: View, showView: View): IntArray {
    val triggerPoint = intArrayOf(0, 0)

    //获取中心点相对于整个屏幕的坐标
    triggerView.getLocationOnScreen(triggerPoint)
    triggerPoint[0] += triggerView.width / 2
    triggerPoint[1] += triggerView.height / 2

    //转换为相对于showView的坐标
    val relativePoint = intArrayOf(0, 0)
    showView.getLocationOnScreen(relativePoint)
    triggerPoint[0] -= relativePoint[0]
    triggerPoint[1] -= relativePoint[1]

    return triggerPoint
}

fun showWithCircularAnimation(triggerView: View,
                              showView: View,
                              callback: (() -> Unit)? = null): Animator {
    val (x, y) = computeCircularCenter(triggerView, showView)
    val animator = buildCircularAnimation(showView, x, y, show = true)
    animator.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            callback?.invoke()
        }
    })
    animator.start()
    return animator
}

fun hideWithCircularAnimation(triggerView: View,
                              showView: View,
                              callback: (() -> Unit)? = null): Animator {
    val (x, y) = computeCircularCenter(triggerView, showView)
    val animator = buildCircularAnimation(showView, x, y, show = false)
    animator.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            callback?.invoke()
        }
    })
    animator.start()
    return animator
}
