package com.applocum.connecttomyhealth.ui.bottomnavigationview

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.view.forEach
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

class BottomNavigationViewActivity : BaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener,
    BottomNavigationView.OnNavigationItemReselectedListener {
    @Inject
    lateinit var userHolder: UserHolder

    override fun getLayoutResourceId(): Int=R.layout.activity_bottomnavigationview

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bottomnavigationView.setOnNavigationItemSelectedListener(this)
       // bottomnavigationView.setOnNavigationItemReselectedListener(this)

        bottomnavigationView.menu.forEach {
            val view = bottomnavigationView.findViewById<View>(it.itemId)
            view.setOnLongClickListener {
                true
            }
        }

        loadFragment(HomeFragment())

    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
       if (id==R.id.nav_home)
       {
           loadFragment(HomeFragment())
           return true
       }
        else if (id==R.id.nav_notification)
       {
           loadFragment(NotificationFragment())
           return true
       }
        else if (id==R.id.nav_appointment)
       {
           loadFragment(AppointmentFragment())
           return true
       }
        else if (id==R.id.nav_profile)
       {
           loadFragment(ProfileFragment())
           return true
       }
        return true
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.rlbottomnavigation, fragment)
        transaction.commit()
    }

    override fun onNavigationItemReselected(item: MenuItem) {

    }
}