package com.githubrepo.ui.issues

import androidx.lifecycle.ViewModel
import com.githubrepo.data.repository.IssueRepository
import com.githubrepo.utils.lazyDeferred

class IssuesViewModel(
    repository: IssueRepository,
    forcefully: Boolean
) : ViewModel() {


    val issues by lazyDeferred {
        repository.getIssues(forcefully)
    }


}