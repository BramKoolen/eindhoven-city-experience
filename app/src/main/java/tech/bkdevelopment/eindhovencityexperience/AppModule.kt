package tech.bkdevelopment.eindhovencityexperience

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module
import tech.bkdevelopment.eindhovencityexperience.generic.dagger.AppContext
import tech.bkdevelopment.eindhovencityexperience.generic.dagger.DataContext

@Module(includes = [AppModule.Bindings::class])
class AppModule {

    @Module
    interface Bindings{

        @Binds
        @AppContext
        fun bindApplicationContext(eceApplication: EceApplication): Context

        @Binds
        @DataContext
        fun bindDataApplicationContext(eceApplication: EceApplication): Context

        @Binds
        fun bindApplication(eceApplication: EceApplication): Application
    }
}