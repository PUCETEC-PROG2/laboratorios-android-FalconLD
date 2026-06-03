package ec.edu.puce.githubclient.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ec.edu.puce.githubclient.models.GithubUser
import ec.edu.puce.githubclient.models.Repository


@Composable
fun RepoItem (
    repository: Repository
) {
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ){
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ){
            AsyncImage(
                model = repository.owner.avatarUrl,
                contentDescription = "imagen de ${repository.name}",
                modifier = Modifier.size(60.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = repository.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
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
}



@Preview(showBackground = true)
@Composable
fun RepoItemPreview(){
    val repository = Repository (
        id = "12345",
        name = "repositorio de android",
        description = "repositorio de android ",
        language = "kotlin",
        owner = GithubUser(
            id = "123",
            login = "FalconLD",
            avatarUrl = "https://avatars.githubusercontent.com/u/216222909?v=4"
        )

    )
    RepoItem(repository)
}
