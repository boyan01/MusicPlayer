package tech.summerly.quiet.local

import android.Manifest
import android.content.Context
import android.graphics.drawable.Animatable
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.storage.StorageManager
import android.support.graphics.drawable.AnimatedVectorDrawableCompat
import android.view.View
import kotlinx.android.synthetic.main.local_activity_music_scanner.*
import kotlinx.android.synthetic.main.local_activity_music_scanner.view.*
import kotlinx.coroutines.experimental.launch
import tech.summerly.quiet.commonlib.base.BaseActivity
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.utils.LoggerLevel
import tech.summerly.quiet.commonlib.utils.log
import tech.summerly.quiet.commonlib.utils.requestPermission
import tech.summerly.quiet.local.scanner.LocalMusicScannerContract
import tech.summerly.quiet.local.scanner.LocalMusicScannerPresenter
import tech.summerly.quiet.local.scanner.LocalScannerSettingDialog
import tech.summerly.quiet.local.scanner.persistence.LocalMusicScannerSetting
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
                paths = listOfNotNull(PATH_INTERNAL_STORAGE, getStoragePath(this, true)).toTypedArray(),
                actionSaveMusic = LocalMusicApi.getLocalMusicApi(this)::insertMusic,
                preference = LocalMusicScannerSetting(this)
        )
    }

    private lateinit var scannerInfoView: ScannerInfoView

    /**
     * 记录下扫描到的歌曲标题
     */
    private val musicList = ArrayList<Music>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        setContentView(R.layout.local_activity_music_scanner)
        scannerInfoView = ScannerInfoView(scannerContainer)
        buttonStartScanner.setOnClickListener {
            scannerInfoView.setScanning()
            musicList.clear()
            launch {
                val isGranted = requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                if (isGranted) {
                    presenter.startScannerJob()
                } else {
                    launch(UI) { scannerInfoView.setNotScanning() }
                }
            }
        }

        textSearchSetting.setOnClickListener {
            LocalScannerSettingDialog().show(supportFragmentManager, "LocalScannerSetting")
        }
        scannerInfoView.setNotScanning()
    }


    override fun onScannerComplete() {
        //动画结束之后,需要显示或者更新一些视图
        scannerInfoView.setNotScanning()
    }

    override fun onBackPressed() {
        presenter.stopScanMusics()
        super.onBackPressed()
    }

    override fun onAMusicScan(music: Music) {
        log { "扫描到音乐 : ${music.toShortString()}" }
        musicList.add(music)
    }

    override fun onScannerError(msg: String?) {
        log(LoggerLevel.ERROR) { "出错 : $msg " }
    }

    private class ScannerInfoView(private val view: View) {

        //设置视图显示状态处于扫描中
        fun setScanning() {
            view.buttonStartScanner.visibility = View.GONE
            view.textSearchSetting.visibility = View.GONE
            view.progressBar.visibility = View.VISIBLE
            (view.imageSearchProfile.drawable as Animatable).start()
        }

        fun setNotScanning() {
            view.buttonStartScanner.visibility = View.VISIBLE
            view.textSearchSetting.visibility = View.VISIBLE
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