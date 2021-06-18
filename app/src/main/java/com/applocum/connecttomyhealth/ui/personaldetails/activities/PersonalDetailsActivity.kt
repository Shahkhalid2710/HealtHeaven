package com.applocum.connecttomyhealth.ui.personaldetails.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.changeFont
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.exemptions.activities.ExemptionsActivity
import com.applocum.connecttomyhealth.ui.mygp.activities.GpServiceActivity
import com.applocum.connecttomyhealth.ui.photoid.activities.PhotoIdActivity
import com.applocum.connecttomyhealth.ui.profile.models.ProfileProgressResponse
import com.applocum.connecttomyhealth.ui.profile.presenters.ProfileProgressPresenter
import com.applocum.connecttomyhealth.ui.profiledetails.activities.ProfileDetailsActivity
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_personal_details.*
import kotlinx.android.synthetic.main.custom_progress.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PersonalDetailsActivity : BaseActivity(),ProfileProgressPresenter.View {

    override fun getLayoutResourceId(): Int = R.layout.activity_personal_details

    @Inject
    lateinit var profileProgressPresenter: ProfileProgressPresenter

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).component.inject(this)
        profileProgressPresenter.injectView(this)

        RxView.clicks(ivBack).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe { finish() }

        RxView.clicks(rlProfileddetails).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this, ProfileDetailsActivity::class.java))
                overridePendingTransition(0,0)
            }

        RxView.clicks(rlMyGp).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this, GpServiceActivity::class.java))
                overridePendingTransition(0,0)
            }

        RxView.clicks(rlExemptions).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this, ExemptionsActivity::class.java))
                overridePendingTransition(0,0)
            }

        RxView.clicks(rlPhotoId).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this,PhotoIdActivity::class.java))
                overridePendingTransition(0,0)
            }
    }

    override fun displayProgressErrorMessage(message: String) {
        val snackBar = Snackbar.make(llPersonalDetails,message, Snackbar.LENGTH_LONG)
        snackBar.changeFont()
        val snackView = snackBar.view
        snackView.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
        snackBar.show()
    }

    override fun viewProfileProgress(isShow: Boolean) {
        progress.visibility=if (isShow) View.VISIBLE else View.GONE
    }

    override fun profileProgressDetail(progressResponse: ProfileProgressResponse) {
        if (progressResponse.profile_progress.my_gp)
        {
            ivMyGPWarning.visibility=View.GONE
        } else
        {
            ivMyGPWarning.visibility=View.VISIBLE
        }

        if (progressResponse.profile_progress.exemption)
        {
            ivExemptionWarning.visibility=View.GONE
        } else
        {
            ivExemptionWarning.visibility=View.VISIBLE
        }
        if (progressResponse.profile_progress.photo_id)
        {
            ivPhotoIDWarning.visibility=View.GONE

        } else
        {
            ivPhotoIDWarning.visibility=View.VISIBLE
        }

        if (progressResponse.profile_progress.my_bio)
        {
            ivProfileDetailsWarning.visibility=View.GONE
        } else
        {
            ivProfileDetailsWarning.visibility=View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        profileProgressPresenter.trackProfileProgress()
    }
}