package tech.bkdevelopment.eindhovencityexperience

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
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

    companion object {

        fun createSettingsIntent(context: Context): Intent {
            return Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + context.packageName)
            )
                .addCategory(Intent.CATEGORY_DEFAULT)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }
}