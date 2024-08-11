package com.cs.lista_de_tareas

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

object TaskManager {
    private const val FILE_NAME = "tasks.json"

    fun loadTasks(context: Context): List<Task> {
        val file = File(context.filesDir, FILE_NAME)
        if (!file.exists()) {
            return emptyList()
        }

        val json = file.readText()
        val taskType = object : TypeToken<List<Task>>() {}.type
        return Gson().fromJson(json, taskType)
    }

    fun saveTasks(context: Context, tasks: List<Task>) {
        val json = Gson().toJson(tasks)
        val file = File(context.filesDir, FILE_NAME)
        file.writeText(json)
    }

    fun updateTask(context: Context, task: Task) {
        val tasks = loadTasks(context).toMutableList()
        val index = tasks.indexOfFirst { it.id == task.id }
        if (index != -1) {
            tasks[index] = task
            saveTasks(context, tasks)
        }
    }
}