package com.dicoding.usergithubapp.ui.following

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.usergithubapp.Resource
import com.dicoding.usergithubapp.RetrofitService
import com.dicoding.usergithubapp.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel : ViewModel() {

    private val retrofit = RetrofitService.create()
    private val listUser = MutableLiveData<Resource<List<User>>>()

    fun getUserFollowing(username: String): LiveData<Resource<List<User>>> {
        listUser.postValue(Resource.Loading())
        retrofit.getUserFollowing(username).enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                val list = response.body()
                if (list.isNullOrEmpty())
                    listUser.postValue(Resource.Error(null))
                else
                    listUser.postValue(Resource.Success(list))
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                listUser.postValue(Resource.Error(t.message))
            }
        })
        return listUser
    }

}