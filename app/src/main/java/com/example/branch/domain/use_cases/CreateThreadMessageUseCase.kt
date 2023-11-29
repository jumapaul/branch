package com.example.branch.domain.use_cases

import com.example.branch.common.Resources
import com.example.branch.domain.model.MessagesEntity
import com.example.branch.domain.model.ThreadDto
import com.example.branch.domain.model.messageDtoToEntity
import com.example.branch.domain.repository.MessagesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import java.io.IOException
import javax.inject.Inject

class CreateThreadMessageUseCase @Inject constructor(
    private val repository: MessagesRepository
) {
    operator fun invoke(threadDto: ThreadDto): Flow<Resources<List<MessagesEntity>>> = flow {
        try {
            val threadResponse = repository.createThreadMessage(threadDto)
            repository.saveMessage(messageDtoToEntity(threadResponse))
            repository.getAllThreadMessages(threadDto.thread_id).onEach { messages ->
                emit(Resources.Success(data = messages))
            }.collect()
        } catch (e: IOException) {
            emit(Resources.Error("Please check internet connection"))
        } catch (e: Exception) {
            emit(Resources.Error(message = "Something went wrong"))
        }
    }
}