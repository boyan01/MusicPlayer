package tech.soit.quiet.utils

import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.suspendCancellableCoroutine
import tech.soit.quiet.ui.fragment.base.BaseFragment


/**
 * run when permission granted
 *
 * @param permissions see [android.Manifest.permission]
 * @param onGranted run when permission granted
 */
fun FragmentActivity.doWithPermissions(
        vararg permissions: String,
        onDenied: ((permission: List<String>) -> Unit)? = null,
        onGranted: () -> Unit) {
    launch(UI) {
        val result = requestPermission(*permissions)
        if (result.all { it }) {
            onGranted()
        } else if (onDenied != null) {
            val deniedPermissions = result.mapIndexed { index, granted ->
                return@mapIndexed if (granted) permissions[index] else null
            }.filterNotNull()
            onDenied(deniedPermissions)
        }
    }
}


/**
 * request a list of permissions ,return result array for all permissions
 */
private suspend fun FragmentActivity.requestPermission(vararg permissions: String): BooleanArray = suspendCancellableCoroutine { continuation ->
    launch(UI) {
        val fragment = createPermissionFragment(this@requestPermission)
        launch {
            fragment.setPermissionResultCallback {
                continuation.resume(it)
            }
            fragment.requestPermissions(arrayOf(*permissions))
        }
    }
}

//get a permission request fragment.
private fun createPermissionFragment(activity: FragmentActivity): CoPermissionsFragment {
    return activity.supportFragmentManager.findFragmentByTag(CoPermissionsFragment.TAG) as? CoPermissionsFragment
            ?: CoPermissionsFragment()
                    .also {
                        activity.supportFragmentManager
                                .beginTransaction()
                                .add(it, CoPermissionsFragment.TAG)
                                .commitAllowingStateLoss()
                        activity.supportFragmentManager.executePendingTransactions()
                    }
}

/**
 * @author : summer
 * @date : 18-9-1
 */
internal class CoPermissionsFragment : BaseFragment() {

    companion object {
        private const val PERMISSIONS_REQUEST_CODE = 12305

        const val TAG = "CoPermissionsFragment"
    }

    private var permissionsResultCallback: ((BooleanArray) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    @TargetApi(Build.VERSION_CODES.M)
    internal fun requestPermissions(permissions: Array<String>) = launch(UI) {
        requestPermissions(permissions, PERMISSIONS_REQUEST_CODE)
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != PERMISSIONS_REQUEST_CODE) return
        val permissionResults = BooleanArray(permissions.size) {
            grantResults.getOrNull(it) == PackageManager.PERMISSION_GRANTED
        }
        permissionsResultCallback?.invoke(permissionResults)
    }

    fun setPermissionResultCallback(callback: (BooleanArray) -> Unit) {
        this.permissionsResultCallback = callback
    }
}