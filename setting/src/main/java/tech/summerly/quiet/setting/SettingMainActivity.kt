package tech.summerly.quiet.setting

import android.os.Bundle
import android.view.MenuItem
import com.alibaba.android.arouter.facade.annotation.Route
import tech.summerly.quiet.commonlib.base.BaseActivity
import tech.summerly.quiet.constraints.Setting
import tech.summerly.quiet.setting.fragments.GeneralSettingFragment

/**
 * Created by summer on 18-2-12
 */
@Route(path = Setting.ACTIVITY_SETTING_MAIN)
class SettingMainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(android.R.id.content, GeneralSettingFragment())
        transaction.commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}