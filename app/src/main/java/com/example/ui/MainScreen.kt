package com.example.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.PlaylistAddCheck
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val currentTab by viewModel.currentTab.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val taskFilter by viewModel.taskFilter.collectAsState()

    val tasks by viewModel.filteredTasks.collectAsState()
    val notes by viewModel.filteredNotes.collectAsState()

    val totalTasks by viewModel.totalTasksCount.collectAsState()
    val completedTasks by viewModel.completedTasksCount.collectAsState()

    var isSearchExpanded by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Surface(
                tonalElevation = 2.dp,
                shadowElevation = 1.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    TopAppBar(
                        title = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.PlaylistAddCheck,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Task & Notepad",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        },
                        actions = {
                            IconButton(
                                onClick = {
                                    isSearchExpanded = !isSearchExpanded
                                    if (!isSearchExpanded) {
                                        viewModel.setSearchQuery("")
                                    }
                                },
                                modifier = Modifier.testTag("toggle_search_button")
                            ) {
                                Icon(
                                    imageVector = if (isSearchExpanded) Icons.Default.Clear else Icons.Default.Search,
                                    contentDescription = if (isSearchExpanded) "Close search" else "Search"
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    )

                    if (isSearchExpanded) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { viewModel.setSearchQuery(it) },
                            placeholder = { Text("Search tasks & notes…") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = "Clear query"
                                        )
                                    }
                                }
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 6.dp)
                                .testTag("search_text_field"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    // Tab Selector
                    TabRow(
                        selectedTabIndex = currentTab.ordinal,
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.primary
                    ) {
                        Tab(
                            selected = currentTab == AppTab.TASKS,
                            onClick = { viewModel.selectTab(AppTab.TASKS) },
                            text = {
                                BadgedBox(
                                    badge = {
                                        val activeCount = totalTasks - completedTasks
                                        if (activeCount > 0) {
                                            Badge { Text("$activeCount") }
                                        }
                                    }
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PlaylistAddCheck,
                                            contentDescription = null,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Tasks",
                                            fontWeight = if (currentTab == AppTab.TASKS) FontWeight.Bold else FontWeight.Medium
                                        )
                                    }
                                }
                            },
                            modifier = Modifier.testTag("tab_tasks")
                        )

                        Tab(
                            selected = currentTab == AppTab.NOTES,
                            onClick = { viewModel.selectTab(AppTab.NOTES) },
                            text = {
                                BadgedBox(
                                    badge = {
                                        if (notes.isNotEmpty()) {
                                            Badge { Text("${notes.size}") }
                                        }
                                    }
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Description,
                                            contentDescription = null,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Notepad",
                                            fontWeight = if (currentTab == AppTab.NOTES) FontWeight.Bold else FontWeight.Medium
                                        )
                                    }
                                }
                            },
                            modifier = Modifier.testTag("tab_notes")
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Crossfade(
            targetState = currentTab,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) { tab ->
            when (tab) {
                AppTab.TASKS -> {
                    TaskScreen(
                        tasks = tasks,
                        totalCount = totalTasks,
                        completedCount = completedTasks,
                        selectedFilter = taskFilter,
                        onFilterSelect = { viewModel.setTaskFilter(it) },
                        onToggleTaskComplete = { viewModel.toggleTaskComplete(it) },
                        onSaveTask = { id, title, desc, prio, cat, due ->
                            viewModel.saveTask(id, title, desc, prio, cat, due)
                        },
                        onDeleteTask = { viewModel.deleteTask(it) },
                        onClearCompleted = { viewModel.clearCompletedTasks() }
                    )
                }

                AppTab.NOTES -> {
                    NotepadScreen(
                        notes = notes,
                        onSaveNote = { id, title, content, color, pinned, cat ->
                            viewModel.saveNote(id, title, content, color, pinned, cat)
                        },
                        onDeleteNote = { viewModel.deleteNote(it) },
                        onTogglePin = { viewModel.toggleNotePin(it) }
                    )
                }
            }
        }
    }
}
