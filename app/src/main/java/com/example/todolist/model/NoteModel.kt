package com.example.todolist.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_model")
data class NoteModel(
    val noteName: String?,
    val type: String?,
    val done: Boolean = false,
    val description: String?,
    val image: String? = null,
    val date: String?,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
