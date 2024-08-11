package com.cs.lista_de_tareas

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CompletedTasksActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var btnDeleteAll: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completed_tasks)

        val btnBack: ImageButton = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            finish() // Esto cerrará la actividad actual y regresará a la anterior (MainActivity)
        }

        recyclerView = findViewById(R.id.recyclerViewCompletedTasks)
        recyclerView.layoutManager = LinearLayoutManager(this)
        taskAdapter = TaskAdapter(mutableListOf(), { task ->
            removeTask(task)
        }, { task ->
            editTask(task)
        })
        recyclerView.adapter = taskAdapter

        btnDeleteAll = findViewById(R.id.btnDeleteAll)
        btnDeleteAll.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    override fun onResume() {
        super.onResume()
        loadCompletedTasks()
    }

    private fun loadCompletedTasks() {
        val tasks = TaskManager.loadTasks(this).filter { it.isCompleted }
        taskAdapter.updateTasks(tasks)
    }

    private fun removeTask(task: Task) {
        val tasks = TaskManager.loadTasks(this).toMutableList()
        val index = tasks.indexOfFirst { it.id == task.id }
        if (index != -1) {
            tasks[index] = task.copy(isCompleted = false)
            TaskManager.saveTasks(this, tasks)
            loadCompletedTasks()
        }
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(this).apply {
            setTitle("Confirmación")
            setMessage("¿Estás seguro de que quieres eliminar todas las tareas completadas?")
            setPositiveButton("Sí") { _, _ ->
                deleteAllCompletedTasks()
            }
            setNegativeButton("No", null)
            create()
            show()
        }
    }

    private fun deleteAllCompletedTasks() {
        val tasks = TaskManager.loadTasks(this).toMutableList()
        tasks.removeAll { it.isCompleted }
        TaskManager.saveTasks(this, tasks)
        loadCompletedTasks()
    }

    private fun moveToPendingTasks(task: Task) {
        val tasks = TaskManager.loadTasks(this).toMutableList()
        val index = tasks.indexOfFirst { it.id == task.id }
        if (index != -1) {
            tasks[index] = task.copy(isCompleted = false)
            TaskManager.saveTasks(this, tasks)
            loadCompletedTasks()
        }
    }

    private fun editTask(task: Task) {
        val intent = Intent(this, EditTaskActivity::class.java)
        intent.putExtra("task", task)
        startActivity(intent)
    }
}