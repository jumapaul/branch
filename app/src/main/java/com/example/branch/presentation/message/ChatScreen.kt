package com.example.branch.presentation.message

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.branch.domain.model.MessagesEntity
import com.example.branch.domain.model.ThreadDto
import com.example.branch.presentation.message.viewmodel.ChatScreenViewModel

@Composable
fun ChatScreen(
    chatViewModel: ChatScreenViewModel = hiltViewModel(),
    threadId: Int
) {
    chatViewModel.getThreadMessages(threadId)

    Column {
        ChatMessageSection(messages = chatViewModel.chatState.value.messages)
        ReplySection(onMessageSent = { message ->
            chatViewModel.createThreadMessage(ThreadDto(message, threadId))
        })
    }
}

@Composable
fun ChatMessageSection(messages: List<MessagesEntity>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.9f)
            .padding(8.dp)
    ) {
        LazyColumn {
            items(count = messages.size) { index ->
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = if (messages[index].agent_id != null) Alignment.BottomEnd else Alignment.BottomStart
                ) {
                    MessageBubble(message = messages[index])
                }
            }
        }
    }
}

@Composable
fun MessageBubble(message: MessagesEntity) {
    val bubbleColor =
        if (message.agent_id == null) MaterialTheme.colorScheme.primary else Color.Gray
    val textColor = if (message.agent_id == null) Color.White else Color.Black

    Card(
        colors = CardDefaults.cardColors(
            containerColor = bubbleColor
        ),
        modifier = Modifier.padding(8.dp)
    ) {
        Text(
            text = message.body.orEmpty(),
            color = textColor,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun ReplySection(onMessageSent: (String) -> Unit) {
    var messageText by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp)
            .fillMaxHeight(1f),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = messageText,
            onValueChange = {
                messageText = it
            },
            modifier = Modifier
                .weight(1f)
                .padding(6.dp)
                .fillMaxHeight(1f),
            maxLines = 10,
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .background(color = Color.Gray.copy(alpha = 0.2f))
                        .clip(RoundedCornerShape(10.dp)),
                ) {
                    if (messageText.isEmpty()) {
                        Text(
                            text = "Reply...",
                            color = Color.Black
                        )
                    }
                    innerTextField()
                }
            }
        )

        IconButton(
            onClick = {
                if (messageText.isNotBlank()) {
                    onMessageSent(messageText)
                    messageText = ""
                }
            }
        ) {
            Icon(Icons.Default.Send, contentDescription = "Send")
        }
    }
}

