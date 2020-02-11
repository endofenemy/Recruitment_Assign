package com.githubrepo.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.githubrepo.data.db.entities.Issue

@Dao
interface IssuesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAllIssues(issues: List<Issue>)

    @Query("SELECT * FROM ISSUE")
    fun getAllIssues(): LiveData<List<Issue>>

    @Query("DELETE FROM ISSUE")
    fun deleteAllFromTable()

}