package tech.summerly.quiet.commonlib.persistence.preference

import android.content.Context
import android.content.SharedPreferences
import com.alibaba.android.arouter.facade.template.IProvider
import com.alibaba.android.arouter.launcher.ARouter

interface PreferenceProvider : IProvider {


    companion object {

        /**
         * build a PreferenceProvider by ARouter
         */
        fun with(path: String): PreferenceProvider {
            return ARouter.getInstance().build(path).navigation() as PreferenceProvider
        }

    }

    override fun init(context: Context) {

    }


    fun getPreference(): SharedPreferences


}