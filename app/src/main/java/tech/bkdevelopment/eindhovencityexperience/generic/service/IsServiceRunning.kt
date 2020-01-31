package tech.bkdevelopment.eindhovencityexperience.generic.service

import android.app.ActivityManager
import android.content.Context
import tech.bkdevelopment.eindhovencityexperience.generic.dagger.AppContext
import javax.inject.Inject

class IsServiceRunning @Inject constructor(@AppContext private val context: Context) {

    operator fun invoke(serviceClass: Class<*>): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }
}