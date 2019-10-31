@file:Suppress("unused")

package tech.bkdevelopment.eindhovencityexperience.presentation.splash

import android.app.Activity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface SplashActivityModule {

    @ContributesAndroidInjector(modules = [Bindings::class])
    fun splashActivity(): SplashActivity

    @Module
    interface Bindings {

        @Binds
        fun bindActivity(splashActivity: SplashActivity): Activity

        @Binds
        fun bindPresenter(splashPresenter: SplashPresenter): SplashContract.Presenter

        @Binds
        fun bindNavigator(splashNavigator: SplashNavigator): SplashContract.Navigator
    }
}