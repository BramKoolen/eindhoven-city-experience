@file:Suppress("unused")

package tech.bkdevelopment.eindhovencityexperience.presentation.story

import android.app.Activity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface StoryActivityModule {

    @ContributesAndroidInjector(modules = [Bindings::class])
    fun tourListActivity(): StoryActivity

    @Module
    interface Bindings {

        @Binds
        fun bindActivity(storyActivity: StoryActivity): Activity

        @Binds
        fun bindView(storyActivity: StoryActivity): StoryContract.View

        @Binds
        fun bindPresenter(storyPresenter: StoryPresenter): StoryContract.Presenter

        @Binds
        fun bindNavigator(storyNavigator: StoryNavigator): StoryContract.Navigator
    }
}