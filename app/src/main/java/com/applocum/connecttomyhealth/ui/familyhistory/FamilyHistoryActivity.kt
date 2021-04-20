package com.applocum.connecttomyhealth.ui.familyhistory

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.familyhistory.models.FamilyHistory
import kotlinx.android.synthetic.main.activity_family_history.*
import kotlinx.android.synthetic.main.activity_family_history.ivBack

class FamilyHistoryActivity : BaseActivity() {
    var mListFamilyHistory:ArrayList<FamilyHistory> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ivBack.setOnClickListener {
            finish()
        }
        tvAddFamilyHistory.setOnClickListener {
            startActivity(Intent(this,AddFamilyHistoryActivity::class.java))
        }

        val familyHistory1=FamilyHistory("Repair of joint capsule of knee")
        val familyHistory2=FamilyHistory("Knee replacing")
        val familyHistory3=FamilyHistory("Hemoglobin manitaba")
        val familyHistory4=FamilyHistory("Neoplasm of Stomach")


        mListFamilyHistory.add(familyHistory1)
        mListFamilyHistory.add(familyHistory2)
        mListFamilyHistory.add(familyHistory3)
        mListFamilyHistory.add(familyHistory4)

        rvFamilyHistory.layoutManager= LinearLayoutManager(this)
        rvFamilyHistory.adapter= FamilyHistoryAdapter(this,mListFamilyHistory)

        if (mListFamilyHistory.size > 0)
        {
            familyhistory.visibility= View.GONE
        }
        else
        {
            familyhistory.visibility= View.VISIBLE
        }

    }

    override fun getLayoutResourceId(): Int =R.layout.activity_family_history
}