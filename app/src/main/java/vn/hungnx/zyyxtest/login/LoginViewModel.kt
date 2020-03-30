package vn.hungnx.zyyxtest.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import vn.hungnx.zyyxtest.model.SearchUserResponse
import vn.hungnx.zyyxtest.network.ZyyxRepository

class LoginViewModel(application: Application) : AndroidViewModel(application){

    val liveDataUsername = MutableLiveData<Boolean>(false)
    val liveDataPassword = MutableLiveData<Boolean>(false)

    fun checkUserName(username:String){
        if (username.isEmpty()){
            liveDataUsername.postValue(false)
        }else{
            ZyyxRepository.getInstance().searchGithubUserName(username,object :Observer<SearchUserResponse>{
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: SearchUserResponse) {
                    if (t.total_count>0){
                        liveDataUsername.postValue(true)
                    }else{
                        liveDataUsername.postValue(false)
                    }
                }

                override fun onError(e: Throwable) {
                    liveDataUsername.postValue(false)
                }

            })
        }
    }

    fun checkPassword(password:String){
        if (password.length==6){
            val char = password[0]
            for (i in password.indices){
                if (password[i]!=char){
                    liveDataPassword.postValue(true)
                    return
                }
            }
            liveDataPassword.postValue(false)
        }else {
            liveDataPassword.postValue(false)
        }
    }
}