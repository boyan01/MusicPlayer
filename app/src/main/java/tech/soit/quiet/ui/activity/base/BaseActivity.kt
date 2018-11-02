package tech.soit.quiet.ui.activity.base

import android.content.Intent
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.transition.TransitionManager
import kotlinx.android.synthetic.main.base_activity_bottom_controller.*
import kotlinx.android.synthetic.main.content_bottom_controller.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import tech.soit.quiet.R
import tech.soit.quiet.model.vo.Music
import tech.soit.quiet.player.core.IMediaPlayer
import tech.soit.quiet.ui.activity.MusicPlayerActivity
import tech.soit.quiet.utils.annotation.DisableLayoutInject
import tech.soit.quiet.utils.annotation.EnableBottomController
import tech.soit.quiet.utils.annotation.LayoutId
import tech.soit.quiet.utils.component.ImageLoader
import tech.soit.quiet.utils.component.support.QuietViewModelProvider
import tech.soit.quiet.utils.component.support.attrValue
import tech.soit.quiet.utils.component.support.observeNonNull
import tech.soit.quiet.utils.component.support.string
import tech.soit.quiet.utils.subTitle
import tech.soit.quiet.viewmodel.MusicControllerViewModel
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.findAnnotation

/**
 *
 * the BaseActivity of this application
 *
 * use [LayoutId] annotation to bind a layout to activity
 *
 * use [EnableBottomController] to enable bottom controller for this activity
 *
 */
abstract class BaseActivity : AppCompatActivity(), CoroutineScope {

    private val controllerViewModel by lazyViewModel<MusicControllerViewModel>()

    var viewModelFactory: ViewModelProvider.Factory = QuietViewModelProvider()

    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        checkTest()
        super.onCreate(savedInstanceState)
        job = Job()

        //inject layout for annotation
        val isInjectLayout = this::class.findAnnotation<DisableLayoutInject>() == null
        val layoutId = this::class.findAnnotation<LayoutId>()
        if (isInjectLayout && layoutId != null) {
            setContentView(layoutId.value)
        }

        if (enableBottomController()) {
            //make bottom controller interchangeable
            listenBottomControllerEvent()
        }
    }

    /**
     *  check for test , if is in test mode, will replace [viewModelFactory]
     */
    private fun checkTest() {
        val isTest = intent.getBooleanExtra("isTest", false)
        if (!isTest) {
            return
        }
        val rule = Class.forName("tech.soit.quiet.utils.test.BaseActivityTestRule").kotlin
        val objectInstance = rule.companionObjectInstance ?: return
        val objectClass = rule.companionObject ?: return
        objectClass.declaredFunctions.find { it.name == "injectActivity" }?.call(objectInstance, this)
    }

    /**
     * observe [controllerViewModel] states
     *
     * ATTENTION!! need add layout [R.layout.content_bottom_controller] to activity's
     * content view if you do not use [EnableBottomController] annotation
     */
    protected fun listenBottomControllerEvent() {
        controllerViewModel.playingMusic.observe(this, Observer { playing ->
            TransitionManager.beginDelayedTransition(root)
            if (playing == null) {
                bottomControllerLayout.isGone = true
            } else {
                bottomControllerLayout.isVisible = true
                updateBottomController(playing)
            }
        })
        controllerViewModel.playerState.observeNonNull(this) { state ->
            if (state == IMediaPlayer.PLAYING) {
                controllerPauseOrPlay.setImageResource(R.drawable.ic_pause_black_24dp)
                controllerPauseOrPlay.contentDescription = string(R.string.pause)
            } else {
                controllerPauseOrPlay.setImageResource(R.drawable.ic_play_arrow_black_24dp)
                controllerPauseOrPlay.contentDescription = string(R.string.play)
            }
        }
        bottomControllerLayout.setOnClickListener {
            startActivity(Intent(this, MusicPlayerActivity::class.java))
        }
        controllerPlaylist.setOnClickListener {
            //TODO
        }
        controllerPauseOrPlay.setOnClickListener {
            controllerViewModel.pauseOrPlay()
        }
    }

    /**
     * is should init bottom controller for this activity or not
     */
    protected open fun enableBottomController(): Boolean {
        val annotation = this::class.annotations.firstOrNull { it is EnableBottomController }
        return annotation != null
    }


    /**
     * update BottomController UI
     */
    private fun updateBottomController(playing: Music) {
        //更新音乐信息
        musicTitle.text = playing.getTitle()
        musicSubTitle.text = playing.subTitle

        val picUri = playing.getAlbum().getCoverImageUrl()
        if (picUri != null) {
            ImageLoader.with(this).load(picUri).into(artWork)
        } else {
            artWork.setImageDrawable(ColorDrawable(attrValue(R.attr.colorPrimary)))
        }
    }


    override fun setContentView(layoutResID: Int) {
        if (enableBottomController()) {
            //if need show bottom controller, we need inject this method to add controller layout to screen
            super.setContentView(R.layout.base_activity_bottom_controller)
            val container = findViewById<ViewGroup>(R.id.container)
            LayoutInflater.from(this).inflate(layoutResID, container)
        } else {
            super.setContentView(layoutResID)
        }

    }

    override fun setContentView(view: View) {
        if (enableBottomController()) {
            //if need show bottom controller, we need inject this method to add controller layout to screen
            super.setContentView(R.layout.base_activity_bottom_controller)
            val container = findViewById<ViewGroup>(R.id.container)
            container.removeAllViews()
            container.addView(view)
        } else {
            super.setContentView(view)
        }
    }

    override fun setContentView(view: View, params: ViewGroup.LayoutParams?) {
        if (enableBottomController()) {
            //if need show bottom controller, we need inject this method to add controller layout to screen
            super.setContentView(R.layout.base_activity_bottom_controller)
            val container = findViewById<ViewGroup>(R.id.container)
            container.removeAllViews()
            container.addView(view, params)
        }
        super.setContentView(view, params)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    /**
     * lazy generate ViewModel
     *
     * example :
     *   private val viewModel by lazyViewModel(XXXViewModel::class)
     *
     */
    protected inline fun <reified T : ViewModel> lazyViewModel(): Lazy<T> = lazy {
        ViewModelProviders.of(this, viewModelFactory).get(T::class.java)
    }


    /**
     * @return first : width
     *         second: height
     */
    fun getWindowSize(): Pair<Int, Int> {
        val point = Point()
        windowManager.defaultDisplay.getSize(point)
        return point.x to point.y
    }


}