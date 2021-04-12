package com.applocum.connecttomyhealth.ui.specialists

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.specialists.models.Specialists
import kotlinx.android.synthetic.main.activity_specialists.*

class SpecialistsActivity : BaseActivity() {
    private val mList:ArrayList<Specialists> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ivBack.setOnClickListener { finish() }

        val specialists1=Specialists(R.drawable.ic_dr_1,"Dr. Paulina Gayoso","Prescription, Sick Notes","It was popularised in the 1960s with the release of Letraset sheets containing Lorem ")
        val specialists2=Specialists(R.drawable.ic_dr_1,"Dr. Gatsharan Sangrota","Prescription, Sick Notes","It was popularised in the 1960s with the release of Letraset sheets containing Lorem ")
        val specialists3=Specialists(R.drawable.ic_dr_1,"Dr. Paulina Gayoso","Prescription, Sick Notes","It was popularised in the 1960s with the release of Letraset sheets containing Lorem ")
        val specialists4=Specialists(R.drawable.ic_dr_1,"Dr. Gatsharan Sangrota","Prescription, Sick Notes","It was popularised in the 1960s with the release of Letraset sheets containing Lorem ")
        val specialists5=Specialists(R.drawable.ic_dr_1,"Dr. Paulina Gayoso","Prescription, Sick Notes","It was popularised in the 1960s with the release of Letraset sheets containing Lorem ")
        val specialists6=Specialists(R.drawable.ic_dr_1,"Dr. Gatsharan Sangrota","Prescription, Sick Notes","It was popularised in the 1960s with the release of Letraset sheets containing Lorem ")

        mList.add(specialists1)
        mList.add(specialists2)
        mList.add(specialists3)
        mList.add(specialists4)
        mList.add(specialists5)
        mList.add(specialists6)

        rvSpecialists.layoutManager=LinearLayoutManager(this)
        rvSpecialists.adapter=SpecialistsAdapter(this,mList)

    }

    override fun getLayoutResourceId(): Int= R.layout.activity_specialists
}