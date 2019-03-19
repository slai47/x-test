package arms.slai.com.xtest.work

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf

class LocationJob(context: Context, workerParams : WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        var lat = 0L
        var long = 0L



        val outputData = workDataOf("lat" to lat, "long" to long)

        return Result.Success(outputData)
    }
}