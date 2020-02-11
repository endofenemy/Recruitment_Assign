package com.githubrepo.ui.comments

import androidx.lifecycle.ViewModel
import com.githubrepo.data.repository.IssueRepository
import com.githubrepo.utils.lazyDeferred

class CommentsViewModel(
    repository: IssueRepository,
    commentNumber: String
) : ViewModel() {

    val comments by lazyDeferred {
        repository.getComments(commentNumber)
    }
}