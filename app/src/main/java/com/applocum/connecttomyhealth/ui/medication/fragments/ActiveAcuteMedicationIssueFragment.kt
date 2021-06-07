package com.applocum.connecttomyhealth.ui.medication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.medication.adapter.MedicationAdapter
import com.applocum.connecttomyhealth.ui.medication.models.Medication
import kotlinx.android.synthetic.main.fragment_active_acute_medication_issue.view.*


class ActiveAcuteMedicationIssueFragment : Fragment() {
    var mListActiveAcuteMedication:ArrayList<Medication> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v= inflater.inflate(R.layout.fragment_active_acute_medication_issue, container, false)

        if (mListActiveAcuteMedication.isEmpty())
        {
            v.noMedication.visibility=View.VISIBLE
            v.rvActiveMedication.visibility=View.GONE
        }
        else
        {
            v.noMedication.visibility=View.GONE
            v.rvActiveMedication.visibility=View.VISIBLE
        }

        v.rvActiveMedication.layoutManager=LinearLayoutManager(requireActivity())
        v.rvActiveMedication.adapter=MedicationAdapter(requireActivity(),mListActiveAcuteMedication)

        return v
    }
}