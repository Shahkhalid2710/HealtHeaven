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


class ActiveAcuteMedicationIssueFragment : Fragment() {
    var mListActiveAcuteMedication:ArrayList<Medication> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v= inflater.inflate(R.layout.fragment_active_acute_medication_issue, container, false)

        val medication1=Medication("Aciclovir 500mg/20ml solution for infusion vials","07 Jan 2020","10")
        val medication2=Medication("Anadin Paracetamol 500mg tablets","20 Jan 2020","12")
        val medication3=Medication("ActiLmphy class 1 (18-21mmHg) below knee closed toe lymphonedema garment petite extra large","12 Feb 2020","10")
        val medication4=Medication("ActiLmphy class 1 (18-21mmHg) below knee closed toe lymphonedema garment petite extra large","12 Feb 2020","10")
        val medication5=Medication("ActiLmphy class 1 (18-21mmHg) below knee closed toe lymphonedema garment petite extra large","12 Feb 2020","10")
        val medication6=Medication("ActiLmphy class 1 (18-21mmHg) below knee closed toe lymphonedema garment petite extra large","12 Feb 2020","10")
        val medication7=Medication("ActiLmphy class 1 (18-21mmHg) below knee closed toe lymphonedema garment petite extra large","12 Feb 2020","10")
        val medication8=Medication("ActiLmphy class 1 (18-21mmHg) below knee closed toe lymphonedema garment petite extra large","12 Feb 2020","10")

        mListActiveAcuteMedication.add(medication1)
        mListActiveAcuteMedication.add(medication2)
        mListActiveAcuteMedication.add(medication3)
        mListActiveAcuteMedication.add(medication4)
        mListActiveAcuteMedication.add(medication5)
        mListActiveAcuteMedication.add(medication6)
        mListActiveAcuteMedication.add(medication7)
        mListActiveAcuteMedication.add(medication8)

        v.rvActiveMedication.layoutManager=LinearLayoutManager(requireActivity())
        v.rvActiveMedication.adapter=MedicationAdapter(requireActivity(),mListActiveAcuteMedication)

        return v
    }
}