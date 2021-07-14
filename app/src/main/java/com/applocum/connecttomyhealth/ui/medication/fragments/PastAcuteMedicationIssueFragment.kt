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

        val medication1=Medication("Aciclovir 500mg/20ml solution for infusion vials","07 Jan 2019","10")
        val medication2=Medication("Anadin Paracetamol 500mg tablets","20 Jan 2019","20")
        val medication3=Medication("ActiLmphy class 1 (18-21mmHg) below knee closed toe lymphonedema garment petite extra large","12 Feb 2019","15")

        mListPastAcuteMedication.add(medication3)
        mListPastAcuteMedication.add(medication2)
        mListPastAcuteMedication.add(medication1)


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