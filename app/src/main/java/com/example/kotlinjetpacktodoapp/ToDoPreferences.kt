package com.example.kotlinjetpacktodoapp
import android.content.Context

public class ToDoPreferences(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("com.example.kotlinJetpackTodoApp.ToDoPreferences", Context.MODE_PRIVATE)

    fun saveToDoCount(count: Int) {
        sharedPreferences.edit().putInt("toDoCount", count).apply()
    }
    fun loadToDoCount(): Int {
        return sharedPreferences.getInt("toDoCount", 0)
    }

    fun saveToDoList(taskList: List<TaskItem>) {
        val json = StringBuilder()
        for (taskItem in taskList) {
            json.append("${taskItem.id},${taskItem.taskName},${taskItem.isCompleted}|")
        }
        sharedPreferences.edit().putString("toDoListKey", json.toString()).apply()
    }

    fun loadToDoList(): List<TaskItem> {
        val taskList = mutableListOf<TaskItem>()
        val json = sharedPreferences.getString("toDoListKey", null)
        json?.split("|")?.forEach { item ->
            item.split(",")?.let { values ->
                if (values.size == 3) {
                    val taskId = values[0]
                    val taskName = values[1]
                    val isCompleted = values[2].toBoolean()
                    taskList.add(TaskItem(taskId.toInt(), taskName, isCompleted))
                }
            }
        }
        return taskList
    }
}