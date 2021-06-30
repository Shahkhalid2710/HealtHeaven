package com.applocum.connecttomyhealth.ui.medicalhistory.fragments

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
import com.applocum.connecttomyhealth.ui.medicalhistory.activities.AddMedicalHistoryActivity
import com.applocum.connecttomyhealth.ui.medicalhistory.adapters.PastMedicalHistoryAdapter
import com.applocum.connecttomyhealth.ui.medicalhistory.models.FalseMedicalHistory
import com.applocum.connecttomyhealth.ui.medicalhistory.models.Medical
import com.applocum.connecttomyhealth.ui.medicalhistory.models.MedicalHistory
import com.applocum.connecttomyhealth.ui.medicalhistory.models.TrueMedicalHistory
import com.applocum.connecttomyhealth.ui.medicalhistory.presenters.MedicalPresenter
import com.applocum.connecttomyhealth.ui.securitycheck.activities.SecurityActivity
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.custom_medical_history.view.*
import kotlinx.android.synthetic.main.custom_no_internet.view.*
import kotlinx.android.synthetic.main.custom_top_progress.view.*
import kotlinx.android.synthetic.main.fragment_past_medical_history.*
import kotlinx.android.synthetic.main.fragment_past_medical_history.view.*
import kotlinx.android.synthetic.main.fragment_past_medical_history.view.noInternet
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PastMedicalHistoryFragment : Fragment(), MedicalPresenter.View {

    @Inject
    lateinit var presenter: MedicalPresenter

    lateinit var v: View

    lateinit var pastMedicalHistoryAdapter: PastMedicalHistoryAdapter

    @SuppressLint("CheckResult")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_past_medical_history, container, false)
        MyApplication.getAppContext().component.inject(this)
        presenter.injectView(this)


        RxView.clicks(v.layoutNotfoundPastMedicalHistory.btnAddMedicalHistory).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(requireActivity(), AddMedicalHistoryActivity::class.java))
                requireActivity().overridePendingTransition(0,0)
            }

        RxView.clicks(v.noInternet.tvRetry).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                v.noInternet.visibility=View.GONE
                presenter.pastMedicalHistory()
            }
        return v
    }

    override fun displayMessage(message: String) {}

    override fun getDiseaseList(list: ArrayList<Medical>) {}

    override fun viewMedicalProgress(isShow: Boolean) {
        v.progress.visibility=if (isShow) View.VISIBLE else View.GONE
    }

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

        pastMedicalHistoryAdapter= PastMedicalHistoryAdapter(requireActivity(),falseMedicalHistory)
        v.rvPastMedicalHistory.layoutManager=LinearLayoutManager(requireActivity())
        v.rvPastMedicalHistory.adapter=pastMedicalHistoryAdapter

    }

    override fun noInternet(isConnect: Boolean) {
        if (!isConnect){
            v.rvPastMedicalHistory.visibility=View.GONE
            v.noInternet.visibility=View.VISIBLE

            val snackBar = Snackbar.make(llPastMedicalHistory,R.string.no_internet, Snackbar.LENGTH_LONG)
            snackBar.changeFont()
            val snackView = snackBar.view
            snackView.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.red))
            snackBar.show()
        }else {
            v.rvPastMedicalHistory.visibility=View.VISIBLE
            v.noInternet.visibility=View.GONE
        }
    }

    override fun showProgress() {}

    override fun hideProgress() {}

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
        presenter.pastMedicalHistory()
    }
}