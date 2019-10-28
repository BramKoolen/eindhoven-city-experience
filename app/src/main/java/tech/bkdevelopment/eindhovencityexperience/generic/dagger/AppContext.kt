package tech.bkdevelopment.eindhovencityexperience.generic.dagger

import android.content.Context
import javax.inject.Qualifier

/**
 *
 * Annotation used to mark Application [Context] instances
 */
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class AppContext