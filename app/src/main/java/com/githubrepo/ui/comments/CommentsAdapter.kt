package com.githubrepo.ui.comments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.githubrepo.R
import com.githubrepo.data.db.entities.Comments
import com.githubrepo.databinding.ItemCommentsBinding

class CommentsAdapter(private val comments: List<Comments>) :
    RecyclerView.Adapter<CommentsAdapter.CommentsHolder>() {
    inner class CommentsHolder(val binding: ItemCommentsBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsHolder {
        return CommentsHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_comments,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = comments.size

    override fun onBindViewHolder(holder: CommentsHolder, position: Int) {
        holder.binding.commentsModel = comments[position]
    }
}