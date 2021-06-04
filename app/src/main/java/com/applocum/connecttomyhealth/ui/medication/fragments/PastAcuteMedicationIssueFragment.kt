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
import kotlinx.android.synthetic.main.fragment_past_acute_medication_issue.view.*

class PastAcuteMedicationIssueFragment : Fragment() {
    var mListPastAcuteMedication:ArrayList<Medication> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v= inflater.inflate(R.layout.fragment_past_acute_medication_issue, container, false)

        if (mListPastAcuteMedication.isEmpty())
        {
            v.noMedication.visibility=View.VISIBLE
            v.rvPastMedication.visibility=View.GONE
        }
        else
        {
            v.noMedication.visibility=View.GONE
            v.rvPastMedication.visibility=View.VISIBLE
        }
        v.rvPastMedication.layoutManager= LinearLayoutManager(requireActivity())
        v.rvPastMedication.adapter= MedicationAdapter(requireActivity(),mListPastAcuteMedication)

        return v
    }
}