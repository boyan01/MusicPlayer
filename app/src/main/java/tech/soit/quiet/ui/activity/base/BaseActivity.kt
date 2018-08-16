package tech.soit.quiet.ui.activity.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import tech.soit.quiet.utils.annotation.LayoutId
import kotlin.reflect.full.findAnnotation

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layoutId = this::class.findAnnotation<LayoutId>()
        if (layoutId != null) {
            setContentView(layoutId.value)
        }
    }

}