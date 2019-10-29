package tech.bkdevelopment.eindhovencityexperience

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import tech.bkdevelopment.eindhovencityexperience.presentation.splash.SplashActivityModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        SplashActivityModule::class
    ]
)
interface AppComponent : AndroidInjector<EceApplication> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<EceApplication>() {

        abstract fun appModule(appModule: AppModule): Builder
    }
}