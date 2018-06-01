package eu.activstar.ursus.myapplication

import android.arch.paging.DataSource
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.database.Cursor
import io.reactivex.Flowable

/**
 * Created by Vlastimil Brečka (www.vlastimilbrecka.sk)
 * on 16-May-18.
 */
@Dao
interface ArticleDao {
    @Insert
    fun insert(article: Article)

    @Delete
    fun delete(article: Article)

    @Query("DELETE FROM article")
    fun deleteAll()


    @Query("SELECT * FROM article ORDER BY addedAt DESC LIMIT :pageSize")
    fun findAll(pageSize: Int): Flowable<List<Article>>

    @Query("SELECT * FROM article")
    fun findAll2(): Cursor

    @Query("SELECT * FROM article")
    fun findAll3(): DataSource.Factory<Int, Article>

    @Query("""
        SELECT *
        FROM (
            SELECT *
            FROM article
            ORDER BY id DESC LIMIT 3)
        WHERE title="title1"
        ORDER BY id DESC""")
    fun findFoo(): Flowable<List<Article>>

    @Query("SELECT * FROM article WHERE id=:id")
    fun findById(id: Long): Flowable<List<Article>>

    @Query("UPDATE article SET addedAt=random()")
    fun updateTimestampsRandom()
}