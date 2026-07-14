package com.example.roomdatabaseexample

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class RoomDBViewModel(private val dbHelper: DatabaseHelper) : ViewModel() {

    val notesResponse = MutableStateFlow(listOf( Note(1, "", "")))

    init {
        fetchNotes()
    }

    fun insert(note: Note) {
        viewModelScope.launch {
            dbHelper.insert(note)
        }
    }

    fun update(note: Note) {
        viewModelScope.launch {
            dbHelper.update(note)
        }
    }

    fun delete(note: Note) {
        viewModelScope.launch {
            dbHelper.delete(note)
        }
    }

    fun fetchNotes() {

        viewModelScope.launch {
            dbHelper.getNotes()
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    // handle exception
                }
                .collect { notes ->
                    notesResponse.value = notes
                }
        }
    }

}