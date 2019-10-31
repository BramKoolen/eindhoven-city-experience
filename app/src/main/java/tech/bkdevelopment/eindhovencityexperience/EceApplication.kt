package tech.bkdevelopment.eindhovencityexperience

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import tech.bkdevelopment.eindhovencityexperience.domain.sync.SyncGenericContentData
import timber.log.Timber
import javax.inject.Inject

class EceApplication : DaggerApplication() {

    @Inject
    lateinit var syncGenericContentData: SyncGenericContentData

    private var disposable: Disposable? = null

    override fun onCreate() {
        super.onCreate()
        initTimber()
        syncContentfulData()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().create(this)
    }

    private fun syncContentfulData() {
        disposable?.dispose()
        disposable = syncGenericContentData.execute()
            .subscribeOn(Schedulers.io())
            .subscribe(
                { Timber.i("content updated") },
                { Timber.e("content update failed: $it") }
            )
    }
}