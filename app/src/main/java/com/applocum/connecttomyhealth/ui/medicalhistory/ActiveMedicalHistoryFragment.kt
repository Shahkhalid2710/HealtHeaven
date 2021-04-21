package com.applocum.connecttomyhealth.ui.medicalhistory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.medicalhistory.adapters.MedicalHistoryAdapter
import com.applocum.connecttomyhealth.ui.medicalhistory.models.MedicalHistory
import kotlinx.android.synthetic.main.fragment_active_medical_history.view.*

class ActiveMedicalHistoryFragment : Fragment() {
    var mListActiveMedicalHistory:ArrayList<MedicalHistory> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v= inflater.inflate(R.layout.fragment_active_medical_history, container, false)

        val medicalHistory1=MedicalHistory("Neoplasm of abducens nerve","January 2020")
        val medicalHistory2=MedicalHistory("Neoplasm of junctional region of epiglottis","March 2020")
        val medicalHistory3=MedicalHistory("Jugular lymphadenopathy","January 2020")

        mListActiveMedicalHistory.add(medicalHistory1)
        mListActiveMedicalHistory.add(medicalHistory2)
        mListActiveMedicalHistory.add(medicalHistory3)
        v.rvActiveMedicalHistory.layoutManager=LinearLayoutManager(requireActivity())
        v.rvActiveMedicalHistory.adapter=MedicalHistoryAdapter(requireActivity(),mListActiveMedicalHistory)

        return v
    }

}