package com.applocum.connecttomyhealth.ui.medicalhistory.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.changeFont
import com.applocum.connecttomyhealth.ui.medicalhistory.activities.AddMedicalHistoryActivity
import com.applocum.connecttomyhealth.ui.medicalhistory.adapters.ActiveMedicalHistoryAdapter
import com.applocum.connecttomyhealth.ui.medicalhistory.models.*
import com.applocum.connecttomyhealth.ui.medicalhistory.presenters.MedicalPresenter
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.custom_loader_progress.view.*
import kotlinx.android.synthetic.main.custom_medical_history.view.*
import kotlinx.android.synthetic.main.custom_no_internet.view.*
import kotlinx.android.synthetic.main.fragment_active_medical_history.*
import kotlinx.android.synthetic.main.fragment_active_medical_history.view.*
import kotlinx.android.synthetic.main.fragment_active_medical_history.view.noInternet
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ActiveMedicalHistoryFragment : Fragment(), MedicalPresenter.View {

    @Inject
    lateinit var presenter: MedicalPresenter

    lateinit var v: View

    @SuppressLint("CheckResult")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_active_medical_history, container, false)
        MyApplication.getAppContext().component.inject(this)
        presenter.injectView(this)

        RxView.clicks(v.layoutNotfoundActiveMedicalHistory.btnAddMedicalHistory).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(requireActivity(),AddMedicalHistoryActivity::class.java))
                requireActivity().overridePendingTransition(0,0)
            }

        RxView.clicks(v.noInternet.tvRetry).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe {
                v.noInternet.visibility=View.GONE
                presenter.activeMedicalHistory()
            }

        return v
    }

    override fun displayMessage(message: String) {}

    override fun getDiseaseList(list: ArrayList<Medical>) {}

    override fun viewMedicalProgress(isShow: Boolean) {}

    override fun sendMedicalHistoryData(medicalHistory: MedicalHistory) {}

    override fun showActiveMedicalHistory(trueMedicalHistory: ArrayList<TrueMedicalHistory>) {
        if (trueMedicalHistory.isEmpty())
        {
            layoutNotfoundActiveMedicalHistory.visibility=View.VISIBLE
            rvActiveMedicalHistory.visibility=View.GONE
        }else{
            layoutNotfoundActiveMedicalHistory.visibility=View.GONE
            rvActiveMedicalHistory.visibility=View.VISIBLE
        }

        rvActiveMedicalHistory.layoutManager = LinearLayoutManager(requireActivity())
        rvActiveMedicalHistory.adapter = ActiveMedicalHistoryAdapter(requireActivity(), trueMedicalHistory)
    }

    override fun showPastMedicalHistory(falseMedicalHistory: ArrayList<FalseMedicalHistory>) {}

    override fun noInternet(isConnect: Boolean) {
        if (!isConnect){
            v.rvActiveMedicalHistory.visibility=View.GONE
            v.noInternet.visibility=View.VISIBLE
            v.layoutNotfoundActiveMedicalHistory.visibility=View.GONE

            val snackBar = Snackbar.make(llActiveMedicalHistory,R.string.no_internet, Snackbar.LENGTH_LONG)
            snackBar.changeFont()
            val snackView = snackBar.view
            snackView.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.red))
            snackBar.show()
        }else {
            v.rvActiveMedicalHistory.visibility=View.VISIBLE
            v.noInternet.visibility=View.GONE
            v.layoutNotfoundActiveMedicalHistory.visibility=View.VISIBLE
        }
    }

    override fun showProgress() {

    }

    override fun hideProgress() {
    }

    override fun onResume() {
        super.onResume()
        presenter.activeMedicalHistory()
    }
}