package com.dicoding.usergithubapp.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.usergithubapp.*
import com.dicoding.usergithubapp.databinding.ActivityMainBinding
import com.dicoding.usergithubapp.ui.adapter.UserAdapter

class MainActivity : AppCompatActivity(), ViewStateCallback<List<User>> {

    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var userQuery: String
    private lateinit var viewModel: MainViewModel
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        userAdapter = UserAdapter()
        mainBinding.includeMainSearch.rvListUser.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        }

        mainBinding.searchView.apply {
            queryHint = resources.getString(R.string.search_hint)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    userQuery = query.toString()
                    clearFocus()
                    viewModel.searchUser(userQuery).observe(this@MainActivity, {
                        when (it) {
                            is Resource.Error -> onFailed(it.message)
                            is Resource.Loading -> onLoading()
                            is Resource.Success -> it.data?.let { it1 -> onSuccess(it1) }
                        }
                    })
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }
    }


    override fun onSuccess(data: List<User>) {
        userAdapter.setAllData(data)
        mainBinding.includeMainSearch.apply {
            ivSearchIcon.visibility = invisible
            tvMessage.visibility = invisible
            mainProgressBar.visibility = invisible
            rvListUser.visibility = visible
        }
    }

    override fun onLoading() {
        mainBinding.includeMainSearch.apply {
            ivSearchIcon.visibility = invisible
            tvMessage.visibility = invisible
            mainProgressBar.visibility = visible
            rvListUser.visibility = invisible
        }
    }

    override fun onFailed(message: String?) {
        mainBinding.includeMainSearch.apply {
            if (message == null) {
                ivSearchIcon.apply {
                    setImageResource(R.drawable.ic_baseline_search_off_24)
                    visibility = visible
                }
                tvMessage.apply {
                    text = resources.getString(R.string.user_not_found)
                    visibility = visible
                }
            } else {
                ivSearchIcon.apply {
                    setImageResource(R.drawable.ic_baseline_search_off_24)
                    visibility = visible
                }
                tvMessage.apply {
                    text = message
                    visibility = visible
                }
            }
            mainProgressBar.visibility = invisible
            rvListUser.visibility = invisible
        }
    }
}