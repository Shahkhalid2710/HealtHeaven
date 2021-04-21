package com.applocum.connecttomyhealth.ui.appointment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.appointment.adapters.MySessionAdapter
import com.applocum.connecttomyhealth.ui.appointment.models.Session
import kotlinx.android.synthetic.main.fragment_upcoming_session_apointment.view.*

class UpcomingSessionApointmentFragment : Fragment() {
    val mListUpcomingSession:ArrayList<Session> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val  v=inflater.inflate(R.layout.fragment_upcoming_session_apointment, container, false)
        val session1=Session(R.drawable.drpaulina,"Dr. Paulina Gayoso","Face To Face | 60 min","Friday, 14th August 2020, 1:00PM")
        val session3=Session(R.drawable.drpaulina,"Dr. Paulina Gayoso","Face To Face | 60 min","Friday, 14th August 2020, 1:00PM")
        val session2=Session(R.drawable.ic_dr_1,"Dr. Paulina Gayoso","Video Call | 30 min","Friday, 14th August 2020, 1:00PM")
        val session4=Session(R.drawable.ic_dr_1,"Dr. Paulina Gayoso","Video Call | 30 min","Friday, 14th August 2020, 1:00PM")

        mListUpcomingSession.add(session1)
        mListUpcomingSession.add(session2)
        mListUpcomingSession.add(session3)
        mListUpcomingSession.add(session4)

        v.rvUpcomingSession.layoutManager=LinearLayoutManager(requireActivity())
        v.rvUpcomingSession.adapter=
            MySessionAdapter(
                requireActivity(),
                mListUpcomingSession
            )
        return v
    }
}