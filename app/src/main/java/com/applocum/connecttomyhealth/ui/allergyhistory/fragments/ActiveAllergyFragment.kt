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
import com.applocum.connecttomyhealth.ui.allergyhistory.adapters.ActiveAllergyHistoryAdapter
import com.applocum.connecttomyhealth.ui.allergyhistory.models.FalseAllergy
import com.applocum.connecttomyhealth.ui.allergyhistory.models.TrueAllergy
import com.applocum.connecttomyhealth.ui.allergyhistory.presenters.AllergyHistoryPresenter
import com.applocum.connecttomyhealth.ui.securitycheck.activities.SecurityActivity
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.custom_allergy_xml.view.*
import kotlinx.android.synthetic.main.custom_loader_progress.view.progress
import kotlinx.android.synthetic.main.custom_no_internet.view.*
import kotlinx.android.synthetic.main.fragment_active_allergy.*
import kotlinx.android.synthetic.main.fragment_active_allergy.view.*
import kotlinx.android.synthetic.main.fragment_active_allergy.view.noInternet
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ActiveAllergyFragment : Fragment(),AllergyHistoryPresenter.View {
    @Inject
    lateinit var presenter: AllergyHistoryPresenter
    lateinit var v: View

    @SuppressLint("CheckResult")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_active_allergy, container, false)
        MyApplication.getAppContext().component.inject(this)
        presenter.injectView(this)

        RxView.clicks(v.layoutNotfoundActiveAllergy.btnAddAllergy).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(requireActivity(),AddAllergyActivity::class.java))
                requireActivity().overridePendingTransition(0,0)
            }

        RxView.clicks(v.noInternet.tvRetry).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe {
                v.noInternet.visibility=View.GONE
                presenter.activeAllergy()
            }

        return v
    }

    override fun displaySuccessMessage(message: String) {}

    override fun displayErrorMessage(message: String) {
        val snackbar = Snackbar.make(llActiveAllergy, message, Snackbar.LENGTH_LONG)
        val snackview = snackbar.view
        snackview.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.red))
        snackbar.show()
    }

    override fun showActiveAllergy(activeAllergy: ArrayList<TrueAllergy>) {
        if (activeAllergy.isEmpty())
        {
            layoutNotfoundActiveAllergy.visibility=View.VISIBLE
            rvActiveAllergy.visibility=View.GONE
        } else
        {
            layoutNotfoundActiveAllergy.visibility=View.GONE
            rvActiveAllergy.visibility=View.VISIBLE
        }
        rvActiveAllergy.layoutManager = LinearLayoutManager(requireActivity())
        rvActiveAllergy.adapter = ActiveAllergyHistoryAdapter(requireActivity(), activeAllergy)
    }

    override fun showPastAllergy(pastAllergy: ArrayList<FalseAllergy>) {}

    override fun viewProgress(isShow: Boolean) {
        v.progress.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun viewAllergyProgress(isShow: Boolean) {}

    override fun noInternetConnection(isConnect: Boolean) {
        if (!isConnect){
            v.rvActiveAllergy.visibility=View.GONE
            v.noInternet.visibility=View.VISIBLE

            val snackBar = Snackbar.make(llActiveAllergy,R.string.no_internet, Snackbar.LENGTH_LONG)
            snackBar.changeFont()
            val snackView = snackBar.view
            snackView.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.red))
            snackBar.show()
        }else {
            v.rvActiveAllergy.visibility=View.VISIBLE
            v.noInternet.visibility=View.GONE
        }
    }

    override fun sessionExpired(message: String) {
        Toast.makeText(requireActivity(),message, Toast.LENGTH_SHORT).show()
        val intent=Intent(requireActivity(),SecurityActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onResume() {
        super.onResume()
        presenter.activeAllergy()
    }
}