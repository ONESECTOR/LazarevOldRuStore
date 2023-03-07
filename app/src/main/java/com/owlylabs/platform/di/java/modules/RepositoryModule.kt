package com.owlylabs.platform.di.java.modules

import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.room.Room
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.owlylabs.platform.PlatformApplication
import com.owlylabs.platform.constants.DBConstants
import com.owlylabs.platform.model.repository.local.AbstractLocalRepository
import com.owlylabs.platform.model.repository.local.db.LocalRepositoryImpl
import com.owlylabs.platform.model.repository.local.db.RoomClientPlatform
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideTabsRepository(roomClientPlatform: RoomClientPlatform, rxSharedPreferences: RxSharedPreferences) : AbstractLocalRepository {
        return LocalRepositoryImpl(roomClientPlatform, rxSharedPreferences)
    }

    @Provides
    fun provideRoom(application: PlatformApplication): RoomClientPlatform {
        return Room
            .databaseBuilder(
                application,
                RoomClientPlatform::class.java,
                DBConstants.dbName
            )
            .addMigrations(RoomClientPlatform.MIGRATION_1_2)
            .addMigrations(RoomClientPlatform.MIGRATION_2_3)
            .build()
    }

    @Provides
    fun provideRxSharedPreferences(application: PlatformApplication): RxSharedPreferences {
        val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(application)
        return RxSharedPreferences.create(preferences)
    }
}