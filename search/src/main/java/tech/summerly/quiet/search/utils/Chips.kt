package tech.summerly.quiet.search.utils

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import kotlin.math.max


/**
 * @author summerly , yangbinyhbn@gmail.com
 *
 * 一个简单的布局,自动让子view向右填充
 *
 * 如果超过布局宽度,则自动新增一行
 *
 */
class ChipsLayout(
        context: Context,
        attr: AttributeSet?,
        defStyleAttr: Int
) : ViewGroup(context, attr, defStyleAttr) {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attr: AttributeSet?) : this(context, attr, 0)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        //首先测量子view的宽高
        measureChildren(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))

        //直接使用最大的宽度
        val width = MeasureSpec.getSize(widthMeasureSpec)

        //获得测量的最大高度
        val height = calculate(width)

        if (layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(width, height)
        } else {
            setMeasuredDimension(width, max(height, MeasureSpec.getSize(heightMeasureSpec)))
        }
    }

    private fun calculate(
            maxWidth: Int,
            onLayoutChildPosition: ((child: View, left: Int, top: Int, width: Int, height: Int) -> Unit)? = null
    ): Int {

        //总高度
        var totalHeight = 0

        //当前行的临时变量
        var lineWidth = 0
        var lineHeight = 0


        var index = 0
        while (index in 0..childCount) {
            val child = getChildAt(index)
            index++

            if (child == null || child.visibility == View.GONE) {
                continue
            }

            val lp = child.layoutParams as LayoutParams

            val childHeight = child.measuredHeight + lp.topMargin + lp.bottomMargin
            val childWidth = child.measuredWidth + lp.marginStart + lp.marginEnd

            lineWidth += childWidth

            if (lineWidth > maxWidth) {
                if (lineWidth == childWidth) {
                    //当前child的宽度已经超过了maxWidth,做特殊处理
                    onLayoutChildPosition?.invoke(child, lp.leftMargin, totalHeight + lp.topMargin, maxWidth - lp.leftMargin - lp.rightMargin
                            , childHeight - lp.topMargin - lp.bottomMargin)
                } else {
                    index--
                }
                //新增一行
                totalHeight += if (lineHeight == 0) childHeight else lineHeight
                lineHeight = 0
                lineWidth = 0
            } else {
                lineHeight = max(childHeight, lineHeight)
                //正常情况
                onLayoutChildPosition?.invoke(child, lineWidth - childWidth + lp.leftMargin, totalHeight + lp.topMargin,
                        childWidth - lp.leftMargin - lp.rightMargin, childHeight - lp.topMargin - lp.bottomMargin)
            }
        }
        if (lineHeight != 0) {
            totalHeight += lineHeight
        }
        //返回计算出所需要的总高度.
        return totalHeight
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val maxWidth = r - l

        val paddingTop = paddingTop
        val paddingStart = paddingStart
        val paddingEnd = paddingEnd
        calculate(maxWidth - paddingStart - paddingEnd) { child, left, top, width, height ->
            child.layout(left + paddingStart, top + paddingTop, left + width + paddingStart, top + height + paddingTop)
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet?): ViewGroup.LayoutParams {
        return LayoutParams(context, attrs)
    }

    override fun generateDefaultLayoutParams(): ViewGroup.LayoutParams {
        return LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    override fun generateLayoutParams(p: ViewGroup.LayoutParams?): ViewGroup.LayoutParams {
        if (p is MarginLayoutParams) {
            return LayoutParams(p)
        }
        return LayoutParams(p)
    }


    override fun checkLayoutParams(p: ViewGroup.LayoutParams?): Boolean {
        return p is LayoutParams
    }


    class LayoutParams : ViewGroup.MarginLayoutParams {

        constructor(c: Context?, attrs: AttributeSet?) : super(c, attrs)
        constructor(width: Int, height: Int) : super(width, height)
        constructor(source: MarginLayoutParams?) : super(source)
        constructor(source: ViewGroup.LayoutParams?) : super(source)
    }

}


