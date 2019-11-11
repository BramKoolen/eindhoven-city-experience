@file:Suppress("unused")

package tech.bkdevelopment.eindhovencityexperience.presentation.notification

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ContinuousNotificationServiceModule {

    @ContributesAndroidInjector
    fun continuousNotificationService(): ContinuousNotificationService
}