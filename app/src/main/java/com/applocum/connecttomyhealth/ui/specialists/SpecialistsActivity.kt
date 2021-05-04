package com.applocum.connecttomyhealth.ui.specialists

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.shareddata.endpoints.BookAppointment
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.addsymptoms.AddSymptomActivity
import com.applocum.connecttomyhealth.ui.booksession.BookSessionActivity
import com.applocum.connecttomyhealth.ui.specialists.models.Specialist
import kotlinx.android.synthetic.main.activity_specialists.*
import kotlinx.android.synthetic.main.custom_progress.*
import javax.inject.Inject

class SpecialistsActivity : BaseActivity() ,SpecilistPresenter.View {
    @Inject
    lateinit var presenter: SpecilistPresenter

    @Inject
    lateinit var userHolder: UserHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ivBack.setOnClickListener { finish() }

        (application as MyApplication).component.inject(this)
        presenter.injectview(this)

        presenter.getlist()

        val appointment = BookAppointment()
        appointment.corporateId = 66
        userHolder.saveBookAppointmentData(appointment)

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
                intent.putExtra("specialist",specialist)
                startActivity(intent)
            }

            override fun onbookSession(specialist: Specialist, position: Int) {
                val intent=Intent(this@SpecialistsActivity,AddSymptomActivity::class.java)
                intent.putExtra("specialist",specialist)
                val appointment = userHolder.getBookAppointmentData()
                appointment.therapistId = specialist.id
                appointment.therapistName = "${specialist.first_name} ${specialist.last_name}"
                specialist.usual_address.apply {
                    appointment.therapistAddress = "$line1, $line2,$line3, $town, $pincode"
                }
                userHolder.saveBookAppointmentData(appointment)
                startActivity(intent)
            }
        })
        rvDoctors.adapter=specialistsAdapter
    }

    override fun viewProgress(isShow: Boolean) {
        progress.visibility = if (isShow) View.VISIBLE else View.GONE
    }
}