package com.githubrepo.data.network.response

import com.githubrepo.data.db.entities.Issue


data class IssueResponse(
    val issues: List<Issue>
)