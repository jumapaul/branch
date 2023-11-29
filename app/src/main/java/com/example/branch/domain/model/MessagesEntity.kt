package com.example.branch.domain.model

import android.icu.text.SimpleDateFormat
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@Entity("messages")
data class MessagesEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val agent_id: String?,
    val body: String?,
    val thread_id: Int?,
    val timestamp: Long,
    val user_id: String
)
fun messageDtoToEntity(message: MessagesDtoItem): MessagesEntity {
    return MessagesEntity(
        message.id,
        message.agent_id,
        message.body,
        message.thread_id,
        dateToTimeStamp(message.timestamp),
        message.user_id
    )
}


fun dateToTimeStamp(dateString: String): Long {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    return dateFormat.parse(dateString).time
}

fun timestampToDate(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("dd, MMMM yyyy hh:mm a", Locale.getDefault())
    return dateFormat.format(Date(timestamp))
}