package com.example.roomdatabaseexample

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.example.roomdatabaseexample.ui.theme.RoomDatabaseExampleTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    var count = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RoomDatabaseExampleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    val noteDatabase = NoteDatabase.getDatabase(this@MainActivity)
                    val dbHelper = DatabaseHelperImpl(noteDatabase)
                    val roomDBViewModel = RoomDBViewModel(dbHelper)

                    val existingNote = roomDBViewModel.notesResponse.collectAsState().value
                    existingNote?.let {
                        existingNote.forEach {
                                note->
                            Log.i("AppLogs","${note.id} ${note.title} ${note.content}")
                        }
                        Log.i("AppLogs","==============================================")
                    }
                    Column(modifier = Modifier.fillMaxSize().padding(innerPadding),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally) {

                        Button(onClick = {
                            // Insert a new note
                            CoroutineScope(Dispatchers.IO).launch {
                                val newNote = Note(title = "My Note $count", content = "This is a sample note $count")
                                roomDBViewModel.insert(newNote)
                                roomDBViewModel.fetchNotes()
                                count++
                            }
                        }) {
                            Text("Insert")
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(onClick = {
                            CoroutineScope(Dispatchers.IO).launch {
                                val existingNote = noteDatabase.noteDao().getAllNotes()
                                existingNote?.let {
                                    existingNote.forEach {
                                        note->
                                        note.title = "Updated Note"
                                        roomDBViewModel.update(note)
                                    }
                                }
                                roomDBViewModel.fetchNotes()
                            }
                        }) {
                            Text("Update")
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(onClick = {
                            CoroutineScope(Dispatchers.IO).launch {
                                val existingNote = noteDatabase.noteDao().getAllNotes()
                                existingNote?.let {
                                    existingNote.forEach {
                                            note->
                                        roomDBViewModel.delete(note)
                                    }
                                }
                                roomDBViewModel.fetchNotes()
                            }
                        }) {
                            Text("Delete")
                        }

                        Spacer(modifier = Modifier.height(20.dp))



                        Button(onClick = {
                            roomDBViewModel.fetchNotes()
                            Log.i("AppLogs","===========================================")
                        }) {
                            Text("Query")
                        }
                    }
                }
            }
        }
    }
}

