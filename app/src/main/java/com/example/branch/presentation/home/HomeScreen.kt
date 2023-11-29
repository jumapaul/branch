package com.example.branch.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.branch.domain.model.MessagesEntity
import com.example.branch.domain.model.timestampToDate
import com.example.branch.presentation.home.viewmodel.MessagesViewModel
import com.example.branch.presentation.navigation.NavigationRoutes

@Composable
fun HomeScreen(
    messagesViewModel: MessagesViewModel = hiltViewModel(),
    navController: NavController,
) {
    val messages: Map<Int, List<MessagesEntity>> = messagesViewModel.messages.value.messages
        .toMutableList()
        .groupBy { it.thread_id!! }
        .toMutableMap()

    Box(modifier = Modifier.fillMaxSize()) {
        if (messagesViewModel.messages.value.isLoading) {
            CircularProgressIndicator(
                strokeWidth = 2.dp,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    count = messages.size,
                    itemContent = { index ->
                        val threadMessages =
                            messages[index + 1]?.sortedByDescending { it.timestamp }
                        MessagesCard(
                            messagesEntity = threadMessages?.first(),
                            onView = { threadId ->
                                navController.navigate(NavigationRoutes.ChatScreen.route + "/" + threadId)
                            })
                    })
            }
        }
    }
}

@Composable
fun MessagesCard(messagesEntity: MessagesEntity?, onView: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(16.dp)
            ) {
                Text(text = "ID: ${messagesEntity?.id}")
                Text(text = "Agent ID: ${messagesEntity?.agent_id ?: "-"}")
                Text(text = "Body: ${messagesEntity?.body ?: "-"}")
                Text(text = "Thread ID: ${messagesEntity?.thread_id ?: "-"}")
                Text(text = "Timestamp: ${timestampToDate(messagesEntity?.timestamp ?: 1686196800000)}")
                Text(text = "User ID: ${messagesEntity?.user_id}")
            }
            Button(onClick = {
                if (messagesEntity?.thread_id != null) {
                    onView(messagesEntity.thread_id)
                }
            }) {
                Text(text = "Reply")
            }
        }
    }
}


