package tech.soit.quiet.ui.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Scroller;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.Nullable;
import tech.soit.quiet.BuildConfig;
import tech.soit.quiet.R;


/**
 * Created by Summerly on 2017/10/7.
 * Desc:
 */
@SuppressWarnings({"unused", "deprecation"})
public class LyricView extends View {

    private static final boolean DEBUG = BuildConfig.DEBUG;

    private static final String TAG = "LyricView";

    private static final int DEFAULT_LYRIC_ADJUST = 0;

    private static final String DEFAULT_CHARSET = "utf-8";

    /**
     * 歌词时间偏移 例:<hr/>-500 :提前500毫秒 <br>  500 :推后500毫秒
     */
    private int lyricTimeStampOffset = DEFAULT_LYRIC_ADJUST;

    /**
     * 歌词字体大小
     */
    private float lyricTextSize;

    /**
     * 默认状态下的歌词颜色
     */
    private int lyricNormalTextColor;

    /**
     * 处于播放状态下的歌词的颜色
     */
    private int lyricPlayingTextColor;

    private int lyricPlayIndicatorColor;


    /**
     * 行间距
     */
    private float lyricLineSpacing;

    /**
     * 当歌词为空时的显示
     */
    private String lyricEmptyText;
    /**
     * 歌词
     */
    private Lyric lyric;

    /**
     * 判断歌词是否可用的标记
     * 如果歌词不可用,将调用 {@link #buildLyricEntry}
     */
    private volatile boolean isLyricAvailable;

    //flag is scroll or fling animation
    private boolean isAnimating;

    private TextPaint textPaint;

    private Paint dashPaint;

    /**
     * 处于播放中的歌词
     */
    private int currentLine = 0;

    /**
     * 处于最中间的歌词
     */
    private int centerLine = 0;

    private StaticLayout emptyStaticLayout;

    //歌词滑动偏移 : 用于控制手势进行歌词滚动
    private float offsetScroll = 0;

    private boolean isScrolling = false;

    private Drawable playIndicator;

    private int sizePlayIndicator;

    private ValueAnimator scrollAnimator;

    /**
     * 用于监听手指的滑动来滚动歌词
     */
    private final GestureDetector gestureDetector;

    //快速滑动时,抬起手指依然进行滑动
    private Scroller scroller;

    private OnPlayIndicatorClickListener onPlayIndicatorClickListener;

    private OnClickListener onClickListener;

    public LyricView(Context context) {
        this(context, null);
    }

    public LyricView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LyricView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LyricView);
        if (typedArray != null) {
            lyricTextSize = typedArray.getDimensionPixelSize(R.styleable.LyricView_lyricTextSize, 16);
            lyricNormalTextColor = typedArray.getColor(R.styleable.LyricView_lyricNormalTextColor, Color.WHITE);
            lyricPlayingTextColor = typedArray.getColor(R.styleable.LyricView_lyricPlayingTextColor, Color.GRAY);
            lyricLineSpacing = typedArray.getDimensionPixelSize(R.styleable.LyricView_lyricLineSpacing, 0);
            lyricEmptyText = typedArray.getString(R.styleable.LyricView_lyricEmptyText);
            lyricPlayIndicatorColor = typedArray.getColor(R.styleable.LyricView_lyricPlayIndicatorColor, Color.DKGRAY);
            typedArray.recycle();
        }
        if (lyricEmptyText == null) {
            lyricEmptyText = "EMPTY";
        }
        sizePlayIndicator = dip2px(24);
        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(lyricTextSize);
        dashPaint = new Paint();
        //用于绘制虚线
        DashPathEffect dashPathEffect = new DashPathEffect(new float[]{19, 19, 19, 19}, 0);
        dashPaint.setPathEffect(dashPathEffect);
        dashPaint.setStyle(Paint.Style.STROKE);
        dashPaint.setAntiAlias(true);
        dashPaint.setColor(lyricPlayIndicatorColor);
        dashPath = new Path();
        dashPath.reset();
        isLyricAvailable = false;
        //noinspection deprecation
        playIndicator = getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp);
        playIndicator = new RippleDrawable(ColorStateList.valueOf(lyricPlayIndicatorColor), playIndicator, null);
        playIndicator.setColorFilter(lyricPlayIndicatorColor, PorterDuff.Mode.SRC_IN);
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onDown(MotionEvent event) {
                //触摸屏幕时立即停止还在自己滚动的歌词
                if (!scroller.isFinished()) {
                    scroller.forceFinished(true);
                }
                return true;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent event) {
                Log.d(TAG, "onSingleTapConfirmed: point = " + event.getX() + "," + event.getY());
                Log.d(TAG, "playIndicator.getBounds(): " + playIndicator.getBounds().toShortString());
                if (playIndicator.getBounds().contains(((int) event.getX()), (int) event.getY())) {
                    isScrolling = false;
                    if (onPlayIndicatorClickListener != null) {
                        onPlayIndicatorClickListener.onClick(lyric.getTimeStamp(centerLine));
                    }
                    playIndicator.setHotspot(event.getX(), event.getY());
                    invalidate();
                    return true;

                } else {
                    return performClick();
                }
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                //终止滚动状态
                isScrolling = false;
                animateScrollToCurrentLine(0, 300);
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (isAnimating) {//如果此时处于动画偏移过程中
                    scrollAnimator.cancel();
                }
                isScrolling = true;
                offsetScroll += -distanceY;
                int maxY = maxY();
                int minY = minY();
                if (offsetScroll > maxY) {
                    offsetScroll = maxY;
                } else if (offsetScroll < minY) {
                    offsetScroll = minY;
                }
                invalidate();
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                int minY = minY();
                int maxY = maxY();
                if (DEBUG) {
                    Log.d(TAG, "onFling: offsetScroll = " + offsetScroll + " velocityY = " + velocityY + " minY = " + minY + " maxY = " + maxY);
                }
                scroller.fling(
                        0,
                        (int) offsetScroll,//播放中歌词距中线偏移量
                        0,
                        (int) velocityY,//每秒的像素滚动速率
                        0, 0,
                        minY, maxY
                );
                computeScroll();
                return true;
            }

            private int minY() {
                //计算当前播放歌词到最后一条歌词的偏移
                //minY表示歌词能够向上滚动的最大距离,也是@offsetScroll偏移的最小取值范围
                //由于是向上滚动,所以需要取负值.
                int minY = -lyric.getLyricEntry(currentLine).staticLayout.getHeight() / 2;
                for (int i = currentLine + 1; i < lyric.size(); i++) {
                    minY -= lyric.getLyricEntry(i).staticLayout.getHeight() + lyricLineSpacing;
                }
                return minY;
            }

            private int maxY() {
                //计算当前歌词到第一条歌词的偏移
                //maxY表示歌词能够向下滚动的最大距离,也是@offsetScroll偏移的最大取值
                int maxY = lyric.getLyricEntry(currentLine).staticLayout.getHeight() / 2;
                for (int i = currentLine - 1; i >= 0; i--) {
                    maxY += lyric.getLyricEntry(i).staticLayout.getHeight() + lyricLineSpacing;
                }
                return maxY;
            }
        });
        scroller = new Scroller(context);
    }

    //滚动指示器右侧歌词时间文字的baseline,可以让文字居中显示
    private float baseline;
    //滚动指示器右侧歌词时间文字的宽度
    private float timeStampWidth;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        buildLyricEntry(true);
        final int centerY = getHeight() / 2;

        //初始化一些绘制用到的且需要依据宽高来计算的数据,放在onDraw方法中会影响性能
        baseline = centerY - (textPaint.getFontMetrics().bottom + textPaint.getFontMetrics().top) / 2;
        playIndicator.setBounds(getPaddingLeft(), centerY - sizePlayIndicator / 2,
                sizePlayIndicator + getPaddingLeft(), centerY + sizePlayIndicator / 2);
        timeStampWidth = textPaint.measureText("000:00");
        dashPath.reset();
        dashPath.moveTo(sizePlayIndicator + dip2px(8), centerY);
        dashPath.lineTo(getWidth() - timeStampWidth - dip2px(8), centerY);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (lyric == null) {
            return false;
        }
        super.onTouchEvent(event);
        boolean b = gestureDetector.onTouchEvent(event);
        //FIXME 不知道怎么操作才能不消耗点击事件且可以消耗掉滑动事件.
        //使用 return gestureDetector.onTouchEvent(event)没有用.
        return true;
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        this.onClickListener = l;
    }

    @Override
    public boolean performClick() {
        super.performClick();
        if (!isScrolling && onClickListener != null) {
            onClickListener.onClick(this);
            return true;
        }
        return false;
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            offsetScroll = scroller.getCurrY();
            postInvalidate();
        }
    }


    private void buildLyricEntrySafely() {
        if (getWidth() == 0) {
            getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    buildLyricEntry(true);
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        }
    }

    private synchronized void buildLyricEntry(final boolean force) {
        if (lyric == null) {
            return;
        }
        if (!force && isLyricAvailable()) {
            return;
        }
        isLyricAvailable = true;
        int width = getWidth();
        if (DEBUG) {
            Log.i(TAG, "buildLyricEntry: width = " + width + " lyricLineSpacing = " + lyricLineSpacing);
        }
        emptyStaticLayout = new StaticLayout(lyricEmptyText, textPaint, width,
                Layout.Alignment.ALIGN_CENTER, 1f, lyricLineSpacing, true);
        lyric.buildLyricEntryLayout(textPaint, width, lyricLineSpacing);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        textPaint.setColor(lyricNormalTextColor);
        textPaint.setTextSize(lyricTextSize);
        if (lyric == null) {
            final StaticLayout emptyStaticLayout = getEmptyStaticLayout();
            drawStaticLayout(canvas, emptyStaticLayout, (getHeight() - emptyStaticLayout.getHeight()) / 2);
            return;
        }
        if (!isLyricAvailable()) {
            buildLyricEntry(false);
            Log.i(TAG, "onDraw: lyric is not available");
            return;
        }
        //屏幕最中央的Y坐标
        final int centerY = getHeight() / 2;

        //首先绘制处于播放中的歌词
        textPaint.setColor(lyricPlayingTextColor);
        //处于播放状态中歌词中间点的Y轴坐标
        final int currentLineCenterY = getHeight() / 2 + (int) offsetScroll;
        final StaticLayout current = lyric.getLyricEntry(currentLine).staticLayout;
        final int currentLineHeight = current.getHeight();
        drawStaticLayout(canvas, current, currentLineCenterY - currentLineHeight / 2);

        if (isScrolling &&//如果不处于滚动状态,那么我们也不需要去计算处于最中间的哪一行的位置
                centerY > currentLineCenterY - currentLineHeight - lyricLineSpacing / 2
                && centerY < currentLineCenterY + currentLineHeight / 2 + lyricLineSpacing / 2) {
            centerLine = currentLine;
        }
        textPaint.setColor(lyricNormalTextColor);
        //绘制前面的歌词
        int dy = currentLineCenterY - currentLineHeight / 2;
        for (int line = currentLine - 1; line >= 0; line--) {
            final StaticLayout former = lyric.getLyricEntry(line).staticLayout;
            dy -= former.getHeight() + lyricLineSpacing;
            drawStaticLayout(canvas, former, dy);
            if (isScrolling &&
                    centerY > dy - lyricLineSpacing / 2 &&
                    centerY < dy + former.getHeight() + lyricLineSpacing / 2) {
                centerLine = line;
            }
            if (dy < getPaddingTop()) { //如果垂直偏移量超过了上界,则停止先前的歌词的绘制
                break;
            }
        }
        //绘制后面的歌词
        dy = currentLineCenterY + currentLineHeight / 2 + (int) lyricLineSpacing;
        for (int line = currentLine + 1; line < lyric.size(); line++) {
            final StaticLayout next = lyric.getLyricEntry(line).staticLayout;
            drawStaticLayout(canvas, next, dy);
            if (isScrolling &&
                    centerY > dy - lyricLineSpacing / 2 &&
                    centerY < dy + next.getHeight() + lyricLineSpacing / 2) {
                centerLine = line;
            }
            dy += next.getHeight() + lyricLineSpacing;
            if (dy > getHeight() - getPaddingBottom()) { //如果垂直偏移量超过了下界,则停止后面的歌词的绘制
                break;
            }
        }

        drawPlayIndicator(canvas, textPaint);
    }

    private Path dashPath;

    /**
     * 绘制滑动时出现在屏幕上的播放指示器
     * 点击后可以跳转到指定的歌词时间点
     */
    private void drawPlayIndicator(Canvas canvas, TextPaint textPaint) {
        if (!isScrolling) {
            return;
        }
        if (DEBUG) {
            Log.i(TAG, "onDraw: centerLine = " + centerLine);
        }
        textPaint.setColor(lyricPlayIndicatorColor);
        textPaint.setTextSize(dip2px(13));
        //绘制右侧的时间指示
        final String timeStamp = lyric.getLyricEntry(centerLine).timeStamp;
        canvas.drawText(timeStamp, getWidth() - timeStampWidth - getPaddingRight(), baseline, textPaint);

        //绘制左侧的播放按钮
        playIndicator.draw(canvas);

        //绘制一条中间水平的虚线
        canvas.drawPath(dashPath, dashPaint);
    }


    private StaticLayout getEmptyStaticLayout() {
        if (emptyStaticLayout == null) {
            emptyStaticLayout = new StaticLayout(lyricEmptyText, textPaint, getWidth(),
                    Layout.Alignment.ALIGN_CENTER, 1, 0, false);
        }
        return emptyStaticLayout;
    }

    private boolean isLyricAvailable() {
        return isLyricAvailable;
    }

    private void drawStaticLayout(Canvas canvas, StaticLayout staticLayout, int dy) {
        //如果@staticLayout不在屏幕内,显然不需要对其进行绘制
        if (dy > getHeight() || dy < 0 - staticLayout.getHeight()) {
            return;
        }
        canvas.save();
        canvas.translate(0, dy);
        staticLayout.draw(canvas);
        canvas.restore();
    }


    /**
     * 滚动歌词到指定的时间点
     *
     * @param millisecond 时间点(毫秒)
     */
    public void scrollLyricTo(int millisecond) {
        if (lyric == null) {//如果没有歌词文件，则没必要进行滚动了
            return;
        }
        if (isScrolling) {
            if (DEBUG) {
                Log.d(TAG, "isScrolling : do not need scroll");
            }
            return;
        }
        final int nextLine = lyric.findIndexByTimeStamp(millisecond + lyricTimeStampOffset, currentLine);
        if (currentLine == nextLine) {
            //do nothing
            return;
        }
        if (DEBUG) {
            Log.i(TAG, "scrollLyricTo: " + nextLine);
        }
        if (!isLyricAvailable()) {
            if (DEBUG) {
                Log.w(TAG, "scrollLyricTo: " + nextLine + ", but lyric is not available, need to rebuild.");
            }
            buildLyricEntry(true);
            currentLine = nextLine;
            invalidate();
            return;
        }
        scrollCurrentPlayingToLine(nextLine);
    }

    private void scrollCurrentPlayingToLine(int nextLine) {
        final int offset = calculateOffset(currentLine, nextLine);
        final int duration = findAnimationDuration(Math.abs(nextLine - currentLine));
        currentLine = nextLine;
        animateScrollToCurrentLine(offset, duration);
    }

    private int findAnimationDuration(int offset) {
        offset = Math.abs(offset);
        final int result;
        if (offset > 5) {
            result = 1000;
        } else {
            result = 175 * offset + 125;
        }
        return result;
    }

    /**
     * 计算指定的两行歌词之间的偏移
     *
     * @param from        起始行
     * @param destination 结束行
     */
    private int calculateOffset(int from, int destination) {
        int dy = 0;//偏移量
        final boolean down = destination > from;
        if (down) {
            //计算滚动到后面歌词的偏移量
            dy += lyric.getLyricEntry(from).staticLayout.getHeight() / 2 + lyricLineSpacing;
            dy += lyric.getLyricEntry(destination).staticLayout.getHeight() / 2;
            for (int i = from + 1; i < destination; i++) {
                dy += lyric.getLyricEntry(i).staticLayout.getHeight() + lyricLineSpacing;
            }
        } else {
            //计算滚动到之前的歌词所需的偏移量
            dy -= lyric.getLyricEntry(from).staticLayout.getHeight() / 2 + lyricLineSpacing;
            dy -= lyric.getLyricEntry(destination).staticLayout.getHeight() / 2;
            for (int i = from - 1; i > destination; i--) {
                dy -= lyric.getLyricEntry(i).staticLayout.getHeight() + lyricLineSpacing;
            }
        }
        return dy;
    }

    /**
     * 开始一个滚动动画,滚动视图到当前播放的那一行歌词
     *
     * @param offset   动画需要滚动的距离
     * @param duration 动画时长
     */
    private void animateScrollToCurrentLine(int offset, final int duration) {
        if (isAnimating) {
            scrollAnimator.cancel();
        }
        offset += offsetScroll;
        if (DEBUG) {
            new Exception().printStackTrace();
            Log.i(TAG, "开始一个滚动到当前播放中歌词的动画: offset = " + offset + " duration = " + duration);
        }
        scrollAnimator = ValueAnimator.ofInt(offset, 0);
        scrollAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                offsetScroll = ((int) animation.getAnimatedValue());
                invalidate();
            }
        });
        scrollAnimator.setDuration(duration);
        scrollAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimating = false;
            }
        });
        scrollAnimator.start();
        isAnimating = true;
    }

    /**
     * @param anchorLine 前一句歌词的索引
     * @param timeStamp  歌词的时间戳(毫秒)
     * @return 该时间戳所对应的歌词位置. -1表示未找到
     */
    private int findLineByTimeStamp(final int anchorLine, int timeStamp) {
        if (lyric == null) {
            return -1;
        }
        timeStamp += lyricTimeStampOffset;//歌词偏移

        int position = anchorLine;

        if (position < 0 || position > lyric.size() - 1) {
            position = 0;
        }

        if (lyric.getTimeStamp(position) > timeStamp) {//向前查找
            while (lyric.getTimeStamp(position) > timeStamp) {
                position--;
                if (position <= 0) {
                    position = 0;
                    break;
                }
            }
        } else {//向后滚动
            while (lyric.getTimeStamp(position) < timeStamp) {//向后查找
                position++;
                if (position <= lyric.size() - 1 && lyric.getTimeStamp(position) > timeStamp) {
                    position--;
                    break;
                }
                if (position >= lyric.size() - 1) {
                    position = lyric.size() - 1;
                    break;
                }
            }
        }
        return position;
    }


    public void setLyric(Lyric lyric) {
        lyric.buildLyricEntryLayout(textPaint, getWidth(), lyricLineSpacing);
        this.lyric = lyric;
        invalidate();
    }

    public void setLyricError(String message) {
        lyric = null;
        emptyStaticLayout = new StaticLayout(message, textPaint, getWidth(),
                Layout.Alignment.ALIGN_CENTER, 1f, 0, false);
        invalidate();
    }

    public void setLyricNormalTextColor(int normalTextColor) {
        this.lyricNormalTextColor = normalTextColor;
        if (getWidth() != 0) {
            invalidate();
        }
    }

    public void setLyricPlayingTextColor(int playingTextColor) {
        this.lyricPlayingTextColor = playingTextColor;
        if (getWidth() != 0) {
            invalidate();
        }
    }

    public void setLyricLineSpacing(float spacing) {
        this.lyricLineSpacing = spacing;
        //添加行间距后,需要重新计算每一行歌词的高度.
        buildLyricEntry(true);
    }

    public void setOnPlayIndicatorClickListener(final OnPlayIndicatorClickListener onPlayIndicatorClickListener) {
        this.onPlayIndicatorClickListener = onPlayIndicatorClickListener;
    }

    public void setLyricPlayIndicatorColor(final int color) {
        lyricPlayIndicatorColor = color;
        playIndicator.setColorFilter(lyricPlayIndicatorColor, PorterDuff.Mode.SRC_IN);
        dashPaint.setColor(lyricPlayIndicatorColor);
        invalidate();
    }

    /**
     * 设置一个歌词显示时间的偏移
     *
     * @param offset 单位：毫秒。例如， 500，所有歌词显示都向前推进500ms
     *               -1000,所有歌词延后1000ms才显示
     */
    public void setLyricTimeStampOffset(final int offset) {
        this.lyricTimeStampOffset = offset;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public interface OnPlayIndicatorClickListener {
        /**
         * 滚动时出现在屏幕中央的歌词指示器被点击时触发此方法
         *
         * @param millisecond 播放指示器所指歌词的时间戳(毫秒)
         */
        void onClick(int millisecond);
    }


    /**
     * Lyric Entity
     * <p>
     * use {@link #from(String)} to new instance
     */
    public static class Lyric {

        private static final String CHAR_SET = "utf-8";

        private Lyric() {

        }

        @Nullable
        public static Lyric from(String lyric) {
            final ByteArrayInputStream inputStream;
            try {
                inputStream = new ByteArrayInputStream(lyric.getBytes(CHAR_SET));
                return LyricUtils.parseLyricFromInputStream(inputStream, CHAR_SET);
            } catch (Exception e) {
                return null;
            }
        }

        //预计的最大歌词行数
        private static final int MAX_LINES = 50;

        /**
         * key : timestamp
         * value : lyric line
         */
        SparseArray<LyricEntry> lyricEntrySparseArray = new SparseArray<>(MAX_LINES);

        /**
         * put a lyric line into {@link Lyric}
         */
        void putLyricLine(int timeStamp, String line) {
            lyricEntrySparseArray.put(timeStamp,
                    new LyricEntry(line, LyricUtils.toMusicTimeStamp(timeStamp)));
        }

        int getTimeStamp(int index) {
            return lyricEntrySparseArray.keyAt(index);
        }

        public int size() {
            return lyricEntrySparseArray.size();
        }

        LyricEntry getLyricEntry(int index) {
            return lyricEntrySparseArray.valueAt(index);
        }

        /**
         * 根据时间戳来寻找匹配当前时刻的歌词
         *
         * @param timeStamp  歌词的时间戳(毫秒)
         * @param anchorLine the start line to search
         * @return index to getLyricEntry
         */
        int findIndexByTimeStamp(final int timeStamp,
                                 final int anchorLine) {

            int position = anchorLine;
            if (position < 0 || position > size() - 1) {
                position = 0;
            }
            if (getTimeStamp(position) > timeStamp) {//向前查找
                while (getTimeStamp(position) > timeStamp) {
                    position--;
                    if (position <= 0) {
                        position = 0;
                        break;
                    }
                }
            } else {
                while (getTimeStamp(position) < timeStamp) {//向后查找
                    position++;
                    if (position <= size() - 1 && getTimeStamp(position) > timeStamp) {
                        position--;
                        break;
                    }
                    if (position >= size() - 1) {
                        position = size() - 1;
                        break;
                    }
                }
            }
            return position;
        }


        void buildLyricEntryLayout(TextPaint paint, int width, float spacingAdd) {
            for (int i = 0; i < size(); i++) {
                getLyricEntry(i).buildLayout(paint, width, spacingAdd);
            }
        }

        /**
         * one line of Lyric
         */
        static class LyricEntry {

            private final String line;

            /**
             * the static layout of this LyricEntry
             * it must be call {@link #buildLayout(TextPaint, int, float)} before get staticLayout
             */
            StaticLayout staticLayout;

            //时间戳:形如 145:00 冒号前面代表分钟,后面代表秒数
            String timeStamp;

            LyricEntry(String line, String timeStamp) {
                this.line = line;
                this.timeStamp = timeStamp;
            }

            private void buildLayout(TextPaint paint, int width, float spacingAdd) {
                staticLayout = new StaticLayout(line, paint, width, Layout.Alignment.ALIGN_CENTER,
                        1f, spacingAdd, true);
            }

            @Override
            public String toString() {
                return line + "\n";
            }

        }

        @Override
        public String toString() {
            return "Lyric{" +
                    "lyricEntrySparseArray=" + lyricEntrySparseArray.toString() +
                    '}';
        }


    }


    final static class LyricUtils {

        private static final String TAG = "LyricUtils";

        @SuppressWarnings("SameParameterValue")
        @Nullable
        static Lyric parseLyricFromInputStream(InputStream inputStream, @Nullable String charsetName) {
            return new LyricParser(charsetName).parse(inputStream);
        }

        /**
         * 将毫秒化成 分钟:秒钟 的形式，如 123:00
         */
        @SuppressLint("DefaultLocale")
        static String toMusicTimeStamp(int millisecond) {
            millisecond = millisecond / 1000;
            int second = millisecond % 60;
            int minute = millisecond / 60;
            return String.format("%02d:%02d", minute, second);
        }


        private static class LyricParser {
            private static final String CHARSET_DEFAULT = "utf-8";

            LyricParser(String charsetName) {
                this.lyric = new Lyric();
                this.encoding = charsetName == null ? CHARSET_DEFAULT : charsetName;
            }


            /**
             * file text encoding
             */
            private String encoding;

            private Lyric lyric;

            Lyric parse(InputStream inputStream) {
                InputStreamReader inputStreamReader = null;
                BufferedReader reader = null;
                try {
                    inputStreamReader = new InputStreamReader(inputStream, encoding);
                    reader = new BufferedReader(inputStreamReader);

                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (BuildConfig.DEBUG) {
                            Log.i(TAG, "parse: " + line);
                        }
                        parseLine(line);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //TODO handle exception
                } finally {
                    closeSilently(reader);
                    closeSilently(inputStreamReader);
                    closeSilently(inputStream);
                }

                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "parse: " + lyric);
                }
                return lyric;
            }

            @SuppressWarnings("StatementWithEmptyBody")
            private void parseLine(String line) {
                if (line.startsWith("[ti:")) {//TODO lyric info
//                lyric.setTitle(line.substring(4, line.length() - 1));
                } else if (line.startsWith("[ar:")) {
//                lyric.setArtist(line.substring(4, line.length() - 1));
                } else if (line.startsWith("[al:")) {
//                lyric.setAlbum(line.substring(4, line.length() - 1));
                } else if (line.startsWith("[au:")) {
//                lyric.setLyricist(line.substring(4, line.length() - 1));
                } else if (line.startsWith("[by:")) {
//                lyric.setFileMaker(line.substring(4, line.length() - 1));
                } else {
                    String regex = "\\[\\d{2}:\\d{2}.\\d{2,3}]";

                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(line);
                    while (matcher.find()) {
                        String timeMatcher = matcher.group();

                        //获取时间戳
                        int timeStamp = stamp2Int(timeMatcher.substring(1, timeMatcher.length() - 1));
                        //获取内容
                        String[] contents = pattern.split(line);

                        if (contents.length >= 1) {
                            lyric.putLyricLine(timeStamp, contents[contents.length - 1]);
                        }
                    }
                }
            }

            /**
             * 将形如 11:10.100 的时间计算成毫秒数
             */
            private int stamp2Int(final String result) {
                final int indexOfColon = result.indexOf(":");
                final int indexOfPoint = result.indexOf(".");

                final int minute = Integer.parseInt(result.substring(0, indexOfColon));
                final int second = Integer.parseInt(result.substring(indexOfColon + 1, indexOfPoint));
                final int millisecond;
                if (result.length() - indexOfPoint == 2) {
                    millisecond = Integer.parseInt(result.substring(indexOfPoint + 1, result.length())) * 10;
                } else {
                    millisecond = Integer.parseInt(result.substring(indexOfPoint + 1, result.length()));
                }
                return ((((minute * 60) + second) * 1000) + millisecond);
            }
        }

        private static void closeSilently(Closeable closeable) {
            if (closeable == null) {
                return;
            }
            try {
                closeable.close();
            } catch (Exception e) {
                //ignore
            }
        }

    }

}
