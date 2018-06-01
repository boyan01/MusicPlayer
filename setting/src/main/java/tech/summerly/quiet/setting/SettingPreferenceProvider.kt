package tech.summerly.quiet.setting

import android.content.SharedPreferences
import com.alibaba.android.arouter.facade.annotation.Route
import org.jetbrains.anko.defaultSharedPreferences
import tech.summerly.quiet.commonlib.persistence.preference.PreferenceProvider
import tech.summerly.quiet.constraints.Setting

/**
 * 提供 Preference 以供其他功能使用
 */
@Route(path = Setting.SETTING_PREFERENCE_PROVIDER)
class SettingPreferenceProvider : PreferenceProvider {

    override fun getPreference(): SharedPreferences {
        return SettingModule.defaultSharedPreferences
    }

}