package com.githubrepo.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "USER")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val login: String,
    val node_id: String
)