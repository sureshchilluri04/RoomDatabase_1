package com.example.roomdatabaseexample

import kotlinx.coroutines.flow.Flow

interface DatabaseHelper {

    fun getNotes(): Flow<List<Note>>

    suspend fun insert(note:Note)

    suspend fun update(note:Note)

    suspend fun delete(note:Note)
}