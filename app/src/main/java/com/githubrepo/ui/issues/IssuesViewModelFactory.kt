package com.githubrepo.ui.issues

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.githubrepo.data.repository.IssueRepository

@Suppress("UNCHECKED_CAST")
class IssuesViewModelFactory(
    private val repository: IssueRepository,
    private val forcefully: Boolean
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return IssuesViewModel(repository, forcefully) as T
    }
}