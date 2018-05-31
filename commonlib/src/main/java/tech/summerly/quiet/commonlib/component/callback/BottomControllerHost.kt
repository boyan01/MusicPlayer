package tech.summerly.quiet.commonlib.component.callback

import android.app.Activity
import android.support.transition.TransitionManager
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.alibaba.android.arouter.launcher.ARouter
import org.jetbrains.anko.contentView
import tech.summerly.quiet.commonlib.R
import tech.summerly.quiet.commonlib.utils.intransaction
import tech.summerly.quiet.commonlib.utils.log
import tech.summerly.quiet.constraints.Player

interface BottomControllerHost {


    companion object {
        private const val PATH_BOTTOM_CONTROLLER = Player.FRAGMENT_BOTTOM_CONTROLLER
    }

    fun initBottomController() {
        val container = getContainer() ?: return
        val fm = getAvailableFragmentManager() ?: return
        val fragment = fm.findFragmentByTag(PATH_BOTTOM_CONTROLLER)
                ?: ARouter.getInstance().build(PATH_BOTTOM_CONTROLLER).navigation() as Fragment
        fm.intransaction {
            replace(container.id, fragment, PATH_BOTTOM_CONTROLLER)
        }
    }

    fun getBottomControllerContainer(): View = error("can not get bottom controller'host")

    fun hideBottomController() {
        val container = getContainer() ?: return
        val parent = container.parent as ViewGroup
        TransitionManager.beginDelayedTransition(parent)
        container.isGone = true
    }


    fun getAvailableFragmentManager(): FragmentManager? {
        return when (this) {
            is FragmentActivity -> supportFragmentManager
            is Fragment -> childFragmentManager
            else -> null
        }
    }


    private fun getContainer(): View? {
        val view = when (this) {
            is Activity -> contentView
            is Fragment -> view
            else -> getBottomControllerContainer()
        }
        if (view == null) {
            log { "can not get bottom controller'host" }
            return null
        }
        return view.findViewById<ViewGroup>(R.id.bottomPlayerContainer)
                ?: throw RuntimeException("can not find R.id.bottomPlayerContainer")
    }

    fun showBottomController() {
        val container = getContainer() ?: return
        val parent = container.parent as ViewGroup
        TransitionManager.beginDelayedTransition(parent)
        container.isVisible = true
    }

}