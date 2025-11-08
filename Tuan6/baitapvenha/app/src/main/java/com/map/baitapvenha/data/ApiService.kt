package com.map.baitapvenha.data

import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("/api/researchUTH/tasks")
    suspend fun getTasks(): Response<TasksResponse>

    @GET("/api/researchUTH/task/{id}")
    suspend fun getTask(@Path("id") id: Int): Response<TaskResponse>

    @DELETE("/api/researchUTH/task/{id}")
    suspend fun deleteTask(@Path("id") id: Int): Response<Unit>
}
