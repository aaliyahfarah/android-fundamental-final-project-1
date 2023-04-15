package com.dicoding.usergithubapp.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.usergithubapp.Constanta.TAB_TITLES
import com.dicoding.usergithubapp.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.dicoding.usergithubapp.Constanta.EXTRA_USER
import com.dicoding.usergithubapp.Resource
import com.dicoding.usergithubapp.User
import com.dicoding.usergithubapp.ViewStateCallback
import com.dicoding.usergithubapp.detail.DetailViewModel
import com.dicoding.usergithubapp.ui.adapter.FollowPagerAdapter

class DetailActivity : AppCompatActivity(), ViewStateCallback<User?> {

    private lateinit var detailBinding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            elevation = 0f
        }

        val username = intent.getStringExtra(EXTRA_USER)

        viewModel.getDetailUser(username).observe(this, {
            when (it) {
                is Resource.Error -> onFailed(it.message)
                is Resource.Loading -> onLoading()
                is Resource.Success -> onSuccess(it.data)
            }
        })

        val pageAdapter = FollowPagerAdapter(this, username.toString())

        detailBinding.apply {
            viewPager.adapter = pageAdapter
            TabLayoutMediator(tabs, viewPager) { tabs, position ->
                tabs.text = resources.getString(TAB_TITLES[position])
            }.attach()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onSuccess(data: User?) {
        detailBinding.apply {
            detailProgressBar.visibility = invisible

            tvDetailNumberOfRepos.text = data?.repository.toString()
            tvDetailNumberOfFollowers.text = data?.follower.toString()
            tvDetailNumberOfFollowing.text = data?.following.toString()
            tvDetailName.text = data?.name
            tvDetailLocation.text = data?.location

            Glide.with(this@DetailActivity)
                .load(data?.avatar)
                .apply(RequestOptions.circleCropTransform())
                .into(ivDetailAvatar)

            supportActionBar?.title = data?.username
        }
    }

    override fun onLoading() {
        detailBinding.apply {
            detailProgressBar.visibility = visible
        }
    }

    override fun onFailed(message: String?) {
        detailBinding.apply {
            detailProgressBar.visibility = invisible
        }
    }
}