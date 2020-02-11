package com.githubrepo.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.githubissue.data.db.AppDatabase
import com.githubrepo.data.db.entities.Comments
import com.githubrepo.data.db.entities.Issue
import com.githubrepo.data.network.MyApi
import com.githubrepo.data.network.SafeApiRequest
import com.githubrepo.data.preferences.PreferenceProvider
import com.githubrepo.utils.Constants
import com.githubrepo.utils.Coroutines
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class IssueRepository(
    private val api: MyApi,
    private val db: AppDatabase,
    private val preferences: PreferenceProvider
) : SafeApiRequest() {
    private val issues = MutableLiveData<List<Issue>>()
    private val comments = MutableLiveData<List<Comments>>()

    init {
        issues.observeForever {
            saveIssues(it)
        }

        comments.observeForever {
            saveComments(it)
        }
    }


    suspend fun getIssues(forcefully: Boolean): LiveData<List<Issue>> {
        return withContext(Dispatchers.IO) {
            fetchIssues(forcefully)
            db.getIssuesDao().getAllIssues()
        }
    }

    suspend fun getComments(commentNumber: String): LiveData<List<Comments>> {
        return withContext(Dispatchers.IO) {
            fetchComments(commentNumber)
            db.getCommentsDao().getAllComments()
        }
    }

    suspend fun fetchComments(commentNumber:  String) {
        try {
            val response = apiRequest { api.getComments(commentNumber) }
            Log.e("Comments", response.toString())
            comments.postValue(response)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    suspend fun fetchIssues(forcefully: Boolean) {
        if (forcefully || isFetchNeeded(preferences.getLastSavedAt())) {
            try {
                val response = apiRequest { api.getIssues() }
                Log.e("Response", response.toString())
                issues.postValue(response)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    // return true if Last Cache Time is more than 2 hours otherwise return false
    private fun isFetchNeeded(lastSavedAt: String?): Boolean {
        if (lastSavedAt == null)
            return true             // If Caching First time return true
        else {
            val lastSavedTime = lastSavedAt.toLong()
            val currentTime = System.currentTimeMillis()
            Log.e("Current Time $currentTime", " Saved Times $lastSavedTime")
            return ((currentTime - lastSavedTime) > Constants.DURATION)
        }
    }

    private fun saveIssues(issue: List<Issue>) {
        Coroutines.io {
            preferences.saveLastSavedAt(System.currentTimeMillis().toString())
            db.getIssuesDao().deleteAllFromTable()
            db.getIssuesDao().saveAllIssues(issue)
        }
    }

    private fun saveComments(comments: List<Comments>) {
        Coroutines.io {
            preferences.saveLastSavedAt(System.currentTimeMillis().toString())
            db.getCommentsDao().deleteAllFromTable()
            db.getCommentsDao().saveAllComments(comments)
        }
    }
}