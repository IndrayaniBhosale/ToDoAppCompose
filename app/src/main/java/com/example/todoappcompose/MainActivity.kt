package com.example.todoappcompose

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                ToDoApp()
            }
        }
    }
}

@Composable
fun ToDoApp() {
    val todoItems = remember { mutableStateListOf<TodoItem>() }
    var text by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Title
        Text(
            text = "TODO List",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Input Row
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text("Enter the task name") },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                singleLine = true,
                shape = MaterialTheme.shapes.medium
            )
            Button(
                onClick = {
                    if (text.isNotBlank()) {
                        todoItems.add(
                            TodoItem(id = todoItems.size, task = text.trim())
                        )
                        text = ""
                    } else {
                        Toast.makeText(context, "Please enter a task!", Toast.LENGTH_SHORT).show()
                    }
                },
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Add")
            }
        }

        Spacer(Modifier.height(24.dp))

        // Active Items Section
        Text("Items", style = MaterialTheme.typography.titleMedium)
        if (todoItems.any { !it.isCompleted }) {
            todoItems.filter { !it.isCompleted }.forEach { item ->
                TodoRow(item,
                    onToggle = {
                        val index = todoItems.indexOf(item)
                        if (index != -1) {
                            todoItems[index] = item.copy(isCompleted = !item.isCompleted)
                        }
                    },
                    onDelete = {
                        val index = todoItems.indexOf(item)
                        if (index != -1) {
                            todoItems.removeAt(index)
                        }
                    }
                )
            }
        } else {
            Text(
                "No items yet",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(8.dp)
            )
        }

        Spacer(Modifier.height(24.dp))

        // Completed Items Section
        Text("Completed Items", style = MaterialTheme.typography.titleMedium)
        if (todoItems.any { it.isCompleted }) {
            todoItems.filter { it.isCompleted }.forEach { item ->
                TodoRow(item,
                    onToggle = {
                        val index = todoItems.indexOf(item)
                        if (index != -1) {
                            todoItems[index] = item.copy(isCompleted = !item.isCompleted)
                        }
                    },
                    onDelete = {
                        val index = todoItems.indexOf(item)
                        if (index != -1) {
                            todoItems.removeAt(index)
                        }
                    }
                )
            }
        } else {
            Text(
                "No completed items",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun TodoRow(
    item: TodoItem,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            Checkbox(
                checked = item.isCompleted,
                onCheckedChange = { onToggle() }
            )
            Text(
                text = item.task,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp),
                style = MaterialTheme.typography.bodyLarge,
                textDecoration = if (item.isCompleted) TextDecoration.LineThrough else null
            )
            IconButton(onClick = { onDelete() }) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
