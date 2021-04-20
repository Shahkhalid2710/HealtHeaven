package com.applocum.connecttomyhealth.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.addcard.AddCardActivity
import com.applocum.connecttomyhealth.ui.changepassword.ChangePasswordActivity
import com.applocum.connecttomyhealth.ui.clinicalrecords.ClinicalRecordsActivity
import com.applocum.connecttomyhealth.ui.mydownloads.MyDownloadsActivity
import com.applocum.connecttomyhealth.ui.payment.AddCodeActivity
import com.applocum.connecttomyhealth.ui.personaldetails.PersonalDetailsActivity
import com.applocum.connecttomyhealth.ui.settings.SettingActivity
import com.applocum.connecttomyhealth.ui.signup.SignupActivity
import kotlinx.android.synthetic.main.fragment_profile.view.*

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v= inflater.inflate(R.layout.fragment_profile, container, false)
        v.rlPersonalDetails.setOnClickListener {
            startActivity(Intent(requireActivity(),PersonalDetailsActivity::class.java))
        }
        v.rlClinicalRecords.setOnClickListener {
            startActivity(Intent(requireActivity(),ClinicalRecordsActivity::class.java))
        }
        v.rlMyDownloads.setOnClickListener {
            startActivity(Intent(requireActivity(),MyDownloadsActivity::class.java))
        }
        v.rlPaymentMethods.setOnClickListener {
            startActivity(Intent(requireActivity(),AddCardActivity::class.java))
        }

        v.rlMemberships.setOnClickListener {
            startActivity(Intent(requireActivity(),AddCodeActivity::class.java))
        }

        v.rlChangePassword.setOnClickListener {
            startActivity(Intent(requireActivity(),ChangePasswordActivity::class.java))
        }
        v.rlSetting.setOnClickListener {
            startActivity(Intent(requireActivity(),SettingActivity::class.java))
        }
        v.btnSignOut.setOnClickListener {
            startActivity(Intent(requireActivity(),SignupActivity::class.java))
        }
        return v
    }

}