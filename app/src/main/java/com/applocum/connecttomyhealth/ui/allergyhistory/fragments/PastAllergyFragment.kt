package com.applocum.connecttomyhealth.ui.allergyhistory.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.changeFont
import com.applocum.connecttomyhealth.ui.allergyhistory.activities.AddAllergyActivity
import com.applocum.connecttomyhealth.ui.allergyhistory.adapters.PastAllergyHistoryAdapter
import com.applocum.connecttomyhealth.ui.allergyhistory.models.FalseAllergy
import com.applocum.connecttomyhealth.ui.allergyhistory.models.TrueAllergy
import com.applocum.connecttomyhealth.ui.allergyhistory.presenters.AllergyHistoryPresenter
import com.applocum.connecttomyhealth.ui.securitycheck.activities.SecurityActivity
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.custom_allergy_xml.view.*
import kotlinx.android.synthetic.main.custom_no_internet.view.*
import kotlinx.android.synthetic.main.custom_top_progress.view.*
import kotlinx.android.synthetic.main.fragment_past_allergy.*
import kotlinx.android.synthetic.main.fragment_past_allergy.view.*
import kotlinx.android.synthetic.main.fragment_past_allergy.view.noInternet
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PastAllergyFragment : Fragment(), AllergyHistoryPresenter.View {
    @Inject
    lateinit var presenter: AllergyHistoryPresenter
    lateinit var v: View

    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_past_allergy, container, false)
        MyApplication.getAppContext().component.inject(this)
        presenter.injectView(this)

        RxView.clicks(v.layoutNotfoundPastAllergy.btnAddAllergy).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(requireActivity(),AddAllergyActivity::class.java))
                requireActivity().overridePendingTransition(0,0)
            }

        RxView.clicks(v.noInternet.tvRetry).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe {
                v.noInternet.visibility=View.GONE
                presenter.pastAllergy()
            }

        return v
    }

    override fun displaySuccessMessage(message: String) {}

    override fun displayErrorMessage(message: String) {
        val snackbar = Snackbar.make(llPastAllergy, message, Snackbar.LENGTH_LONG)
        val snackview = snackbar.view
        snackview.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.red))
        snackbar.show()
    }

    override fun showActiveAllergy(activeAllergy: ArrayList<TrueAllergy>) {}

    override fun showPastAllergy(pastAllergy: ArrayList<FalseAllergy>) {
        if (pastAllergy.isEmpty())
        {
            layoutNotfoundPastAllergy.visibility=View.VISIBLE
            rvPastAllergy.visibility=View.GONE
        }else
        {
            layoutNotfoundPastAllergy.visibility=View.GONE
            rvPastAllergy.visibility=View.VISIBLE
        }
        rvPastAllergy.layoutManager = LinearLayoutManager(requireActivity())
        rvPastAllergy.adapter = PastAllergyHistoryAdapter(requireActivity(), pastAllergy)
    }

    override fun viewProgress(isShow: Boolean) {
        v.progress.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun viewAllergyProgress(isShow: Boolean) {}

    override fun noInternetConnection(isConnect: Boolean) {
        if (!isConnect){
            v.rvPastAllergy.visibility=View.GONE
            v.noInternet.visibility=View.VISIBLE

            val snackBar = Snackbar.make(llPastAllergy,R.string.no_internet, Snackbar.LENGTH_LONG)
            snackBar.changeFont()
            val snackView = snackBar.view
            snackView.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.red))
            snackBar.show()
        }else {
            v.rvPastAllergy.visibility=View.VISIBLE
            v.noInternet.visibility=View.GONE
        }
    }

    override fun sessionExpired(message: String) {
        Toast.makeText(requireActivity(),message, Toast.LENGTH_SHORT).show()
        startActivity(Intent(requireActivity(),SecurityActivity::class.java))
        requireActivity().finish()
    }

    override fun onResume() {
        super.onResume()
        presenter.pastAllergy()
    }
}