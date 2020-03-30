package vn.hungnx.zyyxtest.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_search.*
import vn.hungnx.zyyxtest.MainViewModel
import vn.hungnx.zyyxtest.R
import vn.hungnx.zyyxtest.databinding.FragmentSearchBinding
import vn.hungnx.zyyxtest.login.RxSearchObservable
import vn.hungnx.zyyxtest.model.GithubRepoModel
import vn.hungnx.zyyxtest.util.Utils
import java.util.concurrent.TimeUnit

class SearchFragment : Fragment(), OnClickItem {

    lateinit var binding: FragmentSearchBinding
    lateinit var mainViewModel: MainViewModel
    lateinit var repoFavAdapter: RepoFavAdapter
    lateinit var repoSearchAdapter: RepoSearchAdapter
    lateinit var searchViewModel: SearchViewModel
    val isFocusLiveData = MutableLiveData<Boolean>(false)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        repoFavAdapter = RepoFavAdapter(mainViewModel.listRepoFav, this)
        repoSearchAdapter =
            RepoSearchAdapter(searchViewModel.listRepoSearch, mainViewModel.listRepoFav, this)
        searchViewModel.repoSearchAdapter = repoSearchAdapter
        edt_search.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            isFocusLiveData.postValue(hasFocus)
        }
        recycler_view.layoutManager = LinearLayoutManager(activity!!)
        isFocusLiveData.observe(viewLifecycleOwner, Observer {
            if (!it) {
                Utils.hideKeyboard(activity!!)
                recycler_view.adapter = repoFavAdapter
            } else {
                recycler_view.adapter = repoSearchAdapter
            }
            binding.isFocus = it
        })
        btn_cancel.setOnClickListener {
            edt_search.clearFocus()
        }

        btn_logout.setOnClickListener {
            mainViewModel.logout()
            findNavController().navigate(R.id.action_search_to_login)
        }

        btn_popular.setOnClickListener {
            searchViewModel.isPopular = true
        }

        btn_recent.setOnClickListener {
            searchViewModel.isPopular = false
        }

        RxSearchObservable.fromView(edt_search).debounce(500, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : io.reactivex.Observer<String> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: String) {
                    //check user name
                    searchViewModel.searchRepo(t)
                }

                override fun onError(e: Throwable) {
                }

            })
    }

    override fun onClickItem(repoDetail: GithubRepoModel) {
        val action = SearchFragmentDirections.actionSearchToRepoDetail(repoDetail)
        findNavController().navigate(action)
    }

    override fun addOrRemoveItem(repoDetail: GithubRepoModel, isAdd: Boolean) {
        if (isAdd) mainViewModel.addRepoDetail(repoDetail)
        else mainViewModel.removeRepoDetail(repoDetail)
    }
}

interface OnClickItem {
    fun onClickItem(repoDetail: GithubRepoModel)
    fun addOrRemoveItem(repoDetail: GithubRepoModel, isAdd: Boolean)
}