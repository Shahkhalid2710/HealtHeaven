package com.applocum.connecttomyhealth.ui.investigation

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.investigation.models.Investigation
import kotlinx.android.synthetic.main.activity_investigation.*

class InvestigationActivity : BaseActivity() {
    var mListInvestigation:ArrayList<Investigation> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ivBack.setOnClickListener { finish() }

        tvAddInvestigation.setOnClickListener {
            startActivity(Intent(this,AddInvestigationActivity::class.java))
        }

        val investigation1=Investigation("Salmonella lll arizonae 53:K:Z","12 Aug 2020","Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam non dignissim ex, a ornare augue. Donec id velit leo. In aliquet ultrices mauris, sit amet posuere ante posuere eget.")
        val investigation2=Investigation("Injury of respiratory","12 Sep 2020","Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam non dignissim ex, a ornare augue. Donec id velit leo. In aliquet ultrices mauris, sit amet posuere ante posuere eget.")
        val investigation3=Investigation("Repair of joint capsule of knee","12 May 2020","Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam non dignissim ex, a ornare augue. Donec id velit leo. In aliquet ultrices mauris, sit amet posuere ante posuere eget.")
        val investigation4=Investigation("Salmonella lll arizonae 53:K:Z","15 May 2020","Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam non dignissim ex, a ornare augue. Donec id velit leo. In aliquet ultrices mauris, sit amet posuere ante posuere eget.")
        val investigation5=Investigation("Salmonella lll arizonae 53:K:Z","15 May 2020","Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam non dignissim ex, a ornare augue. Donec id velit leo. In aliquet ultrices mauris, sit amet posuere ante posuere eget.")
        val investigation6=Investigation("Salmonella lll arizonae 53:K:Z","15 May 2020","Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam non dignissim ex, a ornare augue. Donec id velit leo. In aliquet ultrices mauris, sit amet posuere ante posuere eget.")
        val investigation7=Investigation("Salmonella lll arizonae 53:K:Z","15 May 2020","Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam non dignissim ex, a ornare augue. Donec id velit leo. In aliquet ultrices mauris, sit amet posuere ante posuere eget.")
        val investigation8=Investigation("Salmonella lll arizonae 53:K:Z","15 May 2020","Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam non dignissim ex, a ornare augue. Donec id velit leo. In aliquet ultrices mauris, sit amet posuere ante posuere eget.")
        val investigation9=Investigation("Salmonella lll arizonae 53:K:Z","15 May 2020","Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam non dignissim ex, a ornare augue. Donec id velit leo. In aliquet ultrices mauris, sit amet posuere ante posuere eget.")
        val investigation10=Investigation("Salmonella lll arizonae 53:K:Z","15 May 2020","Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam non dignissim ex, a ornare augue. Donec id velit leo. In aliquet ultrices mauris, sit amet posuere ante posuere eget.")

        mListInvestigation.add(investigation1)
        mListInvestigation.add(investigation2)
        mListInvestigation.add(investigation3)
        mListInvestigation.add(investigation4)
        mListInvestigation.add(investigation5)
        mListInvestigation.add(investigation6)
        mListInvestigation.add(investigation7)
        mListInvestigation.add(investigation8)
        mListInvestigation.add(investigation9)
        mListInvestigation.add(investigation10)

        rvInvestigation.layoutManager=LinearLayoutManager(this)
        rvInvestigation.adapter=InvestigationAdapter(this,mListInvestigation)

        if (mListInvestigation.size > 0)
        {
            investigation.visibility=View.GONE
        }
        else
        {
            investigation.visibility=View.VISIBLE
        }

    }

    override fun getLayoutResourceId(): Int =R.layout.activity_investigation
}