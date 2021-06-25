package com.applocum.connecttomyhealth.ui.home.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.changeFont
import com.applocum.connecttomyhealth.shareddata.endpoints.BookAppointment
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.booksession.activities.BookSessionActivity
import com.applocum.connecttomyhealth.ui.home.adapters.DoctorAdapter
import com.applocum.connecttomyhealth.ui.specialists.activities.SpecialistsActivity
import com.applocum.connecttomyhealth.ui.specialists.models.Specialist
import com.applocum.connecttomyhealth.ui.specialists.presenters.SpecilistPresenter
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.custom_no_internet.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_home.view.noInternet
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class HomeFragment : Fragment(), SpecilistPresenter.View {
    var doubleBackToExitPressedOnce = false

    @Inject
    lateinit var userHolder: UserHolder

    @Inject
    lateinit var specilistPresenter: SpecilistPresenter

    lateinit var v: View

    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_home, container, false)

        MyApplication.getAppContext().component.inject(this)
        specilistPresenter.injectview(this)

        val appointment = BookAppointment()
        appointment.corporateId = 66
        userHolder.saveBookAppointmentData(appointment)

        RxView.clicks(v.btnBookAppointment).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(requireActivity(), SpecialistsActivity::class.java))
               requireActivity().overridePendingTransition(0,0)
            }

        RxView.clicks(v.noInternet.tvRetry).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                v.noInternet.visibility=View.GONE
                specilistPresenter.getDoctorlist()
            }


        requireActivity()
            .onBackPressedDispatcher
            .addCallback(requireActivity(), object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (doubleBackToExitPressedOnce) {
                        requireActivity().finish()
                        return
                    }

                    doubleBackToExitPressedOnce = true

                    Toast.makeText(requireContext(), R.string.press_once_again_to_exit, Toast.LENGTH_SHORT).show()

                    Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
                }
               }
            )

        return v
    }

    override fun onResume() {
        super.onResume()
        specilistPresenter.getDoctorlist()

        val circularProgressDrawable = CircularProgressDrawable(requireActivity())
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()


        if (userHolder.userPhoto!!.isEmpty())
        {
            Glide.with(requireActivity()).load(R.drawable.ic_blank_profile_pic).placeholder(circularProgressDrawable).into(v.ivUser)
            v.ivPicWarning.visibility=View.VISIBLE
        }
        else {
            Glide.with(requireActivity()).load(userHolder.userPhoto).placeholder(circularProgressDrawable).into(v.ivUser)
            v.ivPicWarning.visibility=View.GONE
        }

        v.tvName.text=userHolder.userFirstName
    }

    override fun displaymessage(message: String) {}

    override fun getdoctorlist(list: ArrayList<Specialist>) {
        rvTopDoctors.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        rvTopDoctors.adapter = DoctorAdapter(requireActivity(), list, object : DoctorAdapter.DoctorClickListner {
                override fun onDoctorClick(specialist: Specialist, position: Int) {
                    val intent = Intent(requireActivity(),BookSessionActivity::class.java)
                    intent.putExtra("specialist", specialist)
                    intent.putExtra("specialistId", specialist.id)
                    val appointment = userHolder.getBookAppointmentData()
                    appointment.therapistId = specialist.id
                    appointment.therapistImage = specialist.image
                    appointment.threapistBio = specialist.bio
                    appointment.therapistName =
                        "${specialist.first_name} ${specialist.last_name}"
                    specialist.usual_address.apply {
                        appointment.therapistAddress = "$line1, $line2,$line3, $town, $pincode"
                    }
                    userHolder.saveBookAppointmentData(appointment)
                    startActivity(intent)
                    requireActivity().overridePendingTransition(0,0)
                }
            })
     }

    override fun viewProgress(isShow: Boolean) {
        v.progressTopDoctors.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun noInternet(isConnect: Boolean) {
        if (!isConnect)
        {
            v.rvTopDoctors.visibility=View.GONE
            v.noInternet.visibility=View.VISIBLE

            val snackBar = Snackbar.make(llHome,R.string.no_internet, Snackbar.LENGTH_LONG).apply { view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).maxLines = 5 }
            snackBar.changeFont()
            val snackView = snackBar.view
            snackView.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.red))
            snackBar.show()
        }
        else{
            v.noInternet.visibility=View.GONE
            v.rvTopDoctors.visibility=View.VISIBLE
        }
    }

}