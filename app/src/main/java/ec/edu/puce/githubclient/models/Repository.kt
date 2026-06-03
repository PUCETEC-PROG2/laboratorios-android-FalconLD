package ec.edu.puce.githubclient.models

import com.google.gson.annotations.SerializedName

data class Repository(
    val id: String,
    val name: String,
    val description: String?,
    val language: String?,
    val owner: GithubUser,
)
