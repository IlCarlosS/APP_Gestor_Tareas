package com.cs.lista_de_tareas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TaskAdapter(private var tasks: MutableList<Task>,
                  private val onTaskStatusChanged: (Task) -> Unit, // Callback para cambios en el estado de la tarea
                  private val onEditTask: (Task) -> Unit, // Nuevo parámetro para la edición

    ) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBoxTask: CheckBox = itemView.findViewById(R.id.checkBoxTask)
        val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        val textViewDescription: TextView = itemView.findViewById(R.id.textViewDescription)
        val textViewDate: TextView = itemView.findViewById(R.id.textViewDate)
        val textViewTime: TextView = itemView.findViewById(R.id.textViewTime)
        val buttonEdit: Button = itemView.findViewById(R.id.buttonEdit) // Botón de edición
        val taskItemLayout: LinearLayout = itemView.findViewById(R.id.taskItemLayout) // Nueva referencia al layout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.textViewTitle.text = task.title
        holder.textViewDescription.text = task.description
        holder.textViewDate.text = task.date
        holder.textViewTime.text = task.time
        holder.checkBoxTask.isChecked = task.isCompleted

        // Verificar si la tarea está vencida
        val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        val taskDate = task.date
        if (taskDate < currentDate) {
            holder.taskItemLayout.setBackgroundColor(holder.itemView.resources.getColor(R.color.colorExpired))
        } else {
            holder.taskItemLayout.setBackgroundColor(holder.itemView.resources.getColor(R.color.colorAccent)) // Fondo normal
        }

        holder.checkBoxTask.setOnCheckedChangeListener { _, isChecked ->
            task.isCompleted = isChecked
            onTaskStatusChanged(task)
        }

        // Deshabilitar el botón de edición si la tarea está completada
        if (task.isCompleted) {
            holder.buttonEdit.isEnabled = false
            holder.buttonEdit.visibility = View.GONE // Ocultar el botón si se prefiere
        } else {
            holder.buttonEdit.isEnabled = true
            holder.buttonEdit.visibility = View.VISIBLE
        }

        holder.buttonEdit.setOnClickListener {
            onEditTask(task)
        }
    }

    override fun getItemCount() = tasks.size

    fun updateTasks(newTasks: List<Task>) {
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged()
    }

    /*class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val checkBoxTask: CheckBox = itemView.findViewById(R.id.checkBoxTask)
        private val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        private val textViewDescription: TextView = itemView.findViewById(R.id.textViewDescription)
        private val textViewDate: TextView = itemView.findViewById(R.id.textViewDate)
        private val textViewTime: TextView = itemView.findViewById(R.id.textViewTime)


        fun bind(task: Task, onTaskStatusChanged: (Task) -> Unit) {
            checkBoxTask.isChecked = task.isCompleted
            textViewTitle.text = task.title
            textViewDescription.text = task.description
            textViewDate.text = task.date
            textViewTime.text = task.time

            // Lógica para manejar el cambio de estado de la tarea completada
            checkBoxTask.setOnCheckedChangeListener { _, isChecked ->
                if (!isChecked) { // Si la tarea es desmarcada, notificamos el cambio
                    onTaskStatusChanged(task.copy(isCompleted = false))
                }
            }
        }
    }*/
}