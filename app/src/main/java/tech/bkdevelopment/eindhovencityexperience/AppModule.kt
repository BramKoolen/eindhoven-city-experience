package tech.bkdevelopment.eindhovencityexperience

import android.app.Application
import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Binds
import dagger.Module
import dagger.Provides
import tech.bkdevelopment.eindhovencityexperience.generic.dagger.AppContext
import tech.bkdevelopment.eindhovencityexperience.data.generic.dagger.DataContext
import tech.bkdevelopment.eindhovencityexperience.data.generic.room.RoomEceDatabase
import tech.bkdevelopment.eindhovencityexperience.data.location.AndroidLocationProvider
import tech.bkdevelopment.eindhovencityexperience.data.tour.ContentfulRoomTourRepository
import tech.bkdevelopment.eindhovencityexperience.domain.location.LocationProvider
import tech.bkdevelopment.eindhovencityexperience.domain.tour.data.TourRepository
import javax.inject.Singleton

@Module(includes = [AppModule.Bindings::class])
class AppModule {

    @Provides
    @DataContext
    fun provideDataContext(application: EceApplication): Context = application

    @Singleton
    @Provides
    fun provideDatabase(@DataContext context: Context): RoomEceDatabase {
        return RoomEceDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(@DataContext context: Context): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    @Module
    interface Bindings{

        @Binds
        @AppContext
        fun bindApplicationContext(eceApplication: EceApplication): Context

        @Binds
        fun bindApplication(eceApplication: EceApplication): Application

        @Binds
        fun bindTourRepository(contentfulRoomTourRepository: ContentfulRoomTourRepository): TourRepository

        @Binds
        fun bindLocationProvider(androidLocationProvider: AndroidLocationProvider): LocationProvider
    }
}