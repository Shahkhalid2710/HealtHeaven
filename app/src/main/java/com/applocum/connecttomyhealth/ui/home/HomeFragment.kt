package com.applocum.connecttomyhealth.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.home.adapters.CategoryAdapter
import com.applocum.connecttomyhealth.ui.home.adapters.DoctorAdapter
import com.applocum.connecttomyhealth.ui.home.model.Category
import com.applocum.connecttomyhealth.ui.home.model.Doctor
import com.applocum.connecttomyhealth.ui.specialists.SpecialistsActivity
import kotlinx.android.synthetic.main.fragment_home.view.*
import javax.inject.Inject

class HomeFragment : Fragment() {
     private var mListCategory:ArrayList<Category> = ArrayList()
     private var mListDoctor:ArrayList<Doctor> = ArrayList()

    @Inject
    lateinit var userHolder: UserHolder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v= inflater.inflate(R.layout.fragment_home, container, false)

       val category1=Category(R.drawable.ic_heart,"Heart")
       val category2=Category(R.drawable.ic_brain,"Brain")
       val category3=Category(R.drawable.ic_teeth,"Teeth")
       val category4=Category(R.drawable.ic_heart,"Heart")
       val category5=Category(R.drawable.ic_brain,"Brain")
       val category6=Category(R.drawable.ic_teeth,"Teeth")
       val category7=Category(R.drawable.ic_heart,"Heart")
       val category8=Category(R.drawable.ic_brain,"Brain")
       val category9=Category(R.drawable.ic_teeth,"Teeth")

       mListCategory.add(category1)
       mListCategory.add(category2)
       mListCategory.add(category3)
       mListCategory.add(category4)
       mListCategory.add(category5)
       mListCategory.add(category6)
       mListCategory.add(category7)
       mListCategory.add(category8)
       mListCategory.add(category9)

        v.rvCategories.layoutManager=LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        v.rvCategories.adapter=
            CategoryAdapter(
                requireActivity(),
                mListCategory
            )
        val doctor1=Doctor(R.drawable.ic_dr_1,"Dr. Jessica Fernandez","Neurosurgeon ","10 AM - 4 PM ")
        val doctor2=Doctor(R.drawable.drpaulina,"Dr. Jessica Fernandez","Neurosurgeon ","12 PM - 4 PM ")
        val doctor3=Doctor(R.drawable.ic_dr_1,"Dr. Jessica Fernandez","Neurosurgeon ","10 PM - 4 AM ")

        mListDoctor.add(doctor1)
        mListDoctor.add(doctor2)
        mListDoctor.add(doctor3)
        v.rvDoctors.layoutManager=LinearLayoutManager(activity)
        v.rvDoctors.adapter=DoctorAdapter(requireActivity(),mListDoctor)

        v.btnBookAppointment.setOnClickListener {
            startActivity(Intent(requireActivity(),SpecialistsActivity::class.java))
        }

        return v
    }

}