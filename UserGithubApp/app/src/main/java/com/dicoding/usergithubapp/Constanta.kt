package com.dicoding.usergithubapp

import androidx.annotation.StringRes

object Constanta {

    const val EXTRA_USER = "EXTRA_USER"

    const val EXTRA_NAME = "EXTRA_NAME"

    @StringRes
    val TAB_TITLES = intArrayOf(
        R.string.followers,
        R.string.following
    )

    const val GITHUB_TOKEN = "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"

    const val BASE_URL = "https://api.github.com/"
}