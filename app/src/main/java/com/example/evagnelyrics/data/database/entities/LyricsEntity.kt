package com.example.evagnelyrics.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.evagnelyrics.domain.model.Lyric

@Entity(tableName = LYRICS_TABLE_NAME)
data class LyricsEntity(
    @PrimaryKey
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "letter") val letter: String,
    @ColumnInfo(name = "favorite") var favorite: Boolean = false,
    @ColumnInfo(name = "album") var album: String = "Singles",
) {
    constructor(lyric: Lyric) : this(
        lyric.title, lyric.letter, lyric.favorite, lyric.album
    )

    fun toDomain() = Lyric(title, letter, favorite, album)
}

const val LYRICS_TABLE_NAME = "lyrics_table"