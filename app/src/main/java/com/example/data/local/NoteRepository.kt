package com.example.data.local

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import kotlin.math.max

class NoteRepository(
    private val noteDao: NoteDao,
    private val context: Context
) {
    val allNotes: Flow<List<NoteEntity>> = noteDao.getAllNotes()

    fun getNotesByCategory(category: String): Flow<List<NoteEntity>> =
        if (category == "All") noteDao.getAllNotes() else noteDao.getNotesByCategory(category)

    fun searchNotes(query: String): Flow<List<NoteEntity>> =
        noteDao.searchNotes(query)

    suspend fun insertTextNote(
        title: String,
        content: String,
        category: String = "Calculus"
    ): Long = withContext(Dispatchers.IO) {
        val note = NoteEntity(
            title = title.ifBlank { "Untitled Note" },
            content = content,
            category = category,
            sourceType = "MANUAL",
            timestamp = System.currentTimeMillis()
        )
        noteDao.insertNote(note)
    }

    suspend fun saveCameraNote(
        bitmap: Bitmap,
        title: String,
        content: String,
        category: String = "Calculus"
    ): Long = withContext(Dispatchers.IO) {
        val notesDir = File(context.filesDir, "notes_images").apply { if (!exists()) mkdirs() }
        val filename = "camera_note_${System.currentTimeMillis()}.jpg"
        val imageFile = File(notesDir, filename)

        // Optimize and compress bitmap before writing to storage
        val maxDim = 1280
        val w = bitmap.width
        val h = bitmap.height
        val scale = if (max(w, h) > maxDim) maxDim.toFloat() / max(w, h) else 1.0f

        val processed = if (scale < 1.0f) {
            val matrix = Matrix().apply { postScale(scale, scale) }
            Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true)
        } else {
            bitmap
        }

        FileOutputStream(imageFile).use { out ->
            processed.compress(Bitmap.CompressFormat.JPEG, 90, out)
        }

        val note = NoteEntity(
            title = title.ifBlank { "Camera Note (${filename.take(20)})" },
            content = content,
            category = category,
            sourceType = "CAMERA",
            imagePath = imageFile.absolutePath,
            originalFileName = filename,
            timestamp = System.currentTimeMillis()
        )
        noteDao.insertNote(note)
    }

    suspend fun saveImportedFileNote(
        uri: Uri,
        title: String,
        content: String,
        category: String = "Calculus"
    ): Long = withContext(Dispatchers.IO) {
        val contentResolver = context.contentResolver
        val mimeType = contentResolver.getType(uri) ?: ""
        val originalName = getFileNameFromUri(context, uri) ?: "imported_note"

        val isImage = mimeType.startsWith("image/") ||
                originalName.endsWith(".jpg", ignoreCase = true) ||
                originalName.endsWith(".jpeg", ignoreCase = true) ||
                originalName.endsWith(".png", ignoreCase = true) ||
                originalName.endsWith(".webp", ignoreCase = true)

        if (isImage) {
            val notesDir = File(context.filesDir, "notes_images").apply { if (!exists()) mkdirs() }
            val extension = originalName.substringAfterLast('.', "jpg")
            val targetFile = File(notesDir, "import_${System.currentTimeMillis()}.$extension")

            contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(targetFile).use { output ->
                    input.copyTo(output)
                }
            }

            val note = NoteEntity(
                title = title.ifBlank { originalName.substringBeforeLast('.') },
                content = content,
                category = category,
                sourceType = "FILE_IMAGE",
                imagePath = targetFile.absolutePath,
                originalFileName = originalName,
                timestamp = System.currentTimeMillis()
            )
            return@withContext noteDao.insertNote(note)
        } else {
            // Text or Document file
            val text = contentResolver.openInputStream(uri)?.bufferedReader()?.use { it.readText() } ?: ""
            val fullContent = if (content.isNotBlank()) "$content\n\n--- File Content ---\n$text" else text

            val note = NoteEntity(
                title = title.ifBlank { originalName },
                content = fullContent,
                category = category,
                sourceType = "FILE_TEXT",
                originalFileName = originalName,
                timestamp = System.currentTimeMillis()
            )
            return@withContext noteDao.insertNote(note)
        }
    }

    suspend fun updateNote(note: NoteEntity) = withContext(Dispatchers.IO) {
        noteDao.updateNote(note)
    }

    suspend fun deleteNote(note: NoteEntity) = withContext(Dispatchers.IO) {
        if (!note.imagePath.isNullOrBlank()) {
            try {
                val file = File(note.imagePath)
                if (file.exists()) file.delete()
            } catch (_: Exception) {}
        }
        noteDao.deleteNote(note)
    }

    suspend fun togglePin(id: Long) = withContext(Dispatchers.IO) {
        noteDao.togglePin(id)
    }

    suspend fun populateSampleNotesIfEmpty() = withContext(Dispatchers.IO) {
        if (noteDao.getNotesCount() == 0) {
            insertTextNote(
                title = "Catenary Suspension Cable Formulas",
                content = """
                    ## Catenary Hanging Cable Equation
                    y(x) = a * cosh(x / a) - a
                    where a = T₀ / (μ * g)
                    - T₀: Horizontal tension at lowest point (N)
                    - μ: Mass linear density (kg/m)
                    - g: Gravitational acceleration (9.81 m/s²)

                    ### Mid-Span Sag Equation
                    Sag h = a * (cosh(L / (2a)) - 1)

                    ### Total Cable Arc Length S
                    S = 2 * a * sinh(L / (2a))
                """.trimIndent(),
                category = "Catenary"
            )

            insertTextNote(
                title = "Hyperbolic Trigonometric Derivatives",
                content = """
                    ## Standard Hyperbolic Derivatives
                    - d/dx [sinh(x)] = cosh(x)
                    - d/dx [cosh(x)] = sinh(x) (Note positive sign, unlike cos(x)!)
                    - d/dx [tanh(x)] = sech²(x) = 1 - tanh²(x)
                    - d/dx [coth(x)] = -csch²(x)
                    - d/dx [sech(x)] = -sech(x) * tanh(x)
                    - d/dx [csch(x)] = -csch(x) * coth(x)

                    ### Fundamental Hyperbolic Identity
                    cosh²(x) - sinh²(x) = 1
                """.trimIndent(),
                category = "Calculus"
            )

            insertTextNote(
                title = "Inverse Hyperbolic Integrals Sheet",
                content = """
                    ## Key Integrals in Engineering & Physics
                    - ∫ 1 / √(x² + 1) dx = asinh(x) + C = ln(x + √(x² + 1)) + C
                    - ∫ 1 / √(x² - 1) dx = acosh(x) + C = ln(x + √(x² - 1)) + C  (x > 1)
                    - ∫ 1 / (1 - x²) dx = atanh(x) + C = 1/2 ln((1+x)/(1-x)) + C (|x| < 1)
                """.trimIndent(),
                category = "Formula Sheet"
            )
        }
    }

    private fun getFileNameFromUri(context: Context, uri: Uri): String? {
        var name: String? = null
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val idx = it.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                    if (idx >= 0) name = it.getString(idx)
                }
            }
        }
        return name ?: uri.path?.substringAfterLast('/')
    }
}
