package com.example.semfour.di

import android.content.Context
import androidx.work.WorkManager
import com.example.semfour.data.remote.DriveService
import com.example.semfour.data.remote.GoogleAuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGoogleAuthService(@ApplicationContext context: Context): GoogleAuthService =
        GoogleAuthService(context)

    @Provides
    @Singleton
    fun provideDriveService(): DriveService = DriveService()

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager =
        WorkManager.getInstance(context)
}
