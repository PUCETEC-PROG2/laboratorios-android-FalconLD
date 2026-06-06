package ec.edu.puce.githubclient.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ec.edu.puce.githubclient.models.RepositoryPayload
import ec.edu.puce.githubclient.services.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RepoFormViewModels : ViewModel() {
    private val _isLoading = MutableStateFlow(value = false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMsg = MutableStateFlow<String?>(value = null)
    val errorMsg: StateFlow<String?> = _errorMsg.asStateFlow()

    private val _isSuccess = MutableStateFlow(value = false)
    val isSuccess: StateFlow<Boolean> = _isSuccess.asStateFlow()

    fun createRepo(name: String, description: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMsg.value = null
            try {
                val repoBody = RepositoryPayload(name, description)
                RetrofitClient.apiService.createRepository(repository = repoBody)
                _isSuccess.value = true
            } catch (e: Exception) {
                _errorMsg.value = "Error al cargar repositorio: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateRepo(
        owner: String,
        currentRepoName: String,
        newName: String,
        description: String,
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMsg.value = null
            _isSuccess.value = false
            try {
                val payload = RepositoryPayload(
                    name = newName,
                    description = description.ifBlank { null },
                )
                RetrofitClient.apiService.updateRepository(owner, currentRepoName, payload)
                _isSuccess.value = true
            } catch (e: Exception) {
                _errorMsg.value = "Error al actualizar repositorio: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetFormState() {
        _errorMsg.value = null
        _isSuccess.value = false
    }

    fun resetSuccess() {
        _isSuccess.value = false
    }
    fun resetError() {
        _errorMsg.value = null
    }
}
