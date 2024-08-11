package com.cs.lista_de_tareas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import android.os.Handler
import android.os.Looper

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configurar el RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        taskAdapter = TaskAdapter(mutableListOf(), { task ->
            moveToCompletedTasks(task)
        }, { task ->
            editTask(task)
        })
        recyclerView.adapter = taskAdapter

        // Configurar el botón de agregar
        val btnAdd: Button = findViewById(R.id.btnAdd)
        btnAdd.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivity(intent)
        }

        //configurar boton terminadas
        val btnCompleted: Button = findViewById(R.id.btnCompleted)
        btnCompleted.setOnClickListener {
            val intent = Intent(this, CompletedTasksActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadTasks()
    }

    private fun loadTasks() {
        val tasks = TaskManager.loadTasks(this).filter { !it.isCompleted }
        val sortedTasks = tasks.sortedWith(compareBy({ it.date }, { it.time }))
        taskAdapter.updateTasks(sortedTasks)
    }

    private fun editTask(task: Task) {
        val intent = Intent(this, EditTaskActivity::class.java)
        intent.putExtra("task", task)
        startActivity(intent)
    }

    private fun moveToCompletedTasks(task: Task) {
        val tasks = TaskManager.loadTasks(this).toMutableList()
        val index = tasks.indexOfFirst { it.id == task.id }
        if (index != -1) {
            tasks[index] = task.copy(isCompleted = true)
            TaskManager.saveTasks(this, tasks)
            // Usar Handler para retrasar la actualización del RecyclerView
            Handler(Looper.getMainLooper()).post {
            loadTasks()
            }
        }
    }

}