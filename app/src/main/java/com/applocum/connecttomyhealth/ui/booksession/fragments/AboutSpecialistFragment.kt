package com.applocum.connecttomyhealth.ui.booksession.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.specialists.models.Specialist
import kotlinx.android.synthetic.main.fragment_about_specialist.view.*

class AboutSpecialistFragment : Fragment() {

    lateinit var specialist: Specialist
    lateinit var v: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_about_specialist, container, false)

        specialist = arguments?.getSerializable("specialist") as Specialist
        v.tvDoctorBio.text = specialist.bio
        v.tvDoctorLocation.text = (specialist.usual_address?.line1 + "" + "," + specialist.usual_address?.line2 + "" + "," + specialist.usual_address?.town + "" + "," + specialist.usual_address?.pincode)

        v.btnDirection.setOnClickListener {
            val navigation = Uri.parse("google.navigation:q=" + specialist.usual_address?.latitude + "," + specialist.usual_address?.longitude + "")
            val navigationIntent = Intent(Intent.ACTION_VIEW, navigation)
            navigationIntent.setPackage("com.google.android.apps.maps")
            startActivity(navigationIntent)
        }

        doctorSpecialities()
        doctorLanguages()

        return v
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun doctorSpecialities() {
        val buttonLayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        buttonLayoutParams.setMargins(5, 10, 10, 5)

        for (i in 0 until specialist.doctor_specialities.size) {
            val tv = TextView(requireActivity())
            tv.text = specialist.doctor_specialities[i]
            tv.textSize = 13.0f
            tv.gravity = Gravity.CENTER
            tv.setTextColor(Color.parseColor("#828282"))
            tv.background = resources.getDrawable(R.drawable.custom_data_layout)
            tv.id = i + 1
            tv.layoutParams = buttonLayoutParams
            tv.tag = i
            val font =
                Typeface.createFromAsset(requireActivity().assets, "fonts/montserrat_medium.ttf")
            tv.typeface = font
            tv.setPadding(30, 30, 30, 30)
            v.flSpecialities.addView(tv)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun doctorLanguages() {
        val buttonLayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        buttonLayoutParams.setMargins(5, 10, 10, 5)
        for (i in 0 until specialist.languages.size) {
            val tv = TextView(requireActivity())
            tv.text = specialist.languages[i]
            tv.textSize = 13.0f
            tv.gravity = Gravity.CENTER
            tv.setTextColor(Color.parseColor("#828282"))
            tv.background = resources.getDrawable(R.drawable.custom_data_layout)
            tv.id = i + 1
            tv.layoutParams = buttonLayoutParams
            tv.tag = i
            val font = Typeface.createFromAsset(requireActivity().assets, "fonts/montserrat_medium.ttf")
            tv.typeface = font
            tv.setPadding(30, 30, 30, 30)
            v.flLanguages.addView(tv)
        }
    }

    fun newInstance(specialist: Specialist): AboutSpecialistFragment {
        val args = Bundle()
        args.putSerializable("specialist", specialist)
        val fragment = AboutSpecialistFragment()
        fragment.arguments = args
        return fragment
    }
}