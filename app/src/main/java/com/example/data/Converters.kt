package com.example.data

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromPriority(priority: Priority?): String {
        return priority?.name ?: Priority.MEDIUM.name
    }

    @TypeConverter
    fun toPriority(value: String?): Priority {
        return value?.let {
            try {
                Priority.valueOf(it)
            } catch (e: Exception) {
                Priority.MEDIUM
            }
        } ?: Priority.MEDIUM
    }
}
