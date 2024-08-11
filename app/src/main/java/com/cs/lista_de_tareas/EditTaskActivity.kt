package com.cs.lista_de_tareas

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class EditTaskActivity : AppCompatActivity() {


    private lateinit var etTitle: EditText
    private lateinit var etDescription: EditText
    private lateinit var etDate: EditText
    private lateinit var etTime: EditText
    private lateinit var spinnerList: Spinner
    private lateinit var btnEdit: Button
    private lateinit var task: Task

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_task)

        val btnBackEditTask: ImageButton = findViewById(R.id.btnBackAddTask)
        btnBackEditTask.setOnClickListener {
            finish() // Esto cerrará la actividad actual y regresará a la anterior
        }

        // Recuperar el objeto Task
        task = intent.getSerializableExtra("task") as Task

        // Rellena los campos del formulario con los datos de la tarea
        findViewById<EditText>(R.id.etTitle).setText(task.title)
        findViewById<EditText>(R.id.etDescription).setText(task.description)
        findViewById<EditText>(R.id.etDate).setText(task.date)
        findViewById<EditText>(R.id.etTime).setText(task.time)
        findViewById<Spinner>(R.id.spinnerList).setSelection(if (task.type == "Tarea") 0 else 1)

        // Configurar el botón de guardar cambios
        findViewById<Button>(R.id.btnEdit).setOnClickListener {
            saveTask()}

        val btnDiscard: Button = findViewById(R.id.btnDiscard) //descartar cambios
        btnDiscard.setOnClickListener {
            showDiscardConfirmationDialog()
        }

        etTitle = findViewById(R.id.etTitle)
        etDescription = findViewById(R.id.etDescription)
        etDate = findViewById(R.id.etDate)
        etTime = findViewById(R.id.etTime)
        spinnerList = findViewById(R.id.spinnerList)
        btnEdit = findViewById(R.id.btnEdit)

        task = intent.getSerializableExtra("task") as Task
        populateTaskData(task)

        etDate.setOnClickListener { showDatePickerDialog() }
        etTime.setOnClickListener { showTimePickerDialog() }
        //btnEdit.setOnClickListener { saveTask() }
    }

    private fun populateTaskData(task: Task) {
        etTitle.setText(task.title)
        etDescription.setText(task.description)
        etDate.setText(task.date)
        etTime.setText(task.time)

        // Configurar el adaptador del Spinner
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.spinner_list_items, // Usar el array existente en strings.xml
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerList.adapter = adapter

        // Seleccionar el tipo de tarea en el Spinner
        val position = adapter.getPosition(task.type)
        spinnerList.setSelection(position)
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

    private fun saveTask() {
        // Actualizar la tarea con los nuevos datos
        task = task.copy(
            title = findViewById<EditText>(R.id.etTitle).text.toString(),
            description = findViewById<EditText>(R.id.etDescription).text.toString(),
            date = findViewById<EditText>(R.id.etDate).text.toString(),
            time = findViewById<EditText>(R.id.etTime).text.toString(),
            type = findViewById<Spinner>(R.id.spinnerList).selectedItem.toString()
        )

        // Guardar la tarea actualizada
        val tasks = TaskManager.loadTasks(this).toMutableList()
        val index = tasks.indexOfFirst { it.id == task.id }
        if (index != -1) {
            tasks[index] = task
            TaskManager.saveTasks(this, tasks)
        }

        // Regresar a MainActivity
        finish()
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