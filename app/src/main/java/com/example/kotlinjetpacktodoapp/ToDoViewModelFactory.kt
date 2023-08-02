package com.example.kotlinjetpacktodoapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ToDoViewModelFactory(private val toDoPreferences: ToDoPreferences) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ToDoViewModel(toDoPreferences) as T
    }
}
