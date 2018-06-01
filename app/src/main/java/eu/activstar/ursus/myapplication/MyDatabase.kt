package eu.activstar.ursus.myapplication

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

/**
 * Created by Vlastimil Brečka (www.vlastimilbrecka.sk)
 * on 16-May-18.
 */
@Database(entities = arrayOf(Article::class), version = 1)
abstract class MyDatabase : RoomDatabase() {

    abstract fun articleDao(): ArticleDao

}