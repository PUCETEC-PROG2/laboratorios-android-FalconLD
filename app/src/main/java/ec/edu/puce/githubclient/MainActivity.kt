package ec.edu.puce.githubclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import ec.edu.puce.githubclient.models.Repository
import ec.edu.puce.githubclient.ui.screens.RepoForm
import ec.edu.puce.githubclient.ui.screens.RepoList
import ec.edu.puce.githubclient.ui.theme.GithubClientTheme
import ec.edu.puce.githubclient.viewmodels.RepoFormViewModels
import ec.edu.puce.githubclient.viewmodels.RepoListViewModels

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GithubClientTheme {
                var currentScreen by remember { mutableStateOf("repoList") }
                var selectedRepo by remember { mutableStateOf<Repository?>(null) }

                val listViewModel: RepoListViewModels = viewModel()
                val formViewModel: RepoFormViewModels = viewModel()

                when (currentScreen) {
                    "repoList" -> RepoList(
                        viewModel = listViewModel,
                        onNavigateToForm = {
                            selectedRepo = null
                            currentScreen = "repoForm"
                        },
                        onNavigateToEdit = { repo ->
                            selectedRepo = repo
                            currentScreen = "repoEdit"
                        },
                    )
                    "repoForm" -> RepoForm(
                        isEditMode = false,
                        onBackClick = { currentScreen = "repoList" },
                        onSaveSuccess = {
                            currentScreen = "repoList"
                            listViewModel.fetchRepos()
                        },
                        viewModel = formViewModel,
                    )
                    "repoEdit" -> selectedRepo?.let { repo ->
                        RepoForm(
                            isEditMode = true,
                            repositoryToEdit = repo,
                            onBackClick = { currentScreen = "repoList" },
                            onSaveSuccess = {
                                currentScreen = "repoList"
                                listViewModel.fetchRepos()
                            },
                            viewModel = formViewModel,
                        )
                    }
                }
            }
        }
    }
}
