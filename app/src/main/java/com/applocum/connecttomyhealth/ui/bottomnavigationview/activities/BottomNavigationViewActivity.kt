package com.applocum.connecttomyhealth.ui.bottomnavigationview.activities

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.appointment.fragments.AppointmentFragment
import com.applocum.connecttomyhealth.ui.home.fragments.HomeFragment
import com.applocum.connecttomyhealth.ui.notification.fragments.NotificationFragment
import com.applocum.connecttomyhealth.ui.profile.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_bottomnavigationview.*
import javax.inject.Inject

class BottomNavigationViewActivity : BaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener,BottomNavigationView.OnNavigationItemReselectedListener {
    @Inject
    lateinit var userHolder: UserHolder

    override fun getLayoutResourceId(): Int = R.layout.activity_bottomnavigationview

    override fun handleInternetConnectivity(isConnect: Boolean?) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bottomnavigationView.setOnNavigationItemSelectedListener(this)
        bottomnavigationView.setOnNavigationItemReselectedListener(this)

        bottomnavigationView.menu.forEach {
            val view = bottomnavigationView.findViewById<View>(it.itemId)
            view.setOnLongClickListener { true }
        }

        //showBadge(this, bottomnavigationView, R.id.nav_notification, "15")
       // removeBadge(bottomnavigationView, R.id.nav_notification)
        loadFragment(HomeFragment())

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                loadFragment(HomeFragment())
                return true
            }
            R.id.nav_notification -> {
                loadFragment(NotificationFragment())
                return true
            }
            R.id.nav_appointment -> {
                loadFragment(AppointmentFragment())
                return true
            }
            R.id.nav_profile -> {
                loadFragment(ProfileFragment())
                return true
            }
            else -> return true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.rlbottomnavigation, fragment)
        transaction.commit()
    }

    override fun onNavigationItemReselected(item: MenuItem) {}

    private fun showBadge(context: Context?, bottomNavigationView: BottomNavigationView, @IdRes itemId: Int, value: String?) {
        removeBadge(bottomNavigationView, itemId)
        val itemView: BottomNavigationItemView = bottomNavigationView.findViewById(itemId)
        val badge: View = LayoutInflater.from(context).inflate(R.layout.custom_notification_badge, bottomNavigationView, false)
        val text = badge.findViewById<TextView>(R.id.badge_text_view)
        text.text = value
        itemView.addView(badge)
    }

    private fun removeBadge(bottomNavigationView: BottomNavigationView, @IdRes itemId: Int) {
        val itemView: BottomNavigationItemView = bottomNavigationView.findViewById(itemId)
        if (itemView.childCount == 3) {
            itemView.removeViewAt(2)
        }
    }

    override fun onBackPressed() {
        if (bottomnavigationView.selectedItemId == R.id.nav_home) {
            super.onBackPressed()
        } else {
            bottomnavigationView.selectedItemId = R.id.nav_home
        }
    }

}