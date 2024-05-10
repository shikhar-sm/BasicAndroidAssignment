package com.example.basicandroidassignment.ui

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.basicandroidassignment.data.NetworkDataApi
import com.example.basicandroidassignment.models.VideoModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class MainViewModel: ViewModel() {

    private val _videoList : MutableState<List<VideoModel>> = mutableStateOf(emptyList())
    val videoList : State<List<VideoModel>> = _videoList

    private fun getVideos(searchTerm: String) : Unit {
//        println(searchTerm)
        try {
            viewModelScope.launch {
                try {
                    val params = mutableMapOf<String, Any>(
                        "select" to "*",
                        "title" to "ilike.%$searchTerm%"
                    )
                    Log.d("params", params.toString())
                    val listResult = NetworkDataApi.networkDataService.getVideos(params)
                    Log.d("result of video list", listResult.toString())
                    _videoList.value = listResult
                } catch (e: IOException) {
                    Log.e("IO", e.toString())
                } catch (e: HttpException) {
                    Log.e("Http", e.toString())
                } catch (e: Exception) {
                    Log.e("General", e.toString())
                }
            }
        } catch (e: Exception) {
            Log.e("Unknown",e.toString())
        }
    }

    fun search(searchTerm: String) : Unit {
        getVideos(searchTerm)
    }

}