package com.example.evagnelyrics.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = LYRICS_TABLE_NAME)
data class LyricsEntity(
    @PrimaryKey
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "letter") val letter: String,
    @ColumnInfo(name = "favorite") var favorite: Boolean = false,
    @ColumnInfo(name = "album") var album: String = "Singles",
)

const val LYRICS_TABLE_NAME = "lyrics_table"