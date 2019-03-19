package arms.slai.com.xtest

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.work.*
import arms.slai.com.xtest.interfaces.IMainPresenter
import arms.slai.com.xtest.work.WorkBuilder

class MainPresenter(var activity: MainActivity?) : IMainPresenter {

    val REQUEST_CODE = 47

    // single threw put method to check for location work and setup if permission is granted
    override fun setupLocationWork() {
        val permissionResult = checkLocationPermissionAndRequest()
        // if permissions are granted or not needed, fire away
        if(permissionResult == PermissionResult.GRANTED || permissionResult == PermissionResult.LOWER_API){
            // Schedule work
            val debug = BuildConfig.BUILD_TYPE != "debug"
            WorkManager.getInstance().enqueue(
                if(debug) WorkBuilder.buildOneTimeJob()
                else WorkBuilder.buildPeriodicJob()
            )
        }
    }

    // check for location permission, request if needed
    override fun checkLocationPermissionAndRequest() : PermissionResult {
        return if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // only request permissions when higher or equal to than M
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // Docs don't mention if dialog needs to be put up so opting out of it.
                    activity!!.requestPermissions(
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_CODE
                    )
                    PermissionResult.REQUESTED
                } else // Lower than M
                    PermissionResult.LOWER_API
            } else // We have permission
                PermissionResult.GRANTED
    }

    // handle the permissions from activity onRequestPermissionResult
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray){
        if(requestCode == REQUEST_CODE){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                setupLocationWork()
            }
        }
    }

    // When activity dies, remove references to old activities
    override fun dispose() {
        activity = null
    }
}

enum class PermissionResult {
    REQUESTED,
    GRANTED,
    LOWER_API
}