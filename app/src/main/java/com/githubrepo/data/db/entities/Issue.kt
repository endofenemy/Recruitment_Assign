package com.githubrepo.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ISSUE")
data class Issue(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val comments_url: String?,
    val title: String?,
    val created_at: String?,
    val updated_at: String?,
    val body: String?,
    val comments: Int?,
    val number: Int?
)