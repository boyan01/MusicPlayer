package tech.summerly.quiet.local

import android.content.Intent
import android.os.Bundle
import tech.summerly.quiet.commonlib.base.BaseActivity
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.fragments.BottomControllerFragment
import tech.summerly.quiet.commonlib.utils.log
import tech.summerly.quiet.local.fragments.LocalMusicListFragment

/**
 * Created by summer
 */
internal class LocalMusicListActivity : BaseActivity(), BottomControllerFragment.BottomControllerContainer {

    companion object {
        fun start(title: String, musics: ArrayList<Music>) {
            val intent = Intent(LocalModule, LocalMusicListActivity::class.java)
            intent.putExtra("title", title)
            intent.putExtra("musics", musics)
            LocalModule.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.local_activity_music_list)

        val title = intent.getStringExtra("title")
        @Suppress("UNCHECKED_CAST")
        val musics = intent.getSerializableExtra("musics") as? ArrayList<Music>
                ?: ArrayList()
        supportFragmentManager.beginTransaction().add(R.id.container, LocalMusicListFragment(title, musics)).commit()
    }

}