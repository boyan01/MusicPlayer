package tech.summerly.quiet.local

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Animatable
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.storage.StorageManager
import android.support.graphics.drawable.AnimatedVectorDrawableCompat
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.local_activity_music_scanner.*
import kotlinx.android.synthetic.main.local_activity_music_scanner.view.*
import me.drakeet.multitype.MultiTypeAdapter
import tech.summerly.quiet.commonlib.base.BaseActivity
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.utils.*
import tech.summerly.quiet.local.scanner.LocalMusicScannerContract
import tech.summerly.quiet.local.scanner.LocalMusicScannerPresenter
import tech.summerly.quiet.local.scanner.LocalScannerSettingDialog
import java.lang.reflect.Array
import java.lang.reflect.InvocationTargetException

/**
 * Created by summer on 17-12-21
 */
class LocalMusicScannerActivity : BaseActivity(), LocalMusicScannerContract.View {


    companion object {
        private val PATH_INTERNAL_STORAGE: String = Environment.getExternalStorageDirectory().path
    }

    override val presenter: LocalMusicScannerPresenter by lazy {
        LocalMusicScannerPresenter(
                view = this,
                paths = listOfNotNull(PATH_INTERNAL_STORAGE, getStoragePath(this, true)).toTypedArray()
        )
    }

    private lateinit var scannerInfoView: ScannerInfoView

    /**
     * 记录下扫描到的歌曲标题
     */
    private val musicList = ArrayList<Music>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.local_activity_music_scanner)
        scannerInfoView = ScannerInfoView(scannerContainer)
        buttonStartScanner.setOnClickListener {

        }

        buttonStopScanner.setOnClickListener {
            presenter.stopScanMusics()
        }
        textSearchSetting.setOnClickListener {
            LocalScannerSettingDialog().show(supportFragmentManager, "LocalScannerSetting")
        }
        listMusicInfo.adapter = MultiTypeAdapter(musicList)
                .also {
                    it.register(Music::class.java, MusicInfoViewBinder())
                }
        scannerInfoView.setNotScanning()
    }

    override fun onScannerStart() {
        scannerInfoView.setScanning()
        musicList.clear()
        listMusicInfo.multiTypeAdapter.notifyDataSetChanged()
    }

    override fun onScannerComplete() {
        //动画结束之后,需要显示或者更新一些视图
        scannerInfoView.setNotScanning()
        //TODO 添加一个TextView显示添加了多少歌曲和总歌曲数
    }

    override fun onBackPressed() {
        if (scannerInfoView.isScanning()) {
            presenter.stopScanMusics()
            return
        }
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onMusicScanned(music: Music) {
        log { "扫描到音乐 : ${music.toShortString()}" }
        musicList.add(music)
        listMusicInfo.apply {
            val last = musicList.size - 1
            multiTypeAdapter.notifyItemInserted(last)
            smoothScrollToPosition(last)
        }
    }

    override fun onScannerError(msg: String?) {
        log(LoggerLevel.ERROR) { "出错 : $msg " }
    }

    private class ScannerInfoView(private val view: View) {

        //设置视图显示状态处于扫描中
        fun setScanning() {
            view.buttonStartScanner.visibility = View.GONE
            view.textSearchSetting.visibility = View.GONE
            view.buttonStopScanner.visibility = View.VISIBLE
            view.progressBar.visibility = View.VISIBLE
            (view.imageSearchProfile.drawable as Animatable).start()
        }

        fun setNotScanning() {
            view.buttonStartScanner.visibility = View.VISIBLE
            view.textSearchSetting.visibility = View.VISIBLE
            view.buttonStopScanner.visibility = View.GONE
            view.progressBar.visibility = View.INVISIBLE
            with(view.imageSearchProfile.drawable) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    (this as? AnimatedVectorDrawable)?.reset()
                            ?: (this as? AnimatedVectorDrawableCompat)?.stop()
                } else {
                    TODO("VERSION.SDK_INT < M")
                }

            }
        }

        fun isScanning(): Boolean {
            return view.buttonStartScanner.visibility != View.VISIBLE
        }
    }

    private class MusicInfoViewBinder : ItemViewBinder<Music>() {
        override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
            return InfoViewHolder(inflater.inflate(android.R.layout.test_list_item, parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, item: Music) {
            holder as InfoViewHolder
            holder.info.text = item.toShortString()
        }

        private class InfoViewHolder(view: View) : ViewHolder(view) {
            val info: TextView = (view as TextView)
                    .also {
                        view.ellipsize = TextUtils.TruncateAt.END
                        view.setTextColor(it.context.color(R.color.common_textSecondary))
                        view.setSingleLine(true)
                        view.textSize = 12f
                    }
        }
    }


    /**
     * @param is_removale true返回外置存储卡路径 false返回内置存储卡的路径
     */
    private fun getStoragePath(mContext: Context = this, is_removale: Boolean): String? {

        val mStorageManager = mContext.getSystemService(Context.STORAGE_SERVICE) as StorageManager
        val storageVolumeClazz: Class<*>?
        try {
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume")
            val getVolumeList = mStorageManager.javaClass.getMethod("getVolumeList")
            val getPath = storageVolumeClazz!!.getMethod("getPath")
            val isRemovable = storageVolumeClazz.getMethod("isRemovable")
            val result = getVolumeList.invoke(mStorageManager)
            val length = Array.getLength(result)
            for (i in 0 until length) {
                val storageVolumeElement = Array.get(result, i)
                val path = getPath.invoke(storageVolumeElement) as String
                val removable = isRemovable.invoke(storageVolumeElement) as Boolean
                if (is_removale == removable) {
                    return path
                }
            }
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        return null
    }
}