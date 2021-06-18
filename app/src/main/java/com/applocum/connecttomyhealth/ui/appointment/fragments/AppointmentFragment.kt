package com.applocum.connecttomyhealth.ui.appointment.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.allergyhistory.adapters.ViewPagerFragmentAdapter
import kotlinx.android.synthetic.main.fragment_appointment.view.*


class AppointmentFragment : Fragment(){

    lateinit var v: View


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
         v = inflater.inflate(R.layout.fragment_appointment, container, false)

        val viewPagerFragmentAdapter = ViewPagerFragmentAdapter(requireActivity(),requireActivity().supportFragmentManager)
        viewPagerFragmentAdapter.addfragment(UpcomingSessionApointmentFragment(), "Upcoming Session")
        viewPagerFragmentAdapter.addfragment(PastSessionAppointmentFragment(), "Past Session")
        v.viewPager.adapter = viewPagerFragmentAdapter
        v.tablayout.setupWithViewPager(v.viewPager)

        return v
    }
}