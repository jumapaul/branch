package com.example.branch.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.branch.domain.model.MessagesEntity

@Database(entities = [MessagesEntity::class], version = 3, exportSchema = false)
abstract class MessageDatabase : RoomDatabase() {
    abstract val messagesDao: MessagesDao
}