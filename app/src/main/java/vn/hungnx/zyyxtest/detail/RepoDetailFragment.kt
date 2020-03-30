package vn.hungnx.zyyxtest.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_repo_detail.*
import vn.hungnx.zyyxtest.MainViewModel
import vn.hungnx.zyyxtest.R
import vn.hungnx.zyyxtest.databinding.FragmentRepoDetailBinding
import vn.hungnx.zyyxtest.model.GithubRepoModel

class RepoDetailFragment : Fragment(){
    lateinit var binding:FragmentRepoDetailBinding
    lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_repo_detail,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        binding.repo = arguments?.getParcelable<GithubRepoModel>("repoDetail")
        btn_back.setOnClickListener {
            findNavController().popBackStack()
        }
        btn_delete.setOnClickListener {
            arguments?.getParcelable<GithubRepoModel>("repoDetail")?.let {
                mainViewModel.removeRepoDetail(it)
            }
            findNavController().popBackStack()
        }
    }
}