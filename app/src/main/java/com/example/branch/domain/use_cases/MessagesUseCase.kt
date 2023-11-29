package com.example.branch.domain.use_cases

import com.example.branch.common.Resources
import com.example.branch.domain.model.MessagesEntity
import com.example.branch.domain.model.messageDtoToEntity
import com.example.branch.domain.repository.MessagesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import okio.IOException
import javax.inject.Inject

class MessagesUseCase @Inject constructor(
    private val repository: MessagesRepository
) {

    operator fun invoke(): Flow<Resources<List<MessagesEntity>>> = flow {
        try {
            val messageResponse = repository.getAllRemoteMessages()
            repository.saveMessages(messageResponse.map { message ->
                messageDtoToEntity(
                    message
                )
            })

            val x = repository.getAllLocalMessages().onEach { messages ->
                emit(Resources.Success(data = messages))
            }.collect()
        } catch (e: IOException) {
            emit(Resources.Error(e.localizedMessage ?: "No internet connection"))
        } catch (e: Exception) {
            emit(Resources.Error(message = "An error occurred"))
        }
    }
}