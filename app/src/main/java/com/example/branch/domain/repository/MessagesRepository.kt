package com.example.branch.domain.repository

import com.example.branch.domain.model.MessagesDtoItem
import com.example.branch.domain.model.MessagesEntity
import com.example.branch.domain.model.ThreadDto
import kotlinx.coroutines.flow.Flow

interface MessagesRepository {
    suspend fun getAllRemoteMessages(): ArrayList<MessagesDtoItem>

    suspend fun saveMessages(messages: List<MessagesEntity>)

    suspend fun createThreadMessage(threadDto: ThreadDto): MessagesDtoItem

    suspend fun saveMessage(messagesEntity: MessagesEntity)

    fun getAllLocalMessages(): Flow<List<MessagesEntity>>

    fun getAllThreadMessages(threadId: Int): Flow<List<MessagesEntity>>

}