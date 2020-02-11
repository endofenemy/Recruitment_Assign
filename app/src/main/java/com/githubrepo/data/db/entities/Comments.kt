package com.githubrepo.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "COMMENTS")
data class Comments(

    @PrimaryKey(autoGenerate = true)
    val commentId: Int,
    var id:String,
    val created_at: String,
    val updated_at: String,
    val body: String
)