package com.applocum.connecttomyhealth.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


abstract class BaseActivity : AppCompatActivity() {
    private var disposable:Disposable?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResourceId())
    }

    protected  abstract fun getLayoutResourceId(): Int

    protected  abstract fun handleInternetConnectivity(isConnect:Boolean?)

    fun handleInternet()
    {
        disposable=ReactiveNetwork.observeInternetConnectivity(5000, "www.google.com", 80, 2000)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { handleInternetConnectivity(it) }
    }

    override fun onResume() {
        super.onResume()
        handleInternet()
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }

}