@file:Suppress("unused")

package tech.bkdevelopment.eindhovencityexperience.presentation.tour.tourdetail

import android.app.Activity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface TourDetailActivityModule {

    @ContributesAndroidInjector(modules = [Bindings::class])
    fun tourDetailActivity(): TourDetailActivity

    @Module
    interface Bindings {

        @Binds
        fun bindActivity(tourDetailActivity: TourDetailActivity): Activity

        @Binds
        fun bindView(tourDetailActivity: TourDetailActivity): TourDetailContract.View

        @Binds
        fun bindPresenter(tourDetailPresenter: TourDetailPresenter): TourDetailContract.Presenter

        @Binds
        fun bindNavigator(tourDetailNavigator: TourDetailNavigator): TourDetailContract.Navigator
    }
}