package ec.edu.puce.githubclient.services

import ec.edu.puce.githubclient.models.Repository
import ec.edu.puce.githubclient.models.RepositoryPayload
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("users/{username}/repos")
    suspend fun getRepositories(
        @Path("username") username: String,
        @Query(value = "sort") created : String = "created",
        @Query(value = "direction") direction : String = "desc",
        @Query(value = "affiliation") affiliation : String = "owner",
        @Query(value = "per_page") perPage: Int = 100,
        @Query(value = "t") t: String = "${System.currentTimeMillis()}",
    ): List<Repository>

    @POST( value = "/user/repos")
    suspend fun createRepository(
        @Body repository: RepositoryPayload
    ) : Repository
}

