package com.example.todolist.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todolist.model.NoteModel

@Database(entities = [NoteModel::class], version = 6)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): TodoDao

    companion object {
        private var INSTANCE: NoteDatabase? = null

        fun getInstance(context: Context): NoteDatabase {
            return INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                NoteDatabase::class.java,
                "File"
            ).fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
                .also {
                    INSTANCE = it
                }
        }
    }
}
