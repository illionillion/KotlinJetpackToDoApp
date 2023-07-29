package com.example.kotlinjetpacktodoapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kotlinjetpacktodoapp.ui.theme.KotlinJetpackToDoAppTheme

data class TaskItem (val id: Int, val taskName: String, val isCompleted: Boolean)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KotlinJetpackToDoAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background,
                ) {
                    ToDoList()
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDoList () {

    var taskId by remember { mutableStateOf(1) }
    var taskItems by remember { mutableStateOf(listOf<TaskItem>()) }

    fun onTaskAdd() {
        taskItems = taskItems.plus(TaskItem(taskId, taskName = "", isCompleted = false))
        taskId++
    }

    fun onTaskNameChange(taskId: Int, newName: String) {
        taskItems = taskItems.map { task ->
            if (task.id == taskId) task.copy(taskName = newName)
            else task
        }
    }

    fun onTaskCompletionToggle(taskId: Int, isCompleted: Boolean) {
        taskItems = taskItems.map { task ->
            if (task.id == taskId) task.copy(isCompleted = isCompleted)
            else task
        }
    }

    fun onTaskDelete(taskId: Int) {
        taskItems = taskItems.filter { task -> task.id != taskId }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "ToDoアプリ")
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color.Cyan
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onTaskAdd() }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "追加",
                )
            }
        },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        ) {
            LazyColumn(
                modifier = Modifier
                .fillMaxSize(),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(taskItems) { task ->
                    ToDoItem(
                        taskName = task.taskName,
                        isCompleted = task.isCompleted,
                        onTaskNameChange = { newName -> onTaskNameChange(task.id, newName) },
                        onCompletionToggle = { isCompleted -> onTaskCompletionToggle(task.id, isCompleted) },
                        onTaskDelete = { onTaskDelete(task.id) }
                    )
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDoItem (
    taskName: String,
    isCompleted: Boolean,
    onTaskNameChange: (String) -> Unit,
    onCompletionToggle: (Boolean) -> Unit,
    onTaskDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        var text by remember { mutableStateOf(taskName) }
        OutlinedTextField(
            value = taskName,
            onValueChange = {
                text = it
                onTaskNameChange(it)
            },
            modifier = Modifier
                .weight(1f),
            placeholder = { Text(text = "新しいタスク") }
        )

        Button(
            onClick = {
                onCompletionToggle(!isCompleted)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = if(isCompleted) Color.DarkGray else Color.Green
            )
        ) {
            Text(text = if(isCompleted) "完了済み" else "完了", fontWeight = FontWeight(800))
        }

        Button(
            onClick = {
                onTaskDelete()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red
            )
        ) {
            Text(text = "削除", fontWeight = FontWeight(800))
        }

    }
}

@Preview(showBackground = true)
@Composable
fun ToDoItemPreview() {
    ToDoItem(
        taskName = "勉強",
        isCompleted = false,
        onTaskDelete = {},
        onCompletionToggle = { _ -> },
        onTaskNameChange = {_ -> }
    )
}

@Preview(showBackground = true)
@Composable
fun ToDoItemListPreview() {
    ToDoList()
}