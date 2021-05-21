package com.applocum.connecttomyhealth.ui.prescription.models

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.mydownloads.DocumentViewActivity
import com.applocum.connecttomyhealth.ui.prescription.models.adapters.PrescriptionAdapter
import kotlinx.android.synthetic.main.activity_prescription.*
import kotlinx.android.synthetic.main.custom_loader_progress.*
import javax.inject.Inject

class PrescriptionActivity : BaseActivity(), DocumentPresenter.View {

    @Inject
    lateinit var presenter: DocumentPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).component.inject(this)
        presenter.injectView(this)
        ivBack.setOnClickListener { finish() }

        presenter.getPrescription()
    }

    override fun getLayoutResourceId(): Int = R.layout.activity_prescription

    override fun displayErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun getDocument(list: ArrayList<Document>) {
        if (list.isEmpty())
        {
            layoutNotFoundPrescription.visibility=View.VISIBLE
            rvPrescription.visibility=View.GONE
        }
        else
        {
            layoutNotFoundPrescription.visibility=View.GONE
            rvPrescription.visibility=View.VISIBLE
        }

        rvPrescription.layoutManager=LinearLayoutManager(this)
        rvPrescription.adapter=PrescriptionAdapter(this,list,object :PrescriptionAdapter.PrescriptionClickListner{
            override fun onPrescriptionClick(document: Document, position: Int) {
                val intent= Intent(this@PrescriptionActivity, DocumentViewActivity::class.java)
                intent.putExtra("document",document)
                startActivity(intent)
            }
        })
    }

    override fun viewProgress(isShow: Boolean) {
        progress.visibility=if (isShow) View.VISIBLE else View.GONE
    }
}