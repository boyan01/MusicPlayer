package tech.soit.quiet.utils.test

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tech.soit.quiet.utils.component.support.QuietViewModelProvider

object ViewModelUtil {

    fun <T : ViewModel> createFor(vararg models: T): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {

            private val quiet = QuietViewModelProvider()

            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                models.forEach { model ->
                    if (modelClass.isAssignableFrom(model.javaClass)) {
                        @Suppress("UNCHECKED_CAST")
                        return model as T
                    }
                }
                return quiet.create(modelClass)
            }
        }
    }

}