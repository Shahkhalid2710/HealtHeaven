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

class ActiveAllergyFragment : Fragment() {
      var mListActiveAllergy:ArrayList<AllergyHistory> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v= inflater.inflate(R.layout.fragment_active_allergy, container, false)

        val allergyHistory1=AllergyHistory("Oncogene Protine V-ABC")
        val allergyHistory2=AllergyHistory("Food allerdy diet")
        val allergyHistory3=AllergyHistory("Quilonia Ethiopica")

        mListActiveAllergy.add(allergyHistory1)
        mListActiveAllergy.add(allergyHistory2)
        mListActiveAllergy.add(allergyHistory3)

        v.rvActiveAllergy.layoutManager=LinearLayoutManager(requireActivity())
        v.rvActiveAllergy.adapter=AllergyHistoryAdapter(requireActivity(),mListActiveAllergy)
        return v
    }
  }