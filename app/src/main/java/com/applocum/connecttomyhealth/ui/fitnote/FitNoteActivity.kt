package com.applocum.connecttomyhealth.ui.fitnote

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.fitnote.adapters.FitNoteAdapter
import com.applocum.connecttomyhealth.ui.mydownloads.DocumentViewActivity
import com.applocum.connecttomyhealth.ui.prescription.models.Document
import com.applocum.connecttomyhealth.ui.prescription.models.DocumentPresenter
import kotlinx.android.synthetic.main.activity_fit_note.*
import kotlinx.android.synthetic.main.custom_loader_progress.*
import javax.inject.Inject

class FitNoteActivity : BaseActivity(),DocumentPresenter.View {
    @Inject
    lateinit var presenter: DocumentPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).component.inject(this)
        presenter.injectView(this)
        ivBack.setOnClickListener { finish() }

        presenter.getFitNote()
    }

    override fun getLayoutResourceId(): Int = R.layout.activity_fit_note

    override fun displayErrorMessage(message: String) {
      Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

    override fun getDocument(list: ArrayList<Document>) {
        if (list.isEmpty())
        {
            layoutNotFoundFitNote.visibility= View.VISIBLE
            rvFitNote.visibility= View.GONE
        }
        else
        {
            layoutNotFoundFitNote.visibility= View.GONE
            rvFitNote.visibility= View.VISIBLE
        }

        rvFitNote.layoutManager= LinearLayoutManager(this)
        rvFitNote.adapter= FitNoteAdapter(this,list,object :FitNoteAdapter.FitNoteClickListner{
            override fun onNoteClick(document: Document, position: Int) {
                val intent= Intent(this@FitNoteActivity, DocumentViewActivity::class.java)
                intent.putExtra("document",document)
                startActivity(intent)
            }
        })
    }

    override fun viewProgress(isShow: Boolean) {
        progress.visibility=if (isShow) View.VISIBLE else View.GONE
    }
}