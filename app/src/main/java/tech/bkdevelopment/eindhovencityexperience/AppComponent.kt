package tech.bkdevelopment.eindhovencityexperience

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import tech.bkdevelopment.eindhovencityexperience.data.sync.contentful.ContentfulModule
import tech.bkdevelopment.eindhovencityexperience.presentation.notification.ContinuousNotificationService
import tech.bkdevelopment.eindhovencityexperience.presentation.notification.ContinuousNotificationServiceModule
import tech.bkdevelopment.eindhovencityexperience.presentation.splash.SplashActivityModule
import tech.bkdevelopment.eindhovencityexperience.presentation.tour.tourlist.TourListActivityModule
import tech.bkdevelopment.eindhovencityexperience.presentation.tour.tourdetail.TourDetailActivityModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        SplashActivityModule::class,
        TourListActivityModule::class,
        ContentfulModule::class,
        TourDetailActivityModule::class,
        ContinuousNotificationServiceModule::class
    ]
)
interface AppComponent : AndroidInjector<EceApplication> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<EceApplication>() {

        abstract fun appModule(appModule: AppModule): Builder
    }
}