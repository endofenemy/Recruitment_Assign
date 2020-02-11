package com.githubrepo.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.githubrepo.data.db.entities.Comments
import com.githubrepo.data.db.entities.Issue

@Dao
interface CommentsDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAllComments(issues: List<Comments>)

    @Query("SELECT * FROM COMMENTS")
    fun getAllComments(): LiveData<List<Comments>>

    @Query("DELETE FROM COMMENTS")
    fun deleteAllFromTable()

}