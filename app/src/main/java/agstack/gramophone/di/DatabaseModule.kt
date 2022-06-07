package agstack.gramophone.di

import agstack.gramophone.data.local.GramoAppDao
import agstack.gramophone.data.local.GramoAppDatabase
import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application, callback: GramoAppDatabase.Callback): GramoAppDatabase{
        return Room.databaseBuilder(application, GramoAppDatabase::class.java, "alpha_database")
            .fallbackToDestructiveMigration()
            .addCallback(callback)
            .build()
    }

    @Provides
    fun provideMovieAppDao(db: GramoAppDatabase): GramoAppDao {
        return db.getMovieAppDao()
    }
}