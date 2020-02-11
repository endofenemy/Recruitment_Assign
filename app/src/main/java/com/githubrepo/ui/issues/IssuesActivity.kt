package com.githubrepo.ui.issues

import android.os.Bundle
import android.view.Menu
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

class IssuesActivity : AppCompatActivity() {

    private lateinit var factory: IssuesViewModelFactory
    private lateinit var repository: IssueRepository
    private lateinit var db: AppDatabase
    private lateinit var api: MyApi
    private lateinit var networkConnectionInterceptor: NetworkConnectionInterceptor
    private lateinit var preferenceProvider: PreferenceProvider
    private lateinit var issuesAdapter: IssuesAdapter

    private lateinit var viewModel: IssuesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preferenceProvider = PreferenceProvider(this)
        networkConnectionInterceptor = NetworkConnectionInterceptor(this)
        db = AppDatabase.invoke(this)
        api = MyApi(networkConnectionInterceptor)
        repository = IssueRepository(api, db, preferenceProvider)
        factory = IssuesViewModelFactory(repository, false)

        viewModel = ViewModelProviders.of(this, factory).get(IssuesViewModel::class.java)

        setupToolbar()
        shimmer_view_container.startShimmerAnimation()

        Coroutines.main {
            val issues = viewModel.issues.await()
            issues.observe(this, Observer {
                shimmer_view_container.stopShimmerAnimation()
                shimmer_view_container.visibility = View.GONE

                if (it.isNotEmpty()) {
                    error_layout.visibility = View.GONE
                    // Setup Recycler view
                    recycler_view.layoutManager = LinearLayoutManager(this)
                    issuesAdapter = IssuesAdapter(it)
                    recycler_view.adapter = issuesAdapter
                    recycler_view.visibility = View.VISIBLE
                } else {
                    error_layout.visibility = View.VISIBLE
                }
                refresh.isRefreshing = false
            })
        }

        retry.setOnClickListener {
            Coroutines.io {
                repository.fetchIssues(true)
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
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sort_comments -> {
                issuesAdapter?.let {
                    issuesAdapter.sortByComments()
                }
            }
            R.id.sort_title -> {
                issuesAdapter?.let {
                    issuesAdapter.sortByTitle()
                }
            }
        }
        return true
    }

}
