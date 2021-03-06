package dev.ugurcan.githubrepos.domain.repos.db

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.ugurcan.githubrepos.data.Repo

@Database(
    entities = [Repo::class],
    version = 3,
    exportSchema = false
)
abstract class RepoDb : RoomDatabase() {
    abstract fun repoDao(): RepoDao
}
