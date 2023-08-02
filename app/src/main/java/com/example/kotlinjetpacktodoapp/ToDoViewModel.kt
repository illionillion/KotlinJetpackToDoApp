package com.example.kotlinjetpacktodoapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ToDoViewModel(
    private val toDoPreference: ToDoPreferences
) : ViewModel() {
    private var taskId by mutableStateOf(toDoPreference.loadToDoCount())
    var taskItems by mutableStateOf(toDoPreference.loadToDoList())

    fun onTaskAdd() {
        taskItems = taskItems.plus(TaskItem(taskId, taskName = "", isCompleted = false))
        taskId++
        updatePreference()
    }

    fun onTaskNameChange(taskId: Int, newName: String) {
        taskItems = taskItems.map { task ->
            if (task.id == taskId) task.copy(taskName = newName)
            else task
        }
        updatePreference()
    }

    fun onTaskCompletionToggle(taskId: Int, isCompleted: Boolean) {
        taskItems = taskItems.map { task ->
            if (task.id == taskId) task.copy(isCompleted = isCompleted)
            else task
        }
        updatePreference()
    }

    fun onTaskDelete(taskId: Int) {
        taskItems = taskItems.filter { task -> task.id != taskId }
        updatePreference()
    }

    private fun updatePreference () {
        viewModelScope.launch {
            toDoPreference.saveToDoCount(taskId)
            toDoPreference.saveToDoList(taskItems)
        }
    }
}
