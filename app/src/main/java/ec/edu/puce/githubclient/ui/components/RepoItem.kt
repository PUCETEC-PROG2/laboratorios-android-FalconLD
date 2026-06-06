package ec.edu.puce.githubclient.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ec.edu.puce.githubclient.models.GithubUser
import ec.edu.puce.githubclient.models.Repository
import ec.edu.puce.githubclient.ui.theme.PuceBlue
private const val SWIPE_ACTION_THRESHOLD_FRACTION = 0.4f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoItem(
    repository: Repository,
    onEditClick: (Repository) -> Unit = {},
    onDeleteClick: (Repository) -> Unit = {},
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            when (value) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    onEditClick(repository)
                    false
                }
                SwipeToDismissBoxValue.EndToStart -> {
                    onDeleteClick(repository)
                    false
                }
                else -> false
            }
        },
        positionalThreshold = { totalDistance ->
            totalDistance * SWIPE_ACTION_THRESHOLD_FRACTION
        },
    )

    val crossedThreshold = dismissState.progress >= SWIPE_ACTION_THRESHOLD_FRACTION
    val neutralBackground = MaterialTheme.colorScheme.surfaceVariant
    val activeDirection = dismissState.targetValue.takeIf {
        it != SwipeToDismissBoxValue.Settled && dismissState.progress > 0f
    } ?: dismissState.dismissDirection

    val backgroundColor by animateColorAsState(
        targetValue = when (activeDirection) {
            SwipeToDismissBoxValue.StartToEnd ->
                if (crossedThreshold) PuceBlue else neutralBackground
            SwipeToDismissBoxValue.EndToStart ->
                if (crossedThreshold) MaterialTheme.colorScheme.errorContainer else neutralBackground
            else -> neutralBackground
        },
        label = "swipeBackgroundColor",
    )

    val iconTint = when (activeDirection) {
        SwipeToDismissBoxValue.StartToEnd ->
            if (crossedThreshold) Color.White else MaterialTheme.colorScheme.primary
        SwipeToDismissBoxValue.EndToStart ->
            if (crossedThreshold) MaterialTheme.colorScheme.onErrorContainer
            else MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = true,
        enableDismissFromEndToStart = true,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor),
            ) {
                if (dismissState.progress > 0f) {
                    when (activeDirection) {
                        SwipeToDismissBoxValue.StartToEnd -> {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 24.dp),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Editar repositorio",
                                    tint = iconTint,
                                    modifier = Modifier.size(32.dp),
                                )
                            }
                        }
                        SwipeToDismissBoxValue.EndToStart -> {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 24.dp),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Eliminar repositorio",
                                    tint = iconTint,
                                    modifier = Modifier.size(32.dp),
                                )
                            }
                        }
                        else -> Unit
                    }
                }
            }
        },
        content = {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    AsyncImage(
                        model = repository.owner.avatarUrl,
                        contentDescription = "imagen de ${repository.name}",
                        modifier = Modifier.size(60.dp),
                        contentScale = ContentScale.Crop,
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = repository.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        if (!repository.description.isNullOrBlank()) {
                            Text(
                                text = repository.description,
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        if (!repository.language.isNullOrBlank()) {
                            Text(
                                text = repository.language,
                                style = MaterialTheme.typography.labelSmall,
                            )
                        }
                    }
                }
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
fun RepoItemPreview() {
    val repository = Repository(
        id = "12345",
        name = "repositorio de android",
        description = "repositorio de android ",
        language = "kotlin",
        owner = GithubUser(
            id = "123",
            login = "FalconLD",
            avatarUrl = "https://avatars.githubusercontent.com/u/216222909?v=4",
        ),
    )
    RepoItem(repository)
}
