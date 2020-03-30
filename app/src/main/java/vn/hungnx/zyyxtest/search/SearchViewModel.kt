package vn.hungnx.zyyxtest.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import vn.hungnx.zyyxtest.model.GithubRepoModel
import vn.hungnx.zyyxtest.model.SearchRepoResponse
import vn.hungnx.zyyxtest.network.ZyyxRepository

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    var lastSearch: String = ""
    var disposable: Disposable? = null
    var listRepoSearch = ArrayList<GithubRepoModel>()
    lateinit var repoSearchAdapter: RepoSearchAdapter
    var isPopular: Boolean = true
        set(value) {
            field = value
            searchRepo(lastSearch)
        }

    fun searchRepo(repo: String) {
        lastSearch = repo
        disposable?.dispose()
        ZyyxRepository.getInstance()
            .searchGithubRepo(repo, isPopular, object : Observer<SearchRepoResponse> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {
                    disposable = d
                }

                override fun onNext(t: SearchRepoResponse) {
                    listRepoSearch = ArrayList(t.items ?: arrayListOf())
                    repoSearchAdapter.updateListSearch(listRepoSearch)
                }

                override fun onError(e: Throwable) {
                }

            })
    }
}