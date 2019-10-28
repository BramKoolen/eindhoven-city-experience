package tech.bkdevelopment.eindhovencityexperience

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber

class EceApplication : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()
        initTimber()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().create(this)
    }
}