package com.applocum.connecttomyhealth.ui.allergyhistory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.allergyhistory.adapters.AllergyHistoryAdapter
import com.applocum.connecttomyhealth.ui.allergyhistory.models.AllergyHistory
import kotlinx.android.synthetic.main.fragment_active_allergy.view.*
import kotlinx.android.synthetic.main.fragment_past_allergy.view.*

class PastAllergyFragment : Fragment() {
    var mListPastAllergy: ArrayList<AllergyHistory> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_past_allergy, container, false)
        val allergyHistory1 = AllergyHistory("Oncogene Protine V-ABC")
        val allergyHistory2 = AllergyHistory("Food allerdy diet")

        mListPastAllergy.add(allergyHistory1)
        mListPastAllergy.add(allergyHistory2)

        v.rvPastAllergy.layoutManager = LinearLayoutManager(requireActivity())
        v.rvPastAllergy.adapter = AllergyHistoryAdapter(requireActivity(), mListPastAllergy)
        return v
    }
}