package com.cs.lista_de_tareas

import java.io.Serializable

data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val date: String,
    val time: String,
    val type: String, // "Tarea" o "Lista de deseos"
    var isCompleted: Boolean = false
): Serializable
