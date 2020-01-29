package dev.ugurcan.githubrepos.domain.repos.db

import androidx.room.*
import dev.ugurcan.githubrepos.data.Repo
import io.reactivex.Single

@Dao
interface RepoDao {
    @Query("SELECT * FROM repo")
    fun getAll(): Single<List<Repo>>

    @Query("SELECT * FROM repo WHERE id = :id")
    fun getById(id: Int): Single<Repo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(repo: Repo): Single<Long>

    @Delete
    fun delete(repo: Repo): Single<Int>
}
