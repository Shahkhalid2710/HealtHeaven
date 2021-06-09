package com.applocum.connecttomyhealth.ui.notification.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.changeFont
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.custom_no_internet.view.*
import kotlinx.android.synthetic.main.fragment_notification.view.*


class NotificationFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_notification, container, false)

        if (!haveNetworkConnection()) {
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
        }
        return v
    }

    private fun haveNetworkConnection(): Boolean {
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
    }
}