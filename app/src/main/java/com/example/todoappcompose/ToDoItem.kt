package com.example.todoappcompose

data class TodoItem(
    val id: Int,
    val task: String,
    val isCompleted: Boolean = false
)
