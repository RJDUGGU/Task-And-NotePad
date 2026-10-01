package com.example.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.data.NoteItem
import com.example.ui.components.AddEditNoteBottomSheet
import com.example.ui.components.NoteItemCard

@Composable
fun NotepadScreen(
    notes: List<NoteItem>,
    onSaveNote: (id: Long, title: String, content: String, colorHex: String, isPinned: Boolean, category: String) -> Unit,
    onDeleteNote: (NoteItem) -> Unit,
    onTogglePin: (NoteItem) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddEditSheet by remember { mutableStateOf(false) }
    var noteToEdit by remember { mutableStateOf<NoteItem?>(null) }

    val pinnedNotes = remember(notes) { notes.filter { it.isPinned } }
    val unpinnedNotes = remember(notes) { notes.filter { !it.isPinned } }

    Box(modifier = modifier.fillMaxSize()) {
        if (notes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f),
                        modifier = Modifier.size(72.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.EditNote,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No notes yet",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Capture your quick ideas, thoughts, and memos. Tap + New Note to begin.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (pinnedNotes.isNotEmpty()) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.PushPin,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = " Pinned Notes",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    items(pinnedNotes, key = { it.id }) { note ->
                        NoteItemCard(
                            note = note,
                            onNoteClick = {
                                noteToEdit = note
                                showAddEditSheet = true
                            },
                            onTogglePin = { onTogglePin(note) },
                            onDeleteClick = { onDeleteNote(note) }
                        )
                    }

                    if (unpinnedNotes.isNotEmpty()) {
                        item {
                            Text(
                                text = "Other Notes",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 10.dp, bottom = 2.dp)
                            )
                        }
                    }
                }

                items(unpinnedNotes, key = { it.id }) { note ->
                    NoteItemCard(
                        note = note,
                        onNoteClick = {
                            noteToEdit = note
                            showAddEditSheet = true
                        },
                        onTogglePin = { onTogglePin(note) },
                        onDeleteClick = { onDeleteNote(note) }
                    )
                }
            }
        }

        // Floating Action Button
        ExtendedFloatingActionButton(
            onClick = {
                noteToEdit = null
                showAddEditSheet = true
            },
            icon = { Icon(Icons.Default.Add, contentDescription = null) },
            text = { Text("New Note") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .testTag("add_note_fab")
        )

        if (showAddEditSheet) {
            AddEditNoteBottomSheet(
                noteToEdit = noteToEdit,
                onDismiss = {
                    showAddEditSheet = false
                    noteToEdit = null
                },
                onSave = { title, content, colorHex, isPinned, category ->
                    onSaveNote(
                        noteToEdit?.id ?: 0L,
                        title,
                        content,
                        colorHex,
                        isPinned,
                        category
                    )
                },
                onDelete = noteToEdit?.let {
                    { onDeleteNote(it) }
                }
            )
        }
    }
}
