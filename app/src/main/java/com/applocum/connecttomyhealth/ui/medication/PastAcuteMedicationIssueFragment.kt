package com.applocum.connecttomyhealth.ui.medication

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
import kotlinx.android.synthetic.main.fragment_past_acute_medication_issue.view.*


class PastAcuteMedicationIssueFragment : Fragment() {
    var mListPastAcuteMedication:ArrayList<Medication> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v= inflater.inflate(R.layout.fragment_past_acute_medication_issue, container, false)

        val medication1=Medication("Aciclovir 500mg/20ml solution for infusion vials","07 Jan 2020","10")
        val medication2=Medication("Anadin Paracetamol 500mg tablets","20 Jan 2020","12")
        val medication3=Medication("Anadin Paracetamol 500mg tablets","20 Jan 2020","12")
        val medication4=Medication("Anadin Paracetamol 500mg tablets","20 Jan 2020","12")
        val medication5=Medication("Anadin Paracetamol 500mg tablets","20 Jan 2020","12")
        val medication6=Medication("Anadin Paracetamol 500mg tablets","20 Jan 2020","12")
        val medication7=Medication("Anadin Paracetamol 500mg tablets","20 Jan 2020","12")
        val medication8=Medication("Anadin Paracetamol 500mg tablets","20 Jan 2020","12")
        val medication9=Medication("Anadin Paracetamol 500mg tablets","20 Jan 2020","12")
        val medication10=Medication("Anadin Paracetamol 500mg tablets","20 Jan 2020","12")

        mListPastAcuteMedication.add(medication1)
        mListPastAcuteMedication.add(medication2)
        mListPastAcuteMedication.add(medication3)
        mListPastAcuteMedication.add(medication4)
        mListPastAcuteMedication.add(medication5)
        mListPastAcuteMedication.add(medication6)
        mListPastAcuteMedication.add(medication7)
        mListPastAcuteMedication.add(medication8)
        mListPastAcuteMedication.add(medication9)
        mListPastAcuteMedication.add(medication10)

        v.rvPastMedication.layoutManager= LinearLayoutManager(requireActivity())
        v.rvPastMedication.adapter= MedicationAdapter(requireActivity(),mListPastAcuteMedication)

        return v
    }
}