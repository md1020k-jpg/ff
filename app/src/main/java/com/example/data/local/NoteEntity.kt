package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "math_notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val category: String = "Calculus", // "Calculus", "Catenary", "Identities", "Formula Sheet", "General"
    val sourceType: String = "MANUAL",  // "CAMERA", "FILE_IMAGE", "FILE_TEXT", "MANUAL"
    val imagePath: String? = null,      // Internal persistent file storage path
    val originalFileName: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val isPinned: Boolean = false
)
