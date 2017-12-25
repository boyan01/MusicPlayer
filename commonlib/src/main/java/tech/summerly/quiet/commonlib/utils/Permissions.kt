@file:Suppress("unused", "MemberVisibilityCanPrivate")

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

import java.util.HashMap


private const val TAG = "CoPermissions"

/**
 * request permission
 * @return true: granted , false : denied
 */
suspend fun Activity.requestPermission(permission: String): Boolean = requestPermission(permissions = *arrayOf(permission))[0]

/**
 * request a list of permissions ,return result array for all permissions
 */
suspend fun Activity.requestPermission(vararg permissions: String): Array<Boolean> = suspendCancellableCoroutine { continuation ->
    launch(UI) {
        val fragment = createPermissionFragment(this@requestPermission)
        launch {
            fragment.setPermissionResultListener {
                continuation.resume(it.map { it.granted }.toTypedArray())
            }
            fragment.requestPermissions(arrayOf(*permissions))
        }
    }
}

//get a permission request fragment.
private fun createPermissionFragment(activity: Activity): RxPermissionsFragment {
    return activity.fragmentManager.findFragmentByTag(TAG) as? RxPermissionsFragment
            ?: RxPermissionsFragment()
            .also {
                activity.fragmentManager
                        .beginTransaction()
                        .add(it, TAG)
                        .commitAllowingStateLoss()
                activity.fragmentManager.executePendingTransactions()
            }
}

internal class RxPermissionsFragment : Fragment() {

    // Contains all the current permission requests.
    // Once granted or denied, they are removed from it.
    private val mSubjects = HashMap<String, Permission>()

    private var permissionsListener: ((List<Permission>) -> Unit)? = null

    companion object {
        private val PERMISSIONS_REQUEST_CODE = 42
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    @TargetApi(Build.VERSION_CODES.M)
    internal fun requestPermissions(permissions: Array<String>) {
        launch(UI) {
            requestPermissions(permissions, PERMISSIONS_REQUEST_CODE)
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode != PERMISSIONS_REQUEST_CODE) return

        val shouldShowRequestPermissionRationale = BooleanArray(permissions.size)

        for (i in permissions.indices) {
            shouldShowRequestPermissionRationale[i] = shouldShowRequestPermissionRationale(permissions[i])
        }

        onRequestPermissionsResult(permissions, grantResults, shouldShowRequestPermissionRationale)
    }

    private fun onRequestPermissionsResult(permissions: Array<String>, grantResults: IntArray,
                                           shouldShowRequestPermissionRationale: BooleanArray) {
        val permissionList = List(permissions.size) { i ->
            Permission(permissions[i], grantResults[i] == PackageManager.PERMISSION_GRANTED,
                    shouldShowRequestPermissionRationale[i])
        }
        permissionsListener?.invoke(permissionList)
    }

    @TargetApi(Build.VERSION_CODES.M)
    internal fun isGranted(permission: String): Boolean {
        return activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    @TargetApi(Build.VERSION_CODES.M)
    internal fun isRevoked(permission: String): Boolean {
        return activity.packageManager.isPermissionRevokedByPolicy(permission, activity.packageName)
    }

    fun containsByPermission(permission: String): Boolean {
        return mSubjects.containsKey(permission)
    }


    internal fun setPermissionResultListener(listener: (List<Permission>) -> Unit) {
        this.permissionsListener = listener
    }

}

internal class Permission(val name: String,
                          val granted: Boolean,
                          val shouldShowRequestPermissionRationale: Boolean = false) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val that = other as Permission?

        if (granted != that!!.granted) return false
        return if (shouldShowRequestPermissionRationale != that.shouldShowRequestPermissionRationale) false else name == that.name
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + if (granted) 1 else 0
        result = 31 * result + if (shouldShowRequestPermissionRationale) 1 else 0
        return result
    }

    override fun toString(): String {
        return "Permission{" +
                "name='" + name + '\'' +
                ", granted=" + granted +
                ", shouldShowRequestPermissionRationale=" + shouldShowRequestPermissionRationale +
                '}'
    }
}