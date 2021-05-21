package com.applocum.connecttomyhealth.ui.othernotes

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.mydownloads.DocumentViewActivity
import com.applocum.connecttomyhealth.ui.othernotes.adapters.OtherNoteAdapter
import com.applocum.connecttomyhealth.ui.prescription.models.Document
import com.applocum.connecttomyhealth.ui.prescription.models.DocumentPresenter
import kotlinx.android.synthetic.main.activity_other_note.*
import kotlinx.android.synthetic.main.activity_other_note.ivBack
import kotlinx.android.synthetic.main.custom_loader_progress.*
import javax.inject.Inject

class OtherNoteActivity : BaseActivity(),DocumentPresenter.View {

    @Inject
    lateinit var presenter: DocumentPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ivBack.setOnClickListener { finish() }
        (application as MyApplication).component.inject(this)

        presenter.injectView(this)

        presenter.getOtherNote()
    }

    override fun getLayoutResourceId(): Int = R.layout.activity_other_note

    override fun displayErrorMessage(message: String) {
       Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

    override fun getDocument(list: ArrayList<Document>) {
        if (list.isEmpty())
        {
            layoutNotFoundOtherNotes.visibility= View.VISIBLE
            rvOtherNotes.visibility= View.GONE
        }
        else
        {
            layoutNotFoundOtherNotes.visibility= View.GONE
            rvOtherNotes.visibility= View.VISIBLE
        }

        rvOtherNotes.layoutManager= LinearLayoutManager(this)
        rvOtherNotes.adapter= OtherNoteAdapter(this,list,object :OtherNoteAdapter.NoteClickListner{
            override fun onNoteClick(document: Document, position: Int) {
                val intent= Intent(this@OtherNoteActivity,DocumentViewActivity::class.java)
                intent.putExtra("document",document)
                startActivity(intent)
            }
        })
    }

    override fun viewProgress(isShow: Boolean) {
        progress.visibility=if (isShow) View.VISIBLE else View.GONE
    }
}