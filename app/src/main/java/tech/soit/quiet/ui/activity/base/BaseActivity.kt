package tech.soit.quiet.ui.activity.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import tech.soit.quiet.R
import tech.soit.quiet.ui.view.ContentFrameLayout
import tech.soit.quiet.utils.annotation.DisableLayoutInject
import tech.soit.quiet.utils.annotation.LayoutId
import kotlin.reflect.full.findAnnotation

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isInjectLayout = this::class.findAnnotation<DisableLayoutInject>() == null
        val layoutId = this::class.findAnnotation<LayoutId>()
        if (isInjectLayout && layoutId != null) {
            setContentView(layoutId.value)
        }
    }

    /**
     * add a
     */
    override fun setContentView(layoutResID: Int) {
        val content = ContentFrameLayout(this)
        content.id = R.id.content
        LayoutInflater.from(this).inflate(layoutResID, content, true)
        super.setContentView(content)
    }

    override fun setContentView(view: View, params: ViewGroup.LayoutParams?) {
        val content = ContentFrameLayout(this)
        content.id = R.id.content
        content.addView(view, params)
        super.setContentView(content, params)
    }

    override fun setContentView(view: View) {
        val content = ContentFrameLayout(this)
        content.id = R.id.content
        content.addView(view)
        super.setContentView(content)
    }

    override fun addContentView(view: View, params: ViewGroup.LayoutParams?) {
        val content = findViewById<ViewGroup>(R.id.content)
        if (content != null) {//ensure there is only one R.id.content view in Activity
            content.addView(view, params)
            window.callback.onContentChanged()
        } else {
            val newContent = ContentFrameLayout(this)
            newContent.id = R.id.content
            newContent.addView(view, params)
            super.addContentView(newContent, params)
        }
    }

}