package tech.soit.quiet.ui.drawable

import android.animation.ValueAnimator
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.core.animation.doOnEnd
import tech.soit.quiet.utils.component.log
import kotlin.math.atan
import kotlin.math.min

class ArcColorDrawable(@ColorInt color: Int) : Drawable() {

    companion object {
        private const val SIDE_PERCENTAGE = 0.618F
    }

    /**
     * bottom quadratic bezier controller
     */
    private val mAnchor = PointF()

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        mPaint.color = color
        mPaint.style = Paint.Style.FILL
    }

    private var isBeingDragging = false

    private var isAnimating = false

    private var totalPullDistance = 0f

    private var offsetX = 0f
    private var offsetY = 0f

    private var bottomSideOffset = 0f

    override fun draw(canvas: Canvas) {
        val path = Path()
        path.moveTo(bounds.left.toFloat(), bounds.top.toFloat())
        path.lineTo(bounds.right.toFloat(), bounds.top.toFloat())
        val bottomAnchor = bounds.bottom * SIDE_PERCENTAGE + bottomSideOffset
        path.lineTo(bounds.right.toFloat(), bottomAnchor)
        path.quadTo(mAnchor.x + offsetX, mAnchor.y + offsetY, bounds.left.toFloat(), bottomAnchor)
        path.close()

//        canvas.clipPath(path)

        canvas.drawPath(path, mPaint)

//        super.draw(canvas)
    }


    override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
        mAnchor.set((left + right) / 2f, bottom.toFloat())
        super.setBounds(left, top, right, bottom)
    }

    fun pull(distance: Float) {
        if (isAnimating) {
            return
        }
        isBeingDragging = true
        totalPullDistance += distance
        val k = 1 / atan(mAnchor.y + totalPullDistance)
        offsetY += distance * k
        invalidateSelf()
    }

    fun release() {
        isBeingDragging = false
        totalPullDistance = 0f
        val animator = ValueAnimator.ofFloat(offsetY, 0f)
        animator.addUpdateListener {
            offsetY = it.animatedValue as Float
            invalidateSelf()
        }
        animator.doOnEnd {
            isAnimating = false
        }
        animator.start()
        isAnimating = true
    }


    /**
     * offset range is [0,height]
     *
     *
     */
    fun absorb(offset: Float) {
        bottomSideOffset = min(bounds.bottom * (1 - SIDE_PERCENTAGE), offset)
        log { "bottomSideOffset $bottomSideOffset" }
        invalidateSelf()
    }

    override fun setAlpha(alpha: Int) {

    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSPARENT
    }

    override fun setColorFilter(colorFilter: ColorFilter) {

    }

}