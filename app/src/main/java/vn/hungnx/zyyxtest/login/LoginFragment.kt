package vn.hungnx.zyyxtest.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_login.*
import vn.hungnx.zyyxtest.R
import vn.hungnx.zyyxtest.databinding.FragmentLoginBinding
import java.util.concurrent.TimeUnit

class LoginFragment : Fragment() {

    lateinit var loginViewModel: LoginViewModel
    private val canLogin = MutableLiveData<Boolean>(false)
    lateinit var binding:FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_login,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        loginViewModel.liveDataPassword.postValue(false)
        loginViewModel.liveDataUsername.postValue(false)
        RxSearchObservable.fromView(edt_username).debounce(500,TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object :Observer<String>{
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: String) {
                    //check user name
                    loginViewModel.checkUserName(t)
                }

                override fun onError(e: Throwable) {
                }

            })
        RxSearchObservable.fromView(edt_password).debounce(300,TimeUnit.MICROSECONDS)
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object :Observer<String>{
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: String) {
                    //check user name
                    loginViewModel.checkPassword(t)
                }

                override fun onError(e: Throwable) {
                }

            })
        canLogin.observe(viewLifecycleOwner,androidx.lifecycle.Observer {
            binding.canLogin = it
        })
        btn_login.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_search)
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loginViewModel.liveDataUsername.observe(viewLifecycleOwner,androidx.lifecycle.Observer {
            canLogin.postValue(loginViewModel.liveDataPassword.value?:false&&it)
        })
        loginViewModel.liveDataPassword.observe(viewLifecycleOwner,androidx.lifecycle.Observer {
            canLogin.postValue(loginViewModel.liveDataUsername.value?:false&&it)
        })
    }
}