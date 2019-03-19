package arms.slai.com.xtest.interfaces

import arms.slai.com.xtest.PermissionResult

interface IMainPresenter {

    fun checkLocationPermissionAndRequest() : PermissionResult

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)

    fun setupLocationWork()

    fun dispose()
}