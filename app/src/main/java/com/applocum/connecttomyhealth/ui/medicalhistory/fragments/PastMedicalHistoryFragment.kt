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
import com.applocum.connecttomyhealth.ui.medicalhistory.adapters.PastMedicalHistoryAdapter
import com.applocum.connecttomyhealth.ui.medicalhistory.models.FalseMedicalHistory
import com.applocum.connecttomyhealth.ui.medicalhistory.models.Medical
import com.applocum.connecttomyhealth.ui.medicalhistory.models.MedicalHistory
import com.applocum.connecttomyhealth.ui.medicalhistory.models.TrueMedicalHistory
import com.applocum.connecttomyhealth.ui.medicalhistory.presenters.MedicalPresenter
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.custom_loader_progress.view.*
import kotlinx.android.synthetic.main.custom_medical_history.view.*
import kotlinx.android.synthetic.main.fragment_past_medical_history.*
import kotlinx.android.synthetic.main.fragment_past_medical_history.view.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PastMedicalHistoryFragment : Fragment(), MedicalPresenter.View {

    @Inject
    lateinit var presenter: MedicalPresenter

    lateinit var v: View

    @SuppressLint("CheckResult")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_past_medical_history, container, false)
        MyApplication.getAppContext().component.inject(this)
        presenter.injectView(this)

        RxView.clicks(v.layoutNotfoundPastMedicalHistory.btnAddMedicalHistory).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(requireActivity(), AddMedicalHistoryActivity::class.java))
            }

        presenter.pastMedicalHistory()

        return v
    }

    override fun displayMessage(message: String) {}

    override fun getDiseaseList(list: ArrayList<Medical>) {}

    override fun viewProgress(isShow: Boolean) {
        v.progress.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun viewMedicalProgress(isShow: Boolean) {}

    override fun sendMedicalHistoryData(medicalHistory: MedicalHistory) {}

    override fun showActiveMedicalHistory(trueMedicalHistory: ArrayList<TrueMedicalHistory>) {}

    override fun showPastMedicalHistory(falseMedicalHistory: ArrayList<FalseMedicalHistory>) {
        if (falseMedicalHistory.isEmpty())
        {
            layoutNotfoundPastMedicalHistory.visibility=View.VISIBLE
            rvPastMedicalHistory.visibility=View.GONE
        }else
        {
            layoutNotfoundPastMedicalHistory.visibility=View.GONE
            rvPastMedicalHistory.visibility=View.VISIBLE
        }

        rvPastMedicalHistory.layoutManager = LinearLayoutManager(requireActivity())
        rvPastMedicalHistory.adapter = PastMedicalHistoryAdapter(requireActivity(), falseMedicalHistory)
    }

    override fun onResume() {
        presenter.pastMedicalHistory()
        super.onResume()
    }
}