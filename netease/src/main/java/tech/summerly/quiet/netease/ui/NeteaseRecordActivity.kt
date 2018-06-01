package tech.summerly.quiet.netease.ui

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import kotlinx.android.synthetic.main.netease_activity_record.*
import me.drakeet.multitype.MultiTypeAdapter
import tech.summerly.quiet.commonlib.base.BaseActivity
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.bean.Record
import tech.summerly.quiet.commonlib.component.callback.BottomControllerHost
import tech.summerly.quiet.commonlib.fragments.StatedRecyclerFragment
import tech.summerly.quiet.commonlib.fragments.UnimplementedFragment
import tech.summerly.quiet.commonlib.player.MusicPlayerManager
import tech.summerly.quiet.commonlib.player.listenMusicChangePosition
import tech.summerly.quiet.commonlib.utils.multiTypeAdapter
import tech.summerly.quiet.netease.R
import tech.summerly.quiet.netease.persistence.NeteasePreference
import tech.summerly.quiet.netease.ui.items.RecordItemViewBinder
import tech.summerly.quiet.service.netease.NeteaseCloudMusicApi

/**
 * Created by summer on 18-2-27
 */
@Route(path = "/netease/record")
internal class NeteaseRecordActivity : BaseActivity(), BottomControllerHost {

    companion object {

        const val KEY_USER_ID = "user_id"

        private const val COUNT_PAGE = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.netease_activity_record)
        pager.adapter = SectionsPagerAdapter(supportFragmentManager)
        pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(pager))
        pager.offscreenPageLimit = COUNT_PAGE
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onStart() {
        super.onStart()
        initBottomController()
    }

    private val uid: Long
        get() = NeteasePreference.getLoginUser()?.userId ?: 0


    private inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        val fragments = Array<Fragment?>(count) { null }

        override fun getItem(position: Int): Fragment? = fragments[position] ?: when (position) {
            0 -> RecordFragment(uid, 1)
            1 -> RecordFragment(uid, 0)
            else -> UnimplementedFragment()
        }.also {
            fragments[position] = it
        }


        override fun getCount(): Int = COUNT_PAGE

    }

    class RecordFragment : StatedRecyclerFragment<Record>() {

        companion object {

            private const val KEY_TYPE = "type"

            private const val PLAYER_TOKEN = "netease_record"

            /**
             * @param uid 用户id
             * @param type 0:所有记录 ， 1:一周记录
             */
            operator fun invoke(uid: Long, type: Int): RecordFragment {
                val bundle = Bundle()
                bundle.putLong(KEY_USER_ID, uid)
                bundle.putInt(KEY_TYPE, type)
                return RecordFragment().also {
                    it.arguments = bundle
                }
            }

        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            listenMusicChangePosition(
                    { items },
                    { any, music -> (any as? Record?)?.music == music }
            ) { from, to ->
                if (from != -1) {
                    adapter?.notifyItemChanged(from)
                }
                if (to != -1) {
                    adapter?.notifyItemChanged(to)
                }
            }
        }

        private val uid: Long get() = requireNotNull(arguments?.getLong(KEY_USER_ID))

        private val type: Int get() = requireNotNull(arguments?.getInt(KEY_TYPE))

        private val items = ArrayList<Record>()


        override fun initRecyclerView(recyclerView: RecyclerView) {
            recyclerView.adapter = MultiTypeAdapter(items)
            recyclerView.multiTypeAdapter.register(Record::class.java, RecordItemViewBinder(this::onMusicItemClick))
            items.clear()
        }

        override suspend fun loadData(): List<Record> {
            return NeteaseCloudMusicApi().getUserRecord(uid, type)
        }

        override fun onLoadSuccess(result: List<Record>) {
            super.onLoadSuccess(result)
            items.addAll(result)
            adapter?.notifyDataSetChanged()
        }

        private fun onMusicItemClick(music: Music) {
            MusicPlayerManager.play(PLAYER_TOKEN + type, items.map { it.music }, music)
        }

    }
}