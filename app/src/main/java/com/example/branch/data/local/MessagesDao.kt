package com.example.branch.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.branch.domain.model.MessagesEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface MessagesDao {
    @Query("SELECT * FROM messages")
    fun getAllMessages(): Flow<List<MessagesEntity>>

    @Insert
    suspend fun saveMessage(message: MessagesEntity)

    @Query("SELECT * FROM messages WHERE thread_id = :threadId")
    fun getThreadMessages(threadId: Int): Flow<List<MessagesEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMessages(messages: List<MessagesEntity>)
}