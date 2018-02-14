package tech.summerly.quiet.setting.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceFragment
import tech.summerly.quiet.commonlib.player.core.CoreMediaPlayer
import tech.summerly.quiet.setting.R

/**
 * Created by summer on 18-2-13
 * General Setting
 */
class GeneralSettingFragment : PreferenceFragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.setting_pref_general)
    }

    override fun onStart() {
        super.onStart()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onStop() {
        super.onStop()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        when (key) {
            "key_night_mode" -> {

            }
            "key_volume" -> {
                val volume = sharedPreferences.getInt(key, 100)
                CoreMediaPlayer.volume = volume.toFloat()
            }
        }

    }

}