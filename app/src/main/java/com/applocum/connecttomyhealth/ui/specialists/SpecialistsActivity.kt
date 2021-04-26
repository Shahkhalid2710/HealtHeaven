package com.applocum.connecttomyhealth.ui.specialists

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.specialists.models.Specialist
import kotlinx.android.synthetic.main.activity_specialists.*
import javax.inject.Inject

class SpecialistsActivity : BaseActivity() ,SpecilistPresenter.View {
    @Inject
    lateinit var presenter: SpecilistPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as  MyApplication).component.inject(this)
        presenter.injectview(this)

        ivBack.setOnClickListener { finish() }

        presenter.getlist()
    }

    override fun getLayoutResourceId(): Int= R.layout.activity_specialists
    override fun displaymessage(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

    override fun getdoctorlist(list: ArrayList<Specialist>) {
        rvSpecialists.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        val specialistsAdapter=SpecialistsAdapter(this,list)
        rvSpecialists.adapter=specialistsAdapter
        Log.d("doctorlisttttt","-->"+list)
    }
}