package com.example.kotlinjetpacktodoapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ToDoViewModel : ViewModel() {
    var taskId by mutableStateOf(0)
    var taskItems by mutableStateOf(listOf<TaskItem>())

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
}
