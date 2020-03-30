package vn.hungnx.zyyxtest.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class GithubRepoModel(
    val id: Long, val name: String, val full_name: String, val description: String?,
    val stargazers_count: Int, val forks: Int,
    val language: String?
) : Parcelable {
    fun starToString(): String {
        return stargazers_count.toString()
    }

    fun forkToString(): String {
        return forks.toString()
    }
}