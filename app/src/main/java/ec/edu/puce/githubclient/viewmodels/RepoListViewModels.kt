package ec.edu.puce.githubclient.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ec.edu.puce.githubclient.models.Repository
import ec.edu.puce.githubclient.services.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val GITHUB_USERNAME = "FalconLD"

class RepoListViewModels : ViewModel(){
    private val _repos = MutableStateFlow< List<Repository>>(value = emptyList())
    val repos : StateFlow<List<Repository>> = _repos.asStateFlow()

    private val _isLoading = MutableStateFlow(value = false)
    val isLoading : StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMsg = MutableStateFlow<String?>(value = null)
    val errorMsg : StateFlow<String?> = _errorMsg.asStateFlow()

    init {
        fetchRepos()
    }

    fun fetchRepos (){
        viewModelScope.launch {
            _isLoading.value = true
            _errorMsg.value = null
            try{
                _repos.value =
                    RetrofitClient.apiService.getRepositories(GITHUB_USERNAME)
            } catch (e: Exception){
                _errorMsg.value = "Error al cargar repositorios: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
