package dev.csse.ctran156.recall

import android.app.Application
import dev.csse.ctran156.recall.data.BookmarkRepository

class MainApplication : Application() {
    lateinit var bookmarkRepo: BookmarkRepository


    override fun onCreate() {
        super.onCreate()

        bookmarkRepo = BookmarkRepository(applicationContext)
    }
}