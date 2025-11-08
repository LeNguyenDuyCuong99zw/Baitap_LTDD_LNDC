package com.map.baitapvenha.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TasksResponse(
    val isSuccess: Boolean,
    val message: String,
    val data: List<Task>
)

@JsonClass(generateAdapter = true)
data class TaskResponse(
    val isSuccess: Boolean,
    val message: String,
    val data: Task
)

@JsonClass(generateAdapter = true)
data class Task(
    val id: Int,
    val title: String,
    val description: String?,
    val status: String?,
    val priority: String?,
    val category: String?,
    val dueDate: String?,
    val createdAt: String?,
    val updatedAt: String?,
    val subtasks: List<Subtask> = emptyList(),
    val attachments: List<Attachment> = emptyList(),
    val reminders: List<Reminder> = emptyList(),
    // optional field in detail
    val desImageURL: String? = null
)

@JsonClass(generateAdapter = true)
data class Subtask(
    val id: Int,
    val title: String,
    val isCompleted: Boolean
)

@JsonClass(generateAdapter = true)
data class Attachment(
    val id: Int,
    val fileName: String,
    val fileUrl: String
)

@JsonClass(generateAdapter = true)
data class Reminder(
    val id: Int,
    val time: String,
    val type: String
)
