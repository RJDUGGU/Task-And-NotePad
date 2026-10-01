package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val colorHex: String = "#FFF9C4", // Soft Yellow default
    val isPinned: Boolean = false,
    val category: String = "Personal",
    val updatedAt: Long = System.currentTimeMillis()
)
