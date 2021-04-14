package com.applocum.connecttomyhealth.ui.booksession

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.booksession.adapters.SessionTypeAdapter
import com.applocum.connecttomyhealth.ui.booksession.models.SessionType
import kotlinx.android.synthetic.main.fragment_availability.*
import kotlinx.android.synthetic.main.fragment_availability.view.*

class AvailabilityFragment : Fragment(), View.OnClickListener {
    val mListSessionType: ArrayList<SessionType> = ArrayList()
    val mListSelectSlot: ArrayList<SessionType> = ArrayList()
    val mListAvailableTime: ArrayList<SessionType> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_availability, container, false)

        val sessionType1 = SessionType("Phone")
        val sessionType2 = SessionType("Video")
        val sessionType3 = SessionType("Face to Face")
        mListSessionType.add(sessionType1)
        mListSessionType.add(sessionType2)
        mListSessionType.add(sessionType3)

        v.rvSessionType.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        v.rvSessionType.adapter = SessionTypeAdapter(requireActivity(), mListSessionType)


        val sessionType4 = SessionType("30 min")
        val sessionType5 = SessionType("60 min")

        mListSelectSlot.add(sessionType4)
        mListSelectSlot.add(sessionType5)
        v.rvSelectSlot.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        v.rvSelectSlot.adapter = SessionTypeAdapter(requireActivity(), mListSelectSlot)

        val sessionType6 = SessionType("09:00 AM")
        val sessionType7 = SessionType("11:00 AM")
        val sessionType8 = SessionType("01:00 PM")
        val sessionType9 = SessionType("03:00 PM")
        val sessionType10 = SessionType("05:00 PM")
        val sessionType11 = SessionType("07:00 PM")
        val sessionType12 = SessionType("09:00 PM")
        val sessionType13 = SessionType("11:00 PM")
        val sessionType14 = SessionType("01:00 AM")
        val sessionType15 = SessionType("03:00 AM")
        val sessionType16 = SessionType("05:00 AM")

        mListAvailableTime.add(sessionType6)
        mListAvailableTime.add(sessionType7)
        mListAvailableTime.add(sessionType8)
        mListAvailableTime.add(sessionType9)
        mListAvailableTime.add(sessionType10)
        mListAvailableTime.add(sessionType11)
        mListAvailableTime.add(sessionType12)
        mListAvailableTime.add(sessionType13)
        mListAvailableTime.add(sessionType14)
        mListAvailableTime.add(sessionType15)
        mListAvailableTime.add(sessionType16)

        v.rvAvailableTime.layoutManager = GridLayoutManager(requireActivity(), 3)
        v.rvAvailableTime.adapter = SessionTypeAdapter(requireActivity(), mListAvailableTime)

        v.switchMultiSessions.setOnClickListener(this)
        return v
    }

    override fun onClick(v: View?) {
        if (switchMultiSessions.isChecked()) {
            llMultiSessions.visibility = View.VISIBLE
        } else {
            llMultiSessions.visibility = View.GONE
        }
    }
}