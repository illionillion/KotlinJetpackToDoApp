package com.example.kotlinjetpacktodoapp
import android.content.Context

public class ToDoPreferences(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("com.example.kotlinsimpletodoapp.TaskPreferences", Context.MODE_PRIVATE)

    fun saveToDoList(taskList: MutableList<TaskItem>) {
        val json = StringBuilder()
        for (taskItem in taskList) {
            json.append("${taskItem.id},${taskItem.taskName},${taskItem.isCompleted}|")
        }
        sharedPreferences.edit().putString("taskListKey", json.toString()).apply()
    }

    fun getToDoList(): MutableList<TaskItem> {
        val taskList = mutableListOf<TaskItem>()
        val json = sharedPreferences.getString("taskListKey", null)
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