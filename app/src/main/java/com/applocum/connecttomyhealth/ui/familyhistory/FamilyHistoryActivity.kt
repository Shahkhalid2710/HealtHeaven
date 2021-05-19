package com.applocum.connecttomyhealth.ui.familyhistory

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.familyhistory.models.FamilyHistory
import kotlinx.android.synthetic.main.activity_family_history.*
import kotlinx.android.synthetic.main.activity_family_history.ivBack
import kotlinx.android.synthetic.main.custom_family_history_xml.*
import kotlinx.android.synthetic.main.custom_loader_progress.*
import javax.inject.Inject

class FamilyHistoryActivity : BaseActivity(),FamilyHistoryPresenter.View {

    @Inject
    lateinit var presenter: FamilyHistoryPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ivBack.setOnClickListener { finish() }
        (application as MyApplication).component.inject(this)
        presenter.injectView(this)

        tvAddFamilyHistory.setOnClickListener {
            startActivity(Intent(this,AddFamilyHistoryActivity::class.java))
        }
        btnAddFamilyHistory.setOnClickListener {
            startActivity(Intent(this,AddFamilyHistoryActivity::class.java))
        }

        presenter.showFamilyHistoryList()

    }

    override fun getLayoutResourceId(): Int =R.layout.activity_family_history
    override fun displayErrorMessage(message: String) {

    }

    override fun displaySuccessMessage(message: String) {
    }

    override fun viewFamilyHistoryProgress(isShow: Boolean) {
        progress.visibility=if (isShow) View.VISIBLE else View.GONE
    }

    override fun familyHistoryList(list: ArrayList<FamilyHistory>) {
        if (list.isEmpty())
        {
            layoutNotFoundFamilyHistory.visibility= View.VISIBLE
            tvAddFamilyHistory.visibility=View.GONE
            rvFamilyHistory.visibility=View.GONE
        }
        else
        {
            layoutNotFoundFamilyHistory.visibility= View.GONE
            tvAddFamilyHistory.visibility=View.VISIBLE
            rvFamilyHistory.visibility=View.VISIBLE
        }
        rvFamilyHistory.layoutManager=LinearLayoutManager(this)
        rvFamilyHistory.adapter=FamilyHistoryAdapter(this,list)
    }
}