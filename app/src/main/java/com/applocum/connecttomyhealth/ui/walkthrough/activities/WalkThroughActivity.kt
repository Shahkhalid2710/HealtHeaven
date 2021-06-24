package com.applocum.connecttomyhealth.ui.walkthrough.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.login.activities.LoginActivity
import com.applocum.connecttomyhealth.ui.walkthrough.adapters.WalkThroughAdapter
import com.applocum.connecttomyhealth.ui.walkthrough.models.SelectItem
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_walk_through.*

class WalkThroughActivity : BaseActivity(), TabLayout.OnTabSelectedListener {
    var mList: ArrayList<SelectItem> = ArrayList()
    var position = 0

    override fun getLayoutResourceId(): Int = R.layout.activity_walk_through
    override fun handleInternetConnectivity(isConnect: Boolean?) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (restoreprefdata()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            //overridePendingTransition(0,0)
        }

        btnGetStarted.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            saveprefdata()
            finish()
            overridePendingTransition(0,0)
        }

        tvSkip.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            saveprefdata()
            finish()
            overridePendingTransition(0,0)
        }

        val selectItem1 = SelectItem(
            R.drawable.image_book_appointment,
            "Book Appointment",
            "Book Appointment and get consult to our great doctors via Call, Video and Face to Face"
        )
        val selectItem2 = SelectItem(
            R.drawable.image_doctor,
            "Doctor",
            "Recover from Top doctors selected from your location preferences via different session option such as video, audio and face to face."
        )
        val selectItem3 = SelectItem(
            R.drawable.image_sessions,
            "Sessions",
            "Don’t wait just select the session option get appoint to our great doctors via Audio and Video Call also"
        )

        mList.add(selectItem1)
        mList.add(selectItem2)
        mList.add(selectItem3)

        val walkThroughAdapter = WalkThroughAdapter(this, mList)
        viewPagerWalkThrough.adapter = walkThroughAdapter
        tablayoutWalkThrough.setupWithViewPager(viewPagerWalkThrough)

        llNext.setOnClickListener {
            position = viewPagerWalkThrough.currentItem

            if (position < mList.size) {
                position++
                viewPagerWalkThrough.currentItem = position
            }

            if (position == mList.size - 1) loadedLastScreen() else unloadedLastScreen()
        }
        tablayoutWalkThrough.addOnTabSelectedListener(this)
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {}

    override fun onTabUnselected(tab: TabLayout.Tab?) {}

    override fun onTabSelected(tab: TabLayout.Tab) {
        if (tab.position == mList.size - 1) {
            loadedLastScreen()
        } else {
            unloadedLastScreen()
        }
    }

    private fun loadedLastScreen() {
        llNext.visibility = View.GONE
        tablayoutWalkThrough.visibility = View.VISIBLE
        btnGetStarted.visibility = View.VISIBLE
        tvSkip.visibility = View.GONE
    }

    private fun unloadedLastScreen() {
        llNext.visibility = View.VISIBLE
        tablayoutWalkThrough.visibility = View.VISIBLE
        btnGetStarted.visibility = View.GONE
        tvSkip.visibility = View.VISIBLE
    }

    private fun saveprefdata() {
        val sharedPreferences = this.getSharedPreferences("mypref", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean("isIntroOpened", true)
        editor.apply()
    }

    private fun restoreprefdata(): Boolean {
        val sharedPreferences = this.getSharedPreferences("mypref", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("isIntroOpened", false)
    }
}