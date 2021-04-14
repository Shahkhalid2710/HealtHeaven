package com.applocum.connecttomyhealth.ui.bottomnavigationview

import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.appointment.AppointmentFragment
import com.applocum.connecttomyhealth.ui.home.HomeFragment
import com.applocum.connecttomyhealth.ui.notification.NotificationFragment
import com.applocum.connecttomyhealth.ui.profile.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_bottomnavigationview.*
import javax.inject.Inject

class BottomNavigationViewActivity : BaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    @Inject
    lateinit var userHolder: UserHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bottomnavigationView.setOnNavigationItemSelectedListener(this)

        loadfragment(HomeFragment())
        bottomnavigationView.setBackgroundColor(Color.WHITE)

    }

    override fun getLayoutResourceId(): Int=R.layout.activity_bottomnavigationview

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
       if (id==R.id.nav_home)
       {
           loadfragment(HomeFragment())
           return true
       }
        else if (id==R.id.nav_notification)
       {
           loadfragment(NotificationFragment())
           return true
       }
        else if (id==R.id.nav_appointment)
       {
           loadfragment(AppointmentFragment())
           return true
       }
        else if (id==R.id.nav_profile)
       {
           loadfragment(ProfileFragment())
           return true
       }
        return true
    }

    private fun loadfragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.rlbottomnavigation, fragment)
        transaction.commit()

    }
}