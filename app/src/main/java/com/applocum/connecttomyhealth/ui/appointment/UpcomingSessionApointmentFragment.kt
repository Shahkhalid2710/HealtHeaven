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
        val session1=Session(R.drawable.drpaulina,"Paulina","Face To Face","Friday, 14th August 2020,")
        val session3=Session(R.drawable.drpaulina,"Paulina","Face To Face","Friday, 14th August 2020")
        val session2=Session(R.drawable.ic_dr_1,"Paulina","Video Call","Friday, 14th August 2020,")
        val session4=Session(R.drawable.ic_dr_1,"Paulina","Video Call","Friday, 14th August 2020,")
        val session5=Session(R.drawable.drpaulina,"Paulina","Face To Face","Friday, 14th August 2020,")
        val session7=Session(R.drawable.drpaulina,"Paulina","Face To Face","Friday, 14th August 2020")
        val session6=Session(R.drawable.ic_dr_1,"Paulina","Video Call","Friday, 14th August 2020,")
        val session8=Session(R.drawable.ic_dr_1,"Paulina","Video Call","Friday, 14th August 2020,")

        mListUpcomingSession.add(session1)
        mListUpcomingSession.add(session2)
        mListUpcomingSession.add(session3)
        mListUpcomingSession.add(session4)
        mListUpcomingSession.add(session5)
        mListUpcomingSession.add(session6)
        mListUpcomingSession.add(session7)
        mListUpcomingSession.add(session8)

        v.rvUpcomingSession.layoutManager=LinearLayoutManager(requireActivity())
        v.rvUpcomingSession.adapter=
            MySessionAdapter(
                requireActivity(),
                mListUpcomingSession
            )
        return v
    }
}