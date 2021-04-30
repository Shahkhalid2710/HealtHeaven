package com.applocum.connecttomyhealth.ui.booksession

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.specialists.models.Specialist
import kotlinx.android.synthetic.main.fragment_about_specialist.*
import kotlinx.android.synthetic.main.fragment_about_specialist.view.*

class AboutSpecialistFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v= inflater.inflate(R.layout.fragment_about_specialist, container, false)
        return v
    }
}