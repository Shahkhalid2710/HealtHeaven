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


        return v
    }
}

