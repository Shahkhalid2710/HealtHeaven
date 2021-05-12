package com.applocum.connecttomyhealth.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.booksession.BookSessionActivity
import com.applocum.connecttomyhealth.ui.home.adapters.DoctorAdapter
import com.applocum.connecttomyhealth.ui.profiledetails.ProfileDetailsPresenter
import com.applocum.connecttomyhealth.ui.profiledetails.models.Patient
import com.applocum.connecttomyhealth.ui.specialists.SpecialistsActivity
import com.applocum.connecttomyhealth.ui.specialists.SpecilistPresenter
import com.applocum.connecttomyhealth.ui.specialists.models.Specialist
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import javax.inject.Inject

class HomeFragment : Fragment(),ProfileDetailsPresenter.View,SpecilistPresenter.View {
    @Inject
    lateinit var userHolder: UserHolder

    @Inject
    lateinit var presenter: ProfileDetailsPresenter

    @Inject
    lateinit var specilistPresenter: SpecilistPresenter

    lateinit var v:View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_home, container, false)

        MyApplication.getAppContext().component.inject(this)
        presenter.injectview(this)
        specilistPresenter.injectview(this)

        v.btnBookAppointment.setOnClickListener {
            startActivity(Intent(requireActivity(), SpecialistsActivity::class.java))
        }

        presenter.showProfile()
        specilistPresenter.getDoctorlist()

        return v
    }

    override fun showProfile(patient: Patient) {
        tvName.text = patient.first_name
    }

    override fun displaymessage(message: String) {
    }

    override fun getdoctorlist(list: ArrayList<Specialist>) {
        rvTopDoctors.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        rvTopDoctors.adapter = DoctorAdapter(requireActivity(), list,object :DoctorAdapter.DoctorClickListner{
            override fun onDoctorClick(specialist: Specialist, position: Int) {
                val intent=Intent(requireActivity(), BookSessionActivity::class.java)
                intent.putExtra("specialist",specialist)
                startActivity(intent)
            }
        })
    }

    override fun viewProgress(isShow: Boolean) {
       v.progressTopDoctors.visibility = if (isShow) View.VISIBLE else View.GONE
    }
}