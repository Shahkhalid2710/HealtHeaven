package com.applocum.connecttomyhealth.ui.medicalhistory.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.medicalhistory.activities.AddMedicalHistoryActivity
import com.applocum.connecttomyhealth.ui.medicalhistory.adapters.ActiveMedicalHistoryAdapter
import com.applocum.connecttomyhealth.ui.medicalhistory.models.*
import com.applocum.connecttomyhealth.ui.medicalhistory.presenters.MedicalPresenter
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.custom_loader_progress.view.*
import kotlinx.android.synthetic.main.custom_medical_history.view.*
import kotlinx.android.synthetic.main.fragment_active_medical_history.*
import kotlinx.android.synthetic.main.fragment_active_medical_history.view.*
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

        presenter.activeMedicalHistory()

        return v
    }

    override fun displayMessage(message: String) {}

    override fun getDiseaseList(list: ArrayList<Medical>) {}

    override fun viewProgress(isShow: Boolean) {
        v.progress.visibility = if (isShow) View.VISIBLE else View.GONE
    }

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

    override fun onResume() {
        presenter.activeMedicalHistory()
        super.onResume()
    }
}