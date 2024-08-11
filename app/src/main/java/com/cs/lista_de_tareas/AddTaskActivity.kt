package com.cs.lista_de_tareas

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    private lateinit var etDate: EditText
    private lateinit var etTime: EditText
    private lateinit var etTitle: EditText
    private lateinit var etDescription: EditText
    private lateinit var btnSave: Button
    private lateinit var spinnerList: Spinner


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        val btnBackAddTask: ImageButton = findViewById(R.id.btnBackAddTask)
        btnBackAddTask.setOnClickListener {
            finish() // Esto cerrará la actividad actual y regresará a la anterior
        }

        etTitle = findViewById(R.id.etTitle)
        etDescription = findViewById(R.id.etDescription)
        etDate = findViewById(R.id.etDate)
        etTime = findViewById(R.id.etTime)
        btnSave = findViewById(R.id.btnSave)
        spinnerList = findViewById(R.id.spinnerList)

        // Configura el adaptador para el Spinner
        ArrayAdapter.createFromResource(
            this,
            R.array.spinner_list_items,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerList.adapter = adapter
        }

        btnSave.setOnClickListener {
            saveTask()
        }

        val btnDiscard: Button = findViewById(R.id.btnDiscard)
        btnDiscard.setOnClickListener {
            showDiscardConfirmationDialog()
        }

        etDate.setOnClickListener {
            showDatePickerDialog()
        }

        etTime.setOnClickListener {
            showTimePickerDialog()
        }
    }

    private fun saveTask() {
        // Implementa la lógica para guardar la tarea
        val title = etTitle.text.toString()
        val description = etDescription.text.toString()
        val date = etDate.text.toString()
        val time = etTime.text.toString()
        val type = findViewById<Spinner>(R.id.spinnerList).selectedItem.toString()

        if (title.isEmpty() || description.isEmpty() || date.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val newTask = Task(
            id = generateTaskId(),
            title = title,
            description = description,
            date = date,
            time = time,
            type = type
        )

        // Cargar las tareas existentes
        val tasks = TaskManager.loadTasks(this).toMutableList()
        tasks.add(newTask)

        // Guardar las tareas actualizadas
        TaskManager.saveTasks(this, tasks)

        Toast.makeText(this, "Tarea guardada", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun generateTaskId(): Int {
        val tasks = TaskManager.loadTasks(this)
        return if (tasks.isEmpty()) 1 else tasks.maxOf { it.id } + 1
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val date = String.format("%02d-%02d-%04d", selectedDay, selectedMonth + 1, selectedYear)
            etDate.setText(date)
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            val time = String.format("%02d:%02d", selectedHour, selectedMinute)
            etTime.setText(time)
        }, hour, minute, true)

        timePickerDialog.show()
    }

    private fun showDiscardConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("¿Deseas descartar los cambios?")
            .setPositiveButton("Sí") { dialog, id ->
                finish() // Descartar cambios y cerrar actividad
            }
            .setNegativeButton("No") { dialog, id ->
                dialog.dismiss() // Cerrar el diálogo
            }
        builder.create().show()
    }

}