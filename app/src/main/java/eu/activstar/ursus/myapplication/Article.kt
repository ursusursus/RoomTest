package eu.activstar.ursus.myapplication

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by Vlastimil Brečka (www.vlastimilbrecka.sk)
 * on 16-May-18.
 */
@Entity(tableName = "article")
data class Article(
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long,
        @ColumnInfo(name = "title") val title: String,
        @ColumnInfo(name = "desc") val desc: String,
        @ColumnInfo(name = "imageUrl") val imageUrl: String,
        @ColumnInfo(name = "url") val url: String,
        @ColumnInfo(name = "addedAt") val addedAt: Long = System.currentTimeMillis()
)