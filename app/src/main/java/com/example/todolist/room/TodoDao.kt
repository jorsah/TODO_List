package com.example.todolist.room

import androidx.room.*
import com.example.todolist.model.NoteModel

@Dao
interface TodoDao {
    @Query("SELECT * FROM note_model")
    fun getAll(): MutableList<NoteModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(vararg note: NoteModel)

    @Delete
    fun deleteNote(note: NoteModel)

    @Query("SELECT * FROM note_model WHERE type LIKE :category")
    fun getByCategory(category: String): MutableList<NoteModel>

    @Update
    fun updateNote(note: NoteModel)
}