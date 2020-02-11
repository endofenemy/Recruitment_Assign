package com.githubrepo.ui.issues

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.githubrepo.R
import com.githubrepo.data.db.entities.Issue
import com.githubrepo.databinding.ItemIssueBinding
import com.githubrepo.ui.comments.CommentsActivity
import java.util.*


private var currentPosition: Int? = null
private lateinit var context: Context

class IssuesAdapter(
    private val issueList: List<Issue>
) : RecyclerView.Adapter<IssuesAdapter.RepositoryHolder>() {

    inner class RepositoryHolder(
        val binding: ItemIssueBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryHolder {
        context = parent.context
        return RepositoryHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_issue,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = issueList.size

    override fun onBindViewHolder(holder: RepositoryHolder, position: Int) {
        holder.binding.issue = issueList[position]

        holder.binding.root.setOnClickListener {
            if (issueList[position].comments!! > 0) {
                Intent(holder.binding.root.context, CommentsActivity::class.java)
                    .apply {
                        this.putExtra("issue", issueList[position].title)
                        this.putExtra("number", issueList[position].number.toString())
                        holder.binding.root.context.startActivity(this)
                    }
            }
        }
    }

    fun sortByComments() {
        Collections.sort(
            issueList
        ) { t, t2 -> t2.comments!!.compareTo(t.comments!!) }

        notifyDataSetChanged()
    }

    fun sortByTitle() {
        Collections.sort(
            issueList
        ) { t, t2 -> t.title!!.compareTo(t2.title!!) }
        notifyDataSetChanged()
    }
}