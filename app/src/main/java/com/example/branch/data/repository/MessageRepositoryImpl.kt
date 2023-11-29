package com.example.branch.data.repository

import com.example.branch.data.local.MessagesDao
import com.example.branch.data.remote.BranchApiService
import com.example.branch.domain.model.MessagesDtoItem
import com.example.branch.domain.model.MessagesEntity
import com.example.branch.domain.model.ThreadDto
import com.example.branch.domain.repository.MessagesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val api: BranchApiService,
    private val dao: MessagesDao
) : MessagesRepository {
    override suspend fun getAllRemoteMessages(): ArrayList<MessagesDtoItem> {
        return api.getMessages()
    }

    override suspend fun saveMessages(messages: List<MessagesEntity>) {
        dao.saveMessages(messages)
    }

    override suspend fun createThreadMessage(threadDto: ThreadDto): MessagesDtoItem {
        return api.createMessage(threadDto)
    }

    override suspend fun saveMessage(messagesEntity: MessagesEntity) {
        dao.saveMessage(messagesEntity)
    }

    override fun getAllLocalMessages(): Flow<List<MessagesEntity>> {
        return dao.getAllMessages()
    }

    override fun getAllThreadMessages(threadId: Int): Flow<List<MessagesEntity>> {
        return dao.getThreadMessages(threadId)
    }
}