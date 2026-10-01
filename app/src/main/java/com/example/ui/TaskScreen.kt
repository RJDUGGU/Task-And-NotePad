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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
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
import com.example.data.Priority
import com.example.data.TaskItem
import com.example.ui.components.AddEditTaskBottomSheet
import com.example.ui.components.TaskItemCard
import com.example.ui.components.TaskProgressHeader

@Composable
fun TaskScreen(
    tasks: List<TaskItem>,
    totalCount: Int,
    completedCount: Int,
    selectedFilter: TaskFilter,
    onFilterSelect: (TaskFilter) -> Unit,
    onToggleTaskComplete: (TaskItem) -> Unit,
    onSaveTask: (id: Long, title: String, description: String, priority: Priority, category: String, dueDateMillis: Long?) -> Unit,
    onDeleteTask: (TaskItem) -> Unit,
    onClearCompleted: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddEditSheet by remember { mutableStateOf(false) }
    var taskToEdit by remember { mutableStateOf<TaskItem?>(null) }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Task Progress Summary
            TaskProgressHeader(
                total = totalCount,
                completed = completedCount,
                onClearCompleted = onClearCompleted,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Filter Chips
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(TaskFilter.values()) { filter ->
                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick = { onFilterSelect(filter) },
                        label = { Text(filter.label) },
                        modifier = Modifier.testTag("filter_${filter.name}")
                    )
                }
            }

            if (tasks.isEmpty()) {
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
                            shape = androidx.compose.foundation.shape.CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                            modifier = Modifier.size(72.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.AssignmentTurnedIn,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = if (selectedFilter == TaskFilter.COMPLETED) "No completed tasks"
                            else if (selectedFilter == TaskFilter.ACTIVE) "No active tasks pending!"
                            else "No tasks yet",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Tap the + button below to add your first to-do item.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(tasks, key = { it.id }) { task ->
                        TaskItemCard(
                            task = task,
                            onToggleComplete = { onToggleTaskComplete(task) },
                            onEditClick = {
                                taskToEdit = task
                                showAddEditSheet = true
                            },
                            onDeleteClick = { onDeleteTask(task) }
                        )
                    }
                }
            }
        }

        // Floating Action Button
        ExtendedFloatingActionButton(
            onClick = {
                taskToEdit = null
                showAddEditSheet = true
            },
            icon = { Icon(Icons.Default.Add, contentDescription = null) },
            text = { Text("New Task") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .testTag("add_task_fab")
        )

        if (showAddEditSheet) {
            AddEditTaskBottomSheet(
                taskToEdit = taskToEdit,
                onDismiss = {
                    showAddEditSheet = false
                    taskToEdit = null
                },
                onSave = { title, description, priority, category, dueDateMillis ->
                    onSaveTask(
                        taskToEdit?.id ?: 0L,
                        title,
                        description,
                        priority,
                        category,
                        dueDateMillis
                    )
                },
                onDelete = taskToEdit?.let {
                    { onDeleteTask(it) }
                }
            )
        }
    }
}
