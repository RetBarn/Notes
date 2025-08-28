package com.example.notes

import android.app.Application
import androidx.room.Room


class AppRoom : Application() {
    val database: NoteDataBase by lazy {
        Room.databaseBuilder(
            applicationContext,
            NoteDataBase::class.java,
            "notedb"
        ).build()
    }
}