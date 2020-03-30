package vn.hungnx.zyyxtest.network

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import vn.hungnx.zyyxtest.model.SearchRepoResponse
import vn.hungnx.zyyxtest.model.SearchUserResponse

interface ZyyxApiService {

    @GET("/search/users")
    fun searchUserName(@Query("q") userName: String): Observable<SearchUserResponse>

    @GET("/search/repositories")
    fun searchRepo(@Query("q") repo: String, @Query("sort") sort: String): Observable<SearchRepoResponse>
}