package com.githubrepo.ui.comments

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.githubissue.data.db.AppDatabase
import com.githubrepo.R
import com.githubrepo.data.network.MyApi
import com.githubrepo.data.network.NetworkConnectionInterceptor
import com.githubrepo.data.preferences.PreferenceProvider
import com.githubrepo.data.repository.IssueRepository
import com.githubrepo.utils.Coroutines
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.error_layout.*
import kotlinx.android.synthetic.main.placeholder_layout.*

class CommentsActivity : AppCompatActivity() {

    private lateinit var viewModel: CommentsViewModel
    private lateinit var factory: CommentsViewModelFactory
    private lateinit var repository: IssueRepository
    private lateinit var db: AppDatabase
    private lateinit var api: MyApi
    private lateinit var networkConnectionInterceptor: NetworkConnectionInterceptor
    private lateinit var preferenceProvider: PreferenceProvider

    private lateinit var commentsAdapter: CommentsAdapter
    lateinit var issueTitle: String
    lateinit var commentNumber: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initialize()

        Coroutines.main {
            val issues = viewModel.comments.await()
            issues.observe(this, Observer {
                shimmer_view_container.stopShimmerAnimation()
                shimmer_view_container.visibility = View.GONE
                Log.e("size", it.toString())
                if (it.isNotEmpty()) {
                    error_layout.visibility = View.GONE
                    // Setup Recycler view
                    recycler_view.layoutManager = LinearLayoutManager(this)
                    commentsAdapter = CommentsAdapter(it)
                    recycler_view.adapter = commentsAdapter
                    recycler_view.visibility = View.VISIBLE

                    title_issue.text = issueTitle
                    title_issue.visibility = View.VISIBLE
                } else {
                    error_layout.visibility = View.VISIBLE
                    title_issue.text = issueTitle
                    title_issue.visibility = View.GONE
                }
                refresh.isRefreshing = false
            })
        }
    }


    private fun initialize() {
        val intent = this.intent
        intent?.let {
            issueTitle = intent.getStringExtra("issue")
            commentNumber = intent.getStringExtra("number")
        }
        setupToolbar()
        preferenceProvider = PreferenceProvider(this)
        networkConnectionInterceptor = NetworkConnectionInterceptor(this)
        db = AppDatabase.invoke(this)
        api = MyApi(networkConnectionInterceptor)
        repository = IssueRepository(api, db, preferenceProvider)
        factory = CommentsViewModelFactory(repository, commentNumber)

        viewModel = ViewModelProviders.of(this, factory).get(CommentsViewModel::class.java)

        shimmer_view_container.startShimmerAnimation()

        header.text = "Comments"


        retry.setOnClickListener {
            Coroutines.io {
                repository.getComments(commentNumber)
            }
        }

        refresh.setOnRefreshListener {
            refresh.isRefreshing = true
            retry.performClick()
        }
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = ""
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return true
    }
}