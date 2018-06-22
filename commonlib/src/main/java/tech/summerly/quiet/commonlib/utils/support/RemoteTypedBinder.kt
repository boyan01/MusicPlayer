package tech.summerly.quiet.commonlib.utils.support

import android.view.ViewGroup
import android.widget.TextView
import com.alibaba.android.arouter.launcher.ARouter
import tech.summerly.quiet.commonlib.R
import tech.summerly.quiet.commonlib.utils.LoggerLevel
import tech.summerly.quiet.commonlib.utils.log
import kotlin.reflect.full.declaredMemberFunctions

class RemoteTypedBinderWrapper<T : Any>(
        private val binder: TypedBinder<T>?
) : TypedBinder<T>() {

    companion object {

        private val sUnImplementedItemViewBinder = UnImplementedItemViewBinder()

        fun <T : Any> withPath(path: String): RemoteTypedBinderWrapper<T> {
            val obj = ARouter.getInstance().build(path).navigation()
            @Suppress("UNCHECKED_CAST")
            return RemoteTypedBinderWrapper<T>(obj as TypedBinder<T>?)
        }

    }

    public override fun attachAdapter(adapter: TypedAdapter) {
        super.attachAdapter(adapter)
        binder?.attachAdapter(adapter)
    }

    /**
     * 反射的封装，便于调用远程方法
     */
    fun invoke(methodName: String, vararg parameter: Any) {
        binder ?: return
        try {
            val function = binder::class.declaredMemberFunctions.find { it.name == methodName }
            if (function == null) {
                log(LoggerLevel.ERROR) { "can not find function $methodName" }
                return
            }
            function.call(binder, *parameter)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        @Suppress("IfThenToElvis")
        return if (binder == null) {
            sUnImplementedItemViewBinder.onCreateViewHolder(parent)
        } else {
            binder.onCreateViewHolder(parent)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, item: T) {
        binder?.onBindViewHolder(holder, item)
    }

}


private class UnImplementedItemViewBinder : TypedBinder<Any>() {
    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val textView = TextView(parent.context)
        textView.setText(R.string.unimplemented_view)
        return ViewHolder(textView)
    }

    override fun onBindViewHolder(holder: ViewHolder, item: Any) {
        //do nothing
    }

}
