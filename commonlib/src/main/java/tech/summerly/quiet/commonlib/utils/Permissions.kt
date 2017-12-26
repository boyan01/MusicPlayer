package tech.summerly.quiet.commonlib.utils

import android.annotation.TargetApi
import android.app.Activity
import android.app.Fragment
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.suspendCancellableCoroutine


private const val TAG = "CoPermissions"

/**
 * request permission
 * @return true: granted , false : denied
 */
suspend fun Activity.requestPermission(permission: String): Boolean = requestPermission(permissions = *arrayOf(permission))[0]

/**
 * request a list of permissions ,return result array for all permissions
 */
suspend fun Activity.requestPermission(vararg permissions: String): BooleanArray = suspendCancellableCoroutine { continuation ->
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
private fun createPermissionFragment(activity: Activity): CoPermissionsFragment {
    return activity.fragmentManager.findFragmentByTag(TAG) as? CoPermissionsFragment
            ?: CoPermissionsFragment()
            .also {
                activity.fragmentManager
                        .beginTransaction()
                        .add(it, TAG)
                        .commitAllowingStateLoss()
                activity.fragmentManager.executePendingTransactions()
            }
}


internal class CoPermissionsFragment : Fragment() {

    companion object {
        private val PERMISSIONS_REQUEST_CODE = 303
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
        val permissionResults = BooleanArray(permissions.size, {
            grantResults.getOrNull(it) == PackageManager.PERMISSION_GRANTED
        })
        permissionsResultCallback?.invoke(permissionResults)
    }

    fun setPermissionResultCallback(callback: (BooleanArray) -> Unit) {
        this.permissionsResultCallback = callback
    }
}