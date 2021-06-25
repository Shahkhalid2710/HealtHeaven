package com.applocum.connecttomyhealth.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.home.fragments.HomeFragment
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

  /*  private var backPressed:Long=0

    private fun checkScreenForExit(): Boolean = if (getLayoutResourceId() == R.layout.activity_bottomnavigationview) {
        true
    } else if (
        getLayoutResourceId() == R.layout.fragment_home ||
        getLayoutResourceId() == R.layout.fragment_notification ||
        getLayoutResourceId() == R.layout.fragment_appointment ||
        getLayoutResourceId() == R.layout.fragment_profile)
    {
        val intent=Intent(this,HomeFragment::class.java)
        startActivity(intent)
        Log.d("CHeckdataa","->"+backPressed)
        false
    } else {
        super.onBackPressed()
        false
    }

    override fun onBackPressed() {
        if (checkScreenForExit()) {
            if (backPressed + 2000 > System.currentTimeMillis())
                super.onBackPressed()
            else Toast.makeText(this,"Press once again to exit!", Toast.LENGTH_LONG).show()
            backPressed = System.currentTimeMillis()
        } else {
            super.onBackPressed()
        }
    }*/
}