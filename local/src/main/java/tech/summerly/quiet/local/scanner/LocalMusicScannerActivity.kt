package tech.summerly.quiet.local.scanner

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
import kotlinx.coroutines.experimental.cancelAndJoin
import kotlinx.coroutines.experimental.launch
import tech.summerly.quiet.commonlib.base.BaseActivity
import tech.summerly.quiet.commonlib.utils.*
import tech.summerly.quiet.local.LocalModule
import tech.summerly.quiet.local.R
import java.lang.reflect.Array
import java.lang.reflect.InvocationTargetException

/**
 * Created by summer on 17-12-21
 */
internal class LocalMusicScannerActivity : BaseActivity(), LocalMusicScannerContract.View {
    override fun onMusicScan(folder: String, name: String) {
        log { "path :$folder , name:$name" }
    }


    companion object {
        private val PATH_INTERNAL_STORAGE: String = Environment.getExternalStorageDirectory().path
    }

    private val disks = listOfNotNull(PATH_INTERNAL_STORAGE, getStoragePath(LocalModule, true))

    override val presenter: LocalMusicScannerPresenter by lazy {
        LocalMusicScannerPresenter(this)
    }

    private lateinit var scannerInfoView: ScannerInfoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        setContentView(R.layout.local_activity_music_scanner)
        scannerInfoView = ScannerInfoView(scannerContainer)
        buttonStartScanner.setOnClickListener {
            scannerInfoView.setScanning()
            launch(UI + job) {
                val isGranted: Boolean = requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                if (!isGranted) {
                    scannerInfoView.setNotScanning()
                    return@launch
                }
                log { "start scan $disks" }
                disks.forEach {
                    presenter.scan(it).join()
                }
                onScannerComplete()
            }
        }
        buttonClose.setOnClickListener {
            onBackPressed()
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
        UI.submit {
            job.cancelAndJoin()
            super.onBackPressed()
        }
    }

    override fun onScannerError(msg: String?) {
        log(LoggerLevel.ERROR) { "出错 : $msg " }
    }

    private class ScannerInfoView(private val view: View) {

        //设置视图显示状态处于扫描中
        fun setScanning() {
            view.buttonStartScanner.gone()
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
    private fun getStoragePath(mContext: Context = LocalModule, is_removale: Boolean): String? {

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