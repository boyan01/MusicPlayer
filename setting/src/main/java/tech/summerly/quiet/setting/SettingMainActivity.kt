package tech.summerly.quiet.setting

import android.os.Bundle
import android.view.MenuItem
import com.alibaba.android.arouter.facade.annotation.Route
import kotlinx.android.synthetic.main.setting_activity_main.*
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
        setContentView(R.layout.setting_activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content, GeneralSettingFragment())
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