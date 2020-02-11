package com.githubrepo.ui.comments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.githubrepo.data.repository.IssueRepository

class CommentsViewModelFactory(
    private val repository: IssueRepository,
    private val commentNumber: String
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CommentsViewModel(repository, commentNumber) as T
    }
}