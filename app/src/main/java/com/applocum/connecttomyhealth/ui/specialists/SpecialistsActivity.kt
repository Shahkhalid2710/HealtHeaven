package com.applocum.connecttomyhealth.ui.specialists

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.booksession.BookSessionActivity
import com.applocum.connecttomyhealth.ui.specialists.models.Specialist
import kotlinx.android.synthetic.main.activity_specialists.*
import kotlinx.android.synthetic.main.custom_progress.*
import javax.inject.Inject

class SpecialistsActivity : BaseActivity() ,SpecilistPresenter.View {
    @Inject
    lateinit var presenter: SpecilistPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ivBack.setOnClickListener { finish() }

        (application as MyApplication).component.inject(this)
        presenter.injectview(this)

        presenter.getlist()

    }

    override fun getLayoutResourceId(): Int= R.layout.activity_specialists

    override fun displaymessage(message: String) {
        Toast.makeText(this,"Success",Toast.LENGTH_SHORT).show()
    }

    override fun getdoctorlist(list:ArrayList<Specialist>) {
        rvDoctors.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        val specialistsAdapter=SpecialistsAdapter(this,list,object :SpecialistsAdapter.ItemClickListner{
            override fun onItemClick(specialist: Specialist, position: Int) {
               val intent=Intent(this@SpecialistsActivity,BookSessionActivity::class.java)
                val bundle=Bundle()
                bundle.putString("firstname",specialist.first_name)
                bundle.putString("lastname",specialist.last_name)
                bundle.putString("bio",specialist.bio)
                bundle.putString("designation",specialist.designation)
                bundle.putString("doctorid",specialist.id.toString())
                bundle.putString("image",specialist.image)
                intent.putExtras(bundle)
                startActivity(intent)

            }
        })
        rvDoctors.adapter=specialistsAdapter
    }

    override fun viewProgress(isShow: Boolean) {
        progress.visibility = if (isShow) View.VISIBLE else View.GONE
    }
}