package com.applocum.connecttomyhealth.ui.notification.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.notification.adapters.NotificationAdapter
import com.applocum.connecttomyhealth.ui.notification.models.Notification
import kotlinx.android.synthetic.main.fragment_notification.view.*

class NotificationFragment : Fragment() {
    private var mListNotification:ArrayList<Notification> = ArrayList()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_notification, container, false)

        val notification1=Notification("Scheduled Consultation","Please join the waiting room, the Healtheaven specialist is ready to see","07 : 56 AM | 10 April 2021")
        val notification2=Notification("Appointment Confirmation","Your Appointment has been booked","10 : 00 AM | 10 February 2021")
        val notification3=Notification("Appointment Confirmation","Your Appointment has been booked","12 : 27 PM | 09 February 2021")
        val notification4=Notification("Scheduled Consultation","Please join the waiting room, the Healtheaven specialist is ready to see","05 : 50 AM | 10 March 2021")
        val notification5=Notification("Appointment Confirmation","Your Appointment has been booked","07 : 56 AM | 10 March 2021")
        val notification6=Notification("Scheduled Consultation","Please join the waiting room, the Healtheaven specialist is ready to see","07 : 56 AM | 10 April 2021")
        val notification7=Notification("Appointment Confirmation","Your Appointment has been booked","10 : 00 AM | 10 February 2021")
        val notification8=Notification("Appointment Confirmation","Your Appointment has been booked","12 : 27 PM | 09 February 2021")
        val notification9=Notification("Scheduled Consultation","Please join the waiting room, the Healtheaven specialist is ready to see","05 : 50 AM | 10 March 2021")
        val notification10=Notification("Appointment Confirmation","Your Appointment has been booked","07 : 56 AM | 10 March 2021")
        val notification11=Notification("Scheduled Consultation","Please join the waiting room, the Healtheaven specialist is ready to see","07 : 56 AM | 10 April 2021")
        val notification12=Notification("Appointment Confirmation","Your Appointment has been booked","10 : 00 AM | 10 February 2021")
        val notification13=Notification("Appointment Confirmation","Your Appointment has been booked","12 : 27 PM | 09 February 2021")
        val notification14=Notification("Scheduled Consultation","Please join the waiting room, the Healtheaven specialist is ready to see","05 : 50 AM | 10 March 2021")
        val notification15=Notification("Appointment Confirmation","Your Appointment has been booked","07 : 56 AM | 10 March 2021")

        mListNotification.add(notification1)
        mListNotification.add(notification2)
        mListNotification.add(notification3)
        mListNotification.add(notification4)
        mListNotification.add(notification5)
        mListNotification.add(notification6)
        mListNotification.add(notification7)
        mListNotification.add(notification8)
        mListNotification.add(notification9)
        mListNotification.add(notification10)
        mListNotification.add(notification11)
        mListNotification.add(notification12)
        mListNotification.add(notification13)
        mListNotification.add(notification14)
        mListNotification.add(notification15)

        v.rvNotification.layoutManager=LinearLayoutManager(requireActivity())
        v.rvNotification.adapter=NotificationAdapter(requireActivity(),mListNotification)

        return v
    }

  /*  private fun haveNetworkConnection(): Boolean {
        var haveConnectedWifi = false
        var haveConnectedMobile = false
        val cm = requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val netInfo = cm!!.allNetworkInfo
        for (ni in netInfo) {
            if (ni.typeName
                    .equals("WIFI", ignoreCase = true)
            ) if (ni.isConnected) haveConnectedWifi = true
            if (ni.typeName
                    .equals("MOBILE", ignoreCase = true)
            ) if (ni.isConnected) haveConnectedMobile = true
        }
        return haveConnectedWifi || haveConnectedMobile

        *//* if (!haveNetworkConnection()) {
            val snackbar = Snackbar.make(requireActivity().findViewById(android.R.id.content), "No network connection", Snackbar.LENGTH_LONG)
            val snackview = snackbar.view
            snackbar.changeFont()
            snackview.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.red))
            snackbar.show()
            v.noInternet.visibility = View.VISIBLE
            v.noNotification.visibility = View.GONE
        } else {
            v.noInternet.visibility = View.GONE
            v.noNotification.visibility = View.VISIBLE
        }

        v.tvRetry.setOnClickListener {
            val notificationFragment = NotificationFragment()
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.frameLayout, notificationFragment, "notification_Fragment").commitAllowingStateLoss()
            val frg: Fragment? = requireActivity().supportFragmentManager.findFragmentByTag("notification_Fragment")
            val ft: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            frg?.let { it1 -> ft.detach(it1) }
            if (frg != null) {
                ft.attach(frg)
            }
            ft.commit()
        }*//*

    }*/
}

