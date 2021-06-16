package com.ani.e_canteen.database.dao

import androidx.room.*
import com.ani.e_canteen.database.entitas.Note

@Dao
interface NoteDao {
     @Insert
     suspend fun addNote(note: Note)

     @Query("SELECT * FROM note ORDER BY id DESC")
     suspend fun getNotes() : List<Note>

     @Query("SELECT * FROM note WHERE id=:note_id")
     suspend fun getNote(note_id: Int) : List<Note>

     @Update
     suspend fun updateNote(note: Note)

     @Delete
     suspend fun deleteNote(note: Note)
}


