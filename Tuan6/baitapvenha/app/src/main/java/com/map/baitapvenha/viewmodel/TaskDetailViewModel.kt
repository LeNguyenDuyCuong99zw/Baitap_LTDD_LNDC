 package com.map.baitapvenha.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.map.baitapvenha.data.Task
import com.map.baitapvenha.repo.TaskRepository
import kotlinx.coroutines.launch

class TaskDetailViewModel(private val repo: TaskRepository = TaskRepository()) : ViewModel() {
    var isLoading by mutableStateOf(false)
        private set

    var task by mutableStateOf<Task?>(null)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    fun load(id: Int) {
        viewModelScope.launch {
            isLoading = true
            try {
                task = repo.getTask(id)
                error = if (task == null) "Not found" else null
            } catch (t: Throwable) {
                error = t.message
            } finally {
                isLoading = false
            }
        }
    }

    fun delete(id: Int, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            isLoading = true
            try {
                val ok = repo.deleteTask(id)
                onComplete(ok)
            } catch (t: Throwable) {
                onComplete(false)
            } finally {
                isLoading = false
            }
        }
    }
}
