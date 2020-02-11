package com.githubissue.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.githubrepo.data.db.dao.CommentsDao
import com.githubrepo.data.db.dao.IssuesDao
import com.githubrepo.data.db.entities.Comments
import com.githubrepo.data.db.entities.Issue
import com.githubrepo.data.db.entities.User

@Database(
    entities = [Issue::class, Comments::class, User::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getIssuesDao(): IssuesDao
    abstract fun getCommentsDao(): CommentsDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context): AppDatabase = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "DTC.db"

        ).build()
    }
}