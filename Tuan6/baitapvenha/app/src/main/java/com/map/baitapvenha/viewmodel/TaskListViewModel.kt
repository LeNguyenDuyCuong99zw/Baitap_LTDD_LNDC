package com.map.baitapvenha.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.map.baitapvenha.data.Task
import com.map.baitapvenha.repo.TaskRepository
import kotlinx.coroutines.launch

class TaskListViewModel(private val repo: TaskRepository = TaskRepository()) : ViewModel() {
    var isLoading by mutableStateOf(false)
        private set

    var tasks by mutableStateOf<List<Task>>(emptyList())
        private set

    var error by mutableStateOf<String?>(null)
        private set

    init {
        fetchTasks()
    }

    fun fetchTasks() {
        viewModelScope.launch {
            isLoading = true
            try {
                tasks = repo.getTasks()
                error = null
            } catch (t: Throwable) {
                error = t.message
            } finally {
                isLoading = false
            }
        }
    }
}
