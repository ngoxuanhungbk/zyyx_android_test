package vn.hungnx.zyyxtest

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import vn.hungnx.zyyxtest.model.GithubRepoModel

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val listRepoFav = ArrayList<GithubRepoModel>()
    fun addRepoDetail(repoModel: GithubRepoModel) {
        if (checkRepoContains(repoModel)==null){
            listRepoFav.add(repoModel)
        }
    }

    fun removeRepoDetail(repoModel: GithubRepoModel){
        checkRepoContains(repoModel)?.let {
            listRepoFav.remove(it)
        }
    }

    fun checkRepoContains(repoModel: GithubRepoModel):GithubRepoModel?{
        return listRepoFav.find { item -> item.id == repoModel.id }
    }

    fun logout(){
        listRepoFav.clear()
    }
}