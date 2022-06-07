package agstack.gramophone.data.local

import agstack.gramophone.data.model.User
import agstack.gramophone.di.ApplicationScope
import androidx.room.Database
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [User::class], version = 1)
abstract class GramoAppDatabase : RoomDatabase() {

    abstract fun getMovieAppDao(): GramoAppDao

    class Callback @Inject constructor(
        private val database: Provider<GramoAppDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback()
}