package com.example.kotlinjetpacktodoapp

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

public class ToDoPreferences(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("com.example.kotlinJetpackTodoApp.ToDoPreferences", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveToDoCount(count: Int) {
        sharedPreferences.edit().putInt("toDoCount", count).apply()
    }

    fun loadToDoCount(): Int {
        return sharedPreferences.getInt("toDoCount", 0)
    }

    fun saveToDoList(taskList: List<TaskItem>) {
        val json = gson.toJson(taskList)
        sharedPreferences.edit().putString("toDoListKey", json).apply()
    }

    fun loadToDoList(): List<TaskItem> {
        val json = sharedPreferences.getString("toDoListKey", null)
        return if (json != null) {
            val type = object : TypeToken<List<TaskItem>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }
}
