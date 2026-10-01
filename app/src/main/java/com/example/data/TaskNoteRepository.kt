package com.example.data

import kotlinx.coroutines.flow.Flow

class TaskNoteRepository(
    private val taskDao: TaskDao,
    private val noteDao: NoteDao
) {
    val allTasks: Flow<List<TaskItem>> = taskDao.getAllTasks()
    val allNotes: Flow<List<NoteItem>> = noteDao.getAllNotes()

    suspend fun insertTask(task: TaskItem): Long = taskDao.insertTask(task)
    suspend fun updateTask(task: TaskItem) = taskDao.updateTask(task)
    suspend fun deleteTask(task: TaskItem) = taskDao.deleteTask(task)
    suspend fun deleteCompletedTasks() = taskDao.deleteCompletedTasks()

    suspend fun insertNote(note: NoteItem): Long = noteDao.insertNote(note)
    suspend fun updateNote(note: NoteItem) = noteDao.updateNote(note)
    suspend fun deleteNote(note: NoteItem) = noteDao.deleteNote(note)

    suspend fun seedInitialDataIfEmpty() {
        if (taskDao.getTaskCount() == 0) {
            taskDao.insertTask(
                TaskItem(
                    title = "Plan weekly goals and schedule",
                    description = "Outline key priorities, deadlines, and project milestones",
                    isCompleted = false,
                    priority = Priority.HIGH,
                    category = "Work",
                    dueDateMillis = System.currentTimeMillis() + 86400000L
                )
            )
            taskDao.insertTask(
                TaskItem(
                    title = "Pick up groceries & fresh fruits",
                    description = "Apples, oats, almond milk, and green tea",
                    isCompleted = false,
                    priority = Priority.MEDIUM,
                    category = "Personal",
                    dueDateMillis = System.currentTimeMillis() + 172800000L
                )
            )
            taskDao.insertTask(
                TaskItem(
                    title = "Try out Task & Notepad app features",
                    description = "Tap on tasks to mark complete, edit notes, and customize note colors!",
                    isCompleted = true,
                    priority = Priority.LOW,
                    category = "Ideas"
                )
            )
        }

        if (noteDao.getNoteCount() == 0) {
            noteDao.insertNote(
                NoteItem(
                    title = "Welcome to Task & Notepad ✨",
                    content = "This app brings your daily to-dos and quick thoughts into one unified space.\n\n• Switch between Tasks and Notes with the top tabs.\n• Pin important notes to the top.\n• Select cheerful pastel colors for visual categorization.\n• Filter tasks by Active, Completed, or Priority.",
                    colorHex = "#FFF9C4", // Soft Yellow
                    isPinned = true,
                    category = "Getting Started"
                )
            )
            noteDao.insertNote(
                NoteItem(
                    title = "Creative Project Ideas 💡",
                    content = "1. Minimalist desktop productivity layout\n2. Weekly meal planner with nutrition highlights\n3. Quick workout tracker with sets and reps",
                    colorHex = "#E1F5FE", // Soft Blue
                    isPinned = false,
                    category = "Ideas"
                )
            )
        }
    }
}
