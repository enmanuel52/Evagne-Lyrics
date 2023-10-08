package com.example.evagnelyrics.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.evagnelyrics.data.database.dao.LyricsDao
import com.example.evagnelyrics.data.database.entities.LyricsEntity

@Database(entities = [LyricsEntity::class], version = 1)
abstract class LyricsDataBase: RoomDatabase() {

    abstract fun getLyricsDao(): LyricsDao
}