package com.example.roomdatabaseexample

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DatabaseHelperImpl(private val noteDatabase: NoteDatabase) : DatabaseHelper {

    override fun getNotes(): Flow<List<Note>> = flow {
        emit(noteDatabase.noteDao().getAllNotes())
    }

    override suspend fun insert(note: Note) {
        noteDatabase.noteDao().insert(note)
    }

    override suspend fun update(note: Note) {
        noteDatabase.noteDao().update(note)
    }

    override suspend fun delete(note: Note) {
        noteDatabase.noteDao().delete(note)
    }
}