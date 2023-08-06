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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kotlinjetpacktodoapp.ui.theme.KotlinJetpackToDoAppTheme
import androidx.lifecycle.viewmodel.compose.viewModel

data class TaskItem(val id: Int, val taskName: String, val isCompleted: Boolean)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KotlinJetpackToDoAppTheme(
                darkTheme = false
            ) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    ToDoListApp()
                }
            }
        }
    }
}

@Composable
fun ToDoListApp() {
    val context = LocalContext.current
    val toDoPreferences = remember { ToDoPreferences(context) }
    val viewModel: ToDoViewModel = viewModel(factory = ToDoViewModelFactory(toDoPreferences))
    val isOpenDialog = rememberSaveable { mutableStateOf(false) }
    ToDoList(
        isOpenDialog = isOpenDialog,
        taskItems = viewModel.taskItems,
        onTaskAdd = { viewModel.onTaskAdd() },
        onTaskNameChange = { taskId, newName -> viewModel.onTaskNameChange(taskId, newName) },
        onTaskCompletionToggle = { taskId, isCompleted ->
            viewModel.onTaskCompletionToggle(
                taskId,
                isCompleted
            )
        },
        onTaskDelete = { taskId -> viewModel.onTaskDelete(taskId) }
    ) { viewModel.onTaskClear() }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDoList(
    isOpenDialog: MutableState<Boolean>,
    taskItems: List<TaskItem>,
    onTaskAdd: () -> Unit,
    onTaskNameChange: (Int, String) -> Unit,
    onTaskCompletionToggle: (Int, Boolean) -> Unit,
    onTaskDelete: (Int) -> Unit,
    onTaskClear: () -> Unit,
) {


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "ToDoアプリ", color = Color.Black)
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color.Cyan
                )
            )
        },
        floatingActionButton = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {

                FloatingActionButton(onClick = {
                    if (taskItems.isNotEmpty()) isOpenDialog.value = true
                }) {
                    Text(text = "クリア")
                }

                FloatingActionButton(onClick = { onTaskAdd() }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "追加",
                    )
                }
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
                        onCompletionToggle = { isCompleted ->
                            onTaskCompletionToggle(
                                task.id,
                                isCompleted
                            )
                        },
                        onTaskDelete = { onTaskDelete(task.id) }
                    )
                }

            }
        }

        // アラートを表示するためのコンポーネント
        if (isOpenDialog.value) {
            AlertDialog(
                onDismissRequest = { isOpenDialog.value = false },
                title = {
                    Text(text = "タスクをクリアしますか？")
                },
                text = {
                    Text(text = "この操作は元に戻せません。本当にクリアしますか？")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            isOpenDialog.value = false
                            onTaskClear()
                        },
                        colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
                    ) {
                        Text(text = "クリア")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { isOpenDialog.value = false }
                    ) {
                        Text(text = "キャンセル")
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDoItem(
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
        Checkbox(
            checked = isCompleted,
            onCheckedChange = {
                onCompletionToggle(!isCompleted)
            }
        )
        OutlinedTextField(
            value = taskName,
            onValueChange = {
                text = it
                onTaskNameChange(it)
            },
            modifier = Modifier
                .weight(1f),
//            placeholder = { Text(text = "新しいタスク") },
            label = {
                Text(
                    text =
                    if (taskName.isNotEmpty())
                        if (isCompleted) "完了" else "未完了"
                    else
                        "新しいタスク"
                )
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Ascii
            )
        )

//        Button(
//            onClick = {
//                onCompletionToggle(!isCompleted)
//            },
//            colors = ButtonDefaults.buttonColors(
//                containerColor = if (isCompleted) Color.DarkGray else Color.Green
//            )
//        ) {
//            Text(text = if (isCompleted) "完了済み" else "完了", fontWeight = FontWeight(800))
//        }

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
        onCompletionToggle = {},
        onTaskNameChange = {}
    )
}

@Preview(showBackground = true)
@Composable
fun ToDoItemListPreview() {
    val isOpenDialog = remember { mutableStateOf(true) }

    ToDoList(
        taskItems = listOf(
            TaskItem(id = 1, taskName = "勉強", isCompleted = false),
            TaskItem(id = 2, taskName = "買い物", isCompleted = true),
            TaskItem(id = 3, taskName = "運動", isCompleted = false),
        ),
        onTaskNameChange = { _, _ -> },
        onTaskAdd = {},
        onTaskDelete = {},
        onTaskCompletionToggle = { _, _ -> },
        isOpenDialog = isOpenDialog,
    ) {}
}