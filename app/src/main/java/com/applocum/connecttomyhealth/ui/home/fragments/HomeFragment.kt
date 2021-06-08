package com.applocum.connecttomyhealth.ui.home.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.booksession.activities.BookSessionActivity
import com.applocum.connecttomyhealth.ui.home.adapters.DoctorAdapter
import com.applocum.connecttomyhealth.ui.profiledetails.presenters.ProfileDetailsPresenter
import com.applocum.connecttomyhealth.ui.profiledetails.models.Patient
import com.applocum.connecttomyhealth.ui.signup.models.User
import com.applocum.connecttomyhealth.ui.specialists.activities.SpecialistsActivity
import com.applocum.connecttomyhealth.ui.specialists.presenters.SpecilistPresenter
import com.applocum.connecttomyhealth.ui.specialists.models.Specialist
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class HomeFragment : Fragment(), SpecilistPresenter.View, ProfileDetailsPresenter.View {
    @Inject
    lateinit var userHolder: UserHolder

    @Inject
    lateinit var specilistPresenter: SpecilistPresenter

    @Inject
    lateinit var profileDetailsPresenter: ProfileDetailsPresenter

    lateinit var v: View

    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_home, container, false)

        MyApplication.getAppContext().component.inject(this)
        specilistPresenter.injectview(this)
        profileDetailsPresenter.injectview(this)

        RxView.clicks(v.btnBookAppointment).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(requireActivity(), SpecialistsActivity::class.java))
            }

        specilistPresenter.getDoctorlist()
        profileDetailsPresenter.showProfile()

        return v
    }

    override fun displaymessage(message: String) {}

    override fun getdoctorlist(list: ArrayList<Specialist>) {
        rvTopDoctors.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        rvTopDoctors.adapter = DoctorAdapter(requireActivity(), list, object : DoctorAdapter.DoctorClickListner {
                override fun onDoctorClick(specialist: Specialist, position: Int) {
                    val intent = Intent(requireActivity(), BookSessionActivity::class.java)
                    intent.putExtra("specialist", specialist)
                    startActivity(intent)
                }
            })
     }

    override fun viewProgress(isShow: Boolean) {
        v.progressTopDoctors.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun showProfile(patient: Patient) {
        v.tvName.text = patient.user.firstName
        if (patient.image.isEmpty())
        {
            Glide.with(requireActivity()).load(R.drawable.ic_blank_profile_pic).into(v.ivUser)
        }
        else {
            Glide.with(requireActivity()).load(patient.image).into(v.ivUser)
        }
    }

    override fun displayMessage(message: String) {}
    override fun displaySuccessMessage(message: String) {}

    override fun displayErrorMessage(message: String) {}

    override fun userData(user: User) {}

    override fun viewprogress(isShow: Boolean) {}
}