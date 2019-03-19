package arms.slai.com.xtest.work

import androidx.work.*
import java.util.concurrent.TimeUnit

/**
 * Consolidating code down for building jobs
 *
 */
class WorkBuilder {

    companion object {

        // helper method to always add the same constraints
        private fun buildContraints() : Constraints {
            return Constraints.Builder()
                .setRequiresCharging(false)
                .setRequiresBatteryNotLow(true)
                .build()
        }

        // Build out the 1 hour delayed job
        fun buildPeriodicJob(): PeriodicWorkRequest {
            return PeriodicWorkRequestBuilder<LocationJob>(1, TimeUnit.HOURS)
                .setConstraints(buildContraints())
                .addTag("LocationJob")
                .build()
        }

        // build out a one time job with a 1m delay
        fun buildOneTimeJob(): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<LocationJob>()
                .setConstraints(buildContraints())
                .setInitialDelay(1, TimeUnit.MINUTES)
                .addTag("LocationJob")
                .build()
        }
    }
}