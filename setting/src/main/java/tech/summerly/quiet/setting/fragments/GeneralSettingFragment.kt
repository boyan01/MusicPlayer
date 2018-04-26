package tech.summerly.quiet.setting.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import tech.summerly.quiet.commonlib.player.core.CoreMediaPlayer
import tech.summerly.quiet.setting.R

/**
 * Created by summer on 18-2-13
 * General Setting
 */
class GeneralSettingFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
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
                CoreMediaPlayer.volume = volume / 100F
            }
        }

    }

}