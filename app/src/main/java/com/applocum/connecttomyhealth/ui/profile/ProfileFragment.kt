package com.applocum.connecttomyhealth.ui.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.changepassword.ChangePasswordActivity
import com.applocum.connecttomyhealth.ui.login.LoginActivity
import com.applocum.connecttomyhealth.ui.mydownloads.MyDownloadsActivity
import com.applocum.connecttomyhealth.ui.payment.MemberShipActivity
import com.applocum.connecttomyhealth.ui.payment.PaymentMethodActivity
import com.applocum.connecttomyhealth.ui.personaldetails.PersonalDetailsActivity
import com.applocum.connecttomyhealth.ui.securitycheck.SecurityActivity
import com.applocum.connecttomyhealth.ui.settings.SettingActivity
import kotlinx.android.synthetic.main.custom_signout_dialog.view.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.fragment_profile.view.btnSignOut
import javax.inject.Inject


class ProfileFragment : Fragment() {
    lateinit var v: View

    @Inject
    lateinit var userHolder: UserHolder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_profile, container, false)
        MyApplication.getAppContext().component.inject(this)

        v.tvFName.text=userHolder.userFirstName
        v.tvLName.text=userHolder.userLastName

        v.llPersonalDetails.setOnClickListener {
            startActivity(Intent(requireActivity(), PersonalDetailsActivity::class.java))
        }
        v.llClinicalRecords.setOnClickListener {
            startActivity(Intent(requireActivity(), SecurityActivity::class.java))
        }
        v.llMyDownloads.setOnClickListener {
            startActivity(Intent(requireActivity(), MyDownloadsActivity::class.java))
        }
        v.llPaymentMethods.setOnClickListener {
            startActivity(Intent(requireActivity(), PaymentMethodActivity::class.java))
        }

        v.llMemberships.setOnClickListener {
            startActivity(Intent(requireActivity(), MemberShipActivity::class.java))
        }

        v.llChangePassword.setOnClickListener {
            startActivity(Intent(requireActivity(), ChangePasswordActivity::class.java))
        }
        v.llSetting.setOnClickListener {
            startActivity(Intent(requireActivity(), SettingActivity::class.java))
        }
        v.llHelp.setOnClickListener {

        }

        v.btnSignOut.setOnClickListener {
            val showDialogView = LayoutInflater.from(requireActivity())
                .inflate(R.layout.custom_signout_dialog, null, false)
            val dialog = AlertDialog.Builder(requireActivity()).create()
            dialog.setView(showDialogView)
            dialog.setCanceledOnTouchOutside(false)

            showDialogView.btnSignOut.setOnClickListener {
                val intent=Intent(requireActivity(),LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                userHolder.userToken?.let { it1 ->
                    userHolder.userGender?.let { it2 ->
                        userHolder.userLastName?.let { it3 ->
                            userHolder.userFirstName?.let { it4 ->
                                userHolder.userEmail?.let { it5 ->
                                    userHolder.userDOB?.let { it6 ->
                                        userHolder.clearUserData(userHolder.userid!!, it4,
                                            it3, it5,
                                            it2, it6,
                                            it1
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                startActivity(intent)
                requireActivity().finish()
            }
            showDialogView.btnNo.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }

        return v
    }
}