package arms.slai.com.xtest.work

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.work.*
import arms.slai.com.xtest.BuildConfig
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import java.util.concurrent.TimeUnit

/**
 * TODO Task #3 comment
 * I selected this method as it prevents using more battery than required if the last location is available.
 * If it isn't available, quickly do a call that just grabs the latest and ends the callbacks.
 *
 *
 */
class LocationJob(context: Context, workerParams : WorkerParameters) : Worker(context, workerParams) {

    val TAG = LocationJob::class.java.simpleName

    // Callback for location requests
    private val callback = object : LocationCallback() {
        // Respond to location updates
        override fun onLocationResult(locations: LocationResult?) {
            super.onLocationResult(locations)
            locations ?: return

            val location = locations.locations.firstOrNull()
            if(location != null) {
                toastLocation(location)
                fusedLocationClient.removeLocationUpdates(this)
            }
        }
    }

    private val fusedLocationClient by lazy { LocationServices.getFusedLocationProviderClient(context) }

    // Lifecycle method for Worker
    override fun doWork(): Result {
        Log.d(TAG, "Hey I'm working!!")

        // Check permissions
        grabLocation()

        val outputData = workDataOf()

        setupNextSingleJob()

        return Result.Success(outputData)
    }

    // Check permissions, then find out if the best option/lowest battery usage is available
    private fun grabLocation() {
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // See if location is available and recent
            fusedLocationClient.locationAvailability.addOnSuccessListener {
                Log.d(TAG, "LocationAvailability = ${it.isLocationAvailable}")
                if (it.isLocationAvailable) {
                    grabLastLocation()
                } else {
                    requestLocationUpdates()
                }
            }
        }
    }

    // Last location isn't avaiable, request location update
    private fun requestLocationUpdates() {
        // Ok its not recent, lets update it
        val request = LocationRequest()
        request.apply {
            interval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            fastestInterval = 2000
        }
        // Fire and forget about it as the callback will handle it
        fusedLocationClient.requestLocationUpdates(request, callback, null)
    }

    // grab last Location information as its available
    private fun grabLastLocation() {
        // Its recent, lets use it
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            toastLocation(location)
        }
        fusedLocationClient.lastLocation.addOnFailureListener {
            Toast.makeText(applicationContext, "Location failed ${it.message}", Toast.LENGTH_SHORT).show()
            // probably want to request location updates here but I think scope of task is handled
        }
        //TODO Depending on what needs to be saved, list or just one I would choose between just using SharedPreferences or SQLite.
    }

    // method name
    private fun toastLocation(location: Location?) {
        Toast.makeText(applicationContext, location?.toString(), Toast.LENGTH_LONG).show()
    }

    // Work manager has a 15m min, debug we need it shorter so this
    private fun setupNextSingleJob() {
        val debug = BuildConfig.BUILD_TYPE == "debug"
        if(debug)
            WorkManager.getInstance().enqueue(WorkBuilder.buildOneTimeJob())
    }
}