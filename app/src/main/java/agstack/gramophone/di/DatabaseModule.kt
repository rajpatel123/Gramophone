package agstack.gramophone.di

import agstack.gramophone.data.local.MovieAppDao
import agstack.gramophone.data.local.MovieAppDatabase
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
    fun provideDatabase(application: Application, callback: MovieAppDatabase.Callback): MovieAppDatabase{
        return Room.databaseBuilder(application, MovieAppDatabase::class.java, "alpha_database")
            .fallbackToDestructiveMigration()
            .addCallback(callback)
            .build()
    }

    @Provides
    fun provideMovieAppDao(db: MovieAppDatabase): MovieAppDao {
        return db.getMovieAppDao()
    }
}