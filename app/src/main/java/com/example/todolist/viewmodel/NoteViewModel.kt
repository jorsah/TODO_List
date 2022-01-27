package com.example.todolist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todolist.model.NoteModel
import com.example.todolist.room.NoteDatabase

class NoteViewModel : ViewModel() {
    private val _itemListLiveData = MutableLiveData<MutableList<NoteModel>>()
    val itemListLiveData: LiveData<MutableList<NoteModel>>
        get() = _itemListLiveData

    val note = MutableLiveData<NoteModel>()
    var editMode = false

    fun getItemListFromDB(noteDb: NoteDatabase): MutableList<NoteModel> {
        val noteDao = noteDb.noteDao()
        _itemListLiveData.postValue(noteDao.getAll())
        return noteDao.getAll()
    }

    fun updateNote(note: NoteModel, noteDb: NoteDatabase){
        val noteDao = noteDb.noteDao()
        noteDao.updateNote(note)
    }

    fun addItemListToDB(note: NoteModel, noteDb: NoteDatabase) {
        val noteDao = noteDb.noteDao()
        noteDao.add(note)
    }


    fun deleteItemFromDb(note: NoteModel, noteDb: NoteDatabase) {
        val noteDao = noteDb.noteDao()
        noteDao.deleteNote(note)
    }

    fun getByCategory(category: String, noteDb: NoteDatabase) {
        val noteDao = noteDb.noteDao()
        if (category == "All") {
            getItemListFromDB(noteDb)
        } else _itemListLiveData.postValue(noteDao.getByCategory(category))
    }

    fun getIndex(arr: Array<String>): Int {
        var index = 0
        for (i in arr.indices) {
            if (arr[i] == note.value?.type)
                index = i
        }
        return index
    }

}