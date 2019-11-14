@file:Suppress("unused")

package tech.bkdevelopment.eindhovencityexperience.presentation.map

import android.app.Activity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface MapActivityModule {

    @ContributesAndroidInjector(modules = [Bindings::class])
    fun mapActivity(): MapActivity

    @Module
    interface Bindings {

        @Binds
        fun bindActivity(mapActivity: MapActivity): Activity

        @Binds
        fun bindView(mapActivity: MapActivity): MapContract.View

        @Binds
        fun bindPresenter(mapPresenter: MapPresenter): MapContract.Presenter

        @Binds
        fun bindNavigator(mapNavigator: MapNavigator): MapContract.Navigator
    }
}