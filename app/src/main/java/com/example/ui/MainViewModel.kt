package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.NoteItem
import com.example.data.Priority
import com.example.data.TaskItem
import com.example.data.TaskNoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppTab {
    TASKS,
    NOTES
}

enum class TaskFilter(val label: String) {
    ALL("All"),
    ACTIVE("Active"),
    COMPLETED("Completed"),
    HIGH_PRIORITY("High Priority")
}

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TaskNoteRepository

    private val _currentTab = MutableStateFlow(AppTab.TASKS)
    val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _taskFilter = MutableStateFlow(TaskFilter.ALL)
    val taskFilter: StateFlow<TaskFilter> = _taskFilter.asStateFlow()

    private val _rawTasks: StateFlow<List<TaskItem>>
    private val _rawNotes: StateFlow<List<NoteItem>>

    val filteredTasks: StateFlow<List<TaskItem>>
    val filteredNotes: StateFlow<List<NoteItem>>

    val completedTasksCount: StateFlow<Int>
    val totalTasksCount: StateFlow<Int>

    init {
        val database = AppDatabase.getDatabase(application)
        repository = TaskNoteRepository(database.taskDao(), database.noteDao())

        _rawTasks = repository.allTasks.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        _rawNotes = repository.allNotes.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        totalTasksCount = _rawTasks.combine(MutableStateFlow(Unit)) { tasks, _ ->
            tasks.size
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

        completedTasksCount = _rawTasks.combine(MutableStateFlow(Unit)) { tasks, _ ->
            tasks.count { it.isCompleted }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

        filteredTasks = combine(_rawTasks, _searchQuery, _taskFilter) { tasks, query, filter ->
            tasks.filter { task ->
                val matchesQuery = query.isBlank() ||
                        task.title.contains(query, ignoreCase = true) ||
                        task.description.contains(query, ignoreCase = true) ||
                        task.category.contains(query, ignoreCase = true)

                val matchesFilter = when (filter) {
                    TaskFilter.ALL -> true
                    TaskFilter.ACTIVE -> !task.isCompleted
                    TaskFilter.COMPLETED -> task.isCompleted
                    TaskFilter.HIGH_PRIORITY -> task.priority == Priority.HIGH
                }

                matchesQuery && matchesFilter
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        filteredNotes = combine(_rawNotes, _searchQuery) { notes, query ->
            notes.filter { note ->
                if (query.isBlank()) true
                else note.title.contains(query, ignoreCase = true) ||
                        note.content.contains(query, ignoreCase = true) ||
                        note.category.contains(query, ignoreCase = true)
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }
    }

    fun selectTab(tab: AppTab) {
        _currentTab.value = tab
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setTaskFilter(filter: TaskFilter) {
        _taskFilter.value = filter
    }

    fun toggleTaskComplete(task: TaskItem) {
        viewModelScope.launch {
            repository.updateTask(task.copy(isCompleted = !task.isCompleted))
        }
    }

    fun saveTask(
        id: Long,
        title: String,
        description: String,
        priority: Priority,
        category: String,
        dueDateMillis: Long?
    ) {
        viewModelScope.launch {
            if (id == 0L) {
                repository.insertTask(
                    TaskItem(
                        title = title.trim(),
                        description = description.trim(),
                        priority = priority,
                        category = category.ifBlank { "General" },
                        dueDateMillis = dueDateMillis
                    )
                )
            } else {
                val existing = _rawTasks.value.find { it.id == id }
                if (existing != null) {
                    repository.updateTask(
                        existing.copy(
                            title = title.trim(),
                            description = description.trim(),
                            priority = priority,
                            category = category.ifBlank { "General" },
                            dueDateMillis = dueDateMillis
                        )
                    )
                }
            }
        }
    }

    fun deleteTask(task: TaskItem) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    fun clearCompletedTasks() {
        viewModelScope.launch {
            repository.deleteCompletedTasks()
        }
    }

    fun saveNote(
        id: Long,
        title: String,
        content: String,
        colorHex: String,
        isPinned: Boolean,
        category: String
    ) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            if (id == 0L) {
                repository.insertNote(
                    NoteItem(
                        title = title.trim(),
                        content = content.trim(),
                        colorHex = colorHex,
                        isPinned = isPinned,
                        category = category.ifBlank { "Personal" },
                        updatedAt = now
                    )
                )
            } else {
                val existing = _rawNotes.value.find { it.id == id }
                if (existing != null) {
                    repository.updateNote(
                        existing.copy(
                            title = title.trim(),
                            content = content.trim(),
                            colorHex = colorHex,
                            isPinned = isPinned,
                            category = category.ifBlank { "Personal" },
                            updatedAt = now
                        )
                    )
                }
            }
        }
    }

    fun deleteNote(note: NoteItem) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }

    fun toggleNotePin(note: NoteItem) {
        viewModelScope.launch {
            repository.updateNote(note.copy(isPinned = !note.isPinned, updatedAt = System.currentTimeMillis()))
        }
    }
}
