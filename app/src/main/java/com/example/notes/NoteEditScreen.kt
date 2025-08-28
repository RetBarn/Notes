package com.example.notes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.key.type
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import com.example.notes.ui.theme.NotesTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.room.Delete
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.material.icons.filled.Add
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import java.nio.file.WatchEvent
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.input.ImeAction


@Composable
fun NoteEditScreen(
    note: Note?,
    viewModel: NoteViewModel,
    onSave: (title: String, content: String) -> Unit,
    onBack: () -> Unit,
    onDelete: () -> Unit = {}
) {
    var title by remember (note?.id){
        mutableStateOf(viewModel._tempTitle.value.ifEmpty { note?.title ?: "" })
    }
    var content by remember (note?.id){
        mutableStateOf(viewModel._tempContent.value.ifEmpty { note?.content ?: "" })
    }


    var showExitDialog by remember { mutableStateOf(false) }

    fun hasChangesExit(): Boolean {
        return title != (note?.title ?: "") || content != (note?.content ?: "")
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Подтверждение выхода") },
            text = { Text("Выйти с сохранением ?") },
            confirmButton = {
                TextButton(onClick = {
                    onBack()
                    viewModel.updateTempValues("", "")
                }) {
                    Text("Нет")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    onSave(title, content)
                    viewModel.updateTempValues("", "")
                }){
                    Text("Да")
                }
            }
        )
    }

    var showDeleteDialog by remember { mutableStateOf(false) }



    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Подтверждение удаления") },
            text = { Text("Вы хотите удалить заметку?") },
            confirmButton = {
                TextButton(onClick = {
                    onDelete()
                    viewModel.updateTempValues("", "")
                }) {
                    Text("Да")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Нет")
                }
            }
        )
    }


    // Подтверждение сохранения
    var showSaveDialog by remember { mutableStateOf(false) }

    fun hasChangesSave(): Boolean {
        return title != (note?.title ?: "") || content != (note?.content ?: "")
    }

    if (showSaveDialog) {
        AlertDialog(
            onDismissRequest = { showSaveDialog = false },
            title = { Text("Подтверждение сохранения") },
            text = { Text("Вы хотите сохранить заметку?") },
            confirmButton = {
                TextButton(onClick = {
                    onSave(title, content)
                    viewModel.updateTempValues("", "")
                }) {
                    Text("Да")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSaveDialog = false }) {
                    Text("Нет")
                }
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                if (hasChangesExit()) {
                    showExitDialog = true
                } else {
                    onBack()
                }
            }){
                Icon(Icons.Default.ArrowBack, "Back")
            }

            Text(
                note?.title?.take(20) ?: "Новая заметка",
                modifier = Modifier.weight(1f),
                maxLines = 1
            )

            if (note != null) {
                IconButton(
                    onClick = {
                        showDeleteDialog = true
                    }
                ) {
                    Icon(Icons.Default.Delete, "Delete")
                }
                viewModel.updateTempValues("", "")
            }
            IconButton(
                onClick = {
                    if (hasChangesSave()) {
                        showSaveDialog = true
                    } else {
                        onSave(title, content)
                    }
                    viewModel.updateTempValues("", "")},
                enabled = title.isNotBlank() || content.isNotBlank()
            ) {
                Icon(Icons.Default.Check, "Save")
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it
                    viewModel.updateTempValues(it, content)},
                label = { Text("Название") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth().onKeyEvent { event ->
                    if (event.type == KeyEventType.KeyDown) {
                        println("Pressed: ${event.nativeKeyEvent}")
                    }
                    true
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = content,
                onValueChange = { content = it
                    viewModel.updateTempValues(title, it)},
                label = { Text("Содержание") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                maxLines = Int.MAX_VALUE
            )
        }
    }
}