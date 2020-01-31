@file:Suppress("unused")

package tech.bkdevelopment.eindhovencityexperience.presentation.tour.tourlist

import android.app.Activity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface TourListActivityModule {

    @ContributesAndroidInjector(modules = [Bindings::class])
    fun tourListActivity(): TourListActivity

    @Module
    interface Bindings {

        @Binds
        fun bindActivity(tourListActivity: TourListActivity): Activity

        @Binds
        fun bindView(tourListActivity: TourListActivity): TourListContract.View

        @Binds
        fun bindPresenter(tourListPresenter: TourListPresenter): TourListContract.Presenter

        @Binds
        fun bindNavigator(tourListNavigator: TourListNavigator): TourListContract.Navigator
    }
}