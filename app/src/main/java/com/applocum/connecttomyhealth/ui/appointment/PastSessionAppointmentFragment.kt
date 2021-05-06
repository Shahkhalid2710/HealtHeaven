package com.applocum.connecttomyhealth.ui.appointment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.appointment.adapters.PastSessionAdapter
import com.applocum.connecttomyhealth.ui.appointment.models.Session
import kotlinx.android.synthetic.main.fragment_past_session_appointment.view.*


class PastSessionAppointmentFragment : Fragment() {
    val mListPastSession:ArrayList<Session> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v= inflater.inflate(R.layout.fragment_past_session_appointment, container, false)

        val session1=Session(R.drawable.drpaulina,"Paulina","Face To Face","Friday, 14th August 2020,")
        val session2=Session(R.drawable.ic_dr_1,"Paulina","Video Call","Friday, 14th August 2020,")
        val session3=Session(R.drawable.drpaulina,"Paulina","Face To Face","Friday, 14th August 2020,")
        val session4=Session(R.drawable.drpaulina,"Paulina","Face To Face","Friday, 14th August 2020,")
        val session5=Session(R.drawable.ic_dr_1,"Paulina","Video Call","Friday, 14th August 2020,")
        val session6=Session(R.drawable.drpaulina,"Paulina","Face To Face","Friday, 14th August 2020,")

        mListPastSession.add(session1)
        mListPastSession.add(session2)
        mListPastSession.add(session3)
        mListPastSession.add(session4)
        mListPastSession.add(session5)
        mListPastSession.add(session6)

        v.rvPastSession.layoutManager=LinearLayoutManager(requireActivity())
        v.rvPastSession.adapter=PastSessionAdapter(requireActivity(),mListPastSession)
        return v
    }
}