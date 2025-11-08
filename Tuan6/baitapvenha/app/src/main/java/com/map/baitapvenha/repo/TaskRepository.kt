package com.map.baitapvenha.repo

import com.map.baitapvenha.data.ApiService
import com.map.baitapvenha.data.RetrofitClient
import com.map.baitapvenha.data.Task

class TaskRepository(private val api: ApiService = RetrofitClient.api) {
    suspend fun getTasks(): List<Task> {
        val resp = api.getTasks()
        if (resp.isSuccessful) {
            return resp.body()?.data ?: emptyList()
        }
        return emptyList()
    }

    suspend fun getTask(id: Int): Task? {
        val resp = api.getTask(id)
        if (resp.isSuccessful) {
            return resp.body()?.data
        }
        return null
    }

    suspend fun deleteTask(id: Int): Boolean {
        val resp = api.deleteTask(id)
        return resp.isSuccessful
    }
}
