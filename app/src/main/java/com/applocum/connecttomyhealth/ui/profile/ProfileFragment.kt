package com.applocum.connecttomyhealth.ui.profile

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
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
import com.applocum.connecttomyhealth.ui.profiledetails.ProfileDetailsPresenter
import com.applocum.connecttomyhealth.ui.profiledetails.models.Patient
import com.applocum.connecttomyhealth.ui.securitycheck.SecurityActivity
import com.applocum.connecttomyhealth.ui.settings.SettingActivity
import com.applocum.connecttomyhealth.ui.signup.models.User
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.custom_profile_dialog.view.*
import kotlinx.android.synthetic.main.custom_progress.view.*
import kotlinx.android.synthetic.main.custom_signout_dialog.view.btnNo
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.fragment_profile.view.btnSignOut
import javax.inject.Inject


class ProfileFragment : Fragment(),ProfileDetailsPresenter.View {
    lateinit var v: View
    private var profileImage=""

    @Inject
    lateinit var userHolder: UserHolder

    @Inject
    lateinit var presenter: ProfileDetailsPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_profile, container, false)
        MyApplication.getAppContext().component.inject(this)
        presenter.injectview(this)
        presenter.showProfile()


        v.flProfile.setOnClickListener {
            val showDialogView = LayoutInflater.from(requireActivity()).inflate(R.layout.custom_profile_dialog, null, false)
            val dialog = androidx.appcompat.app.AlertDialog.Builder(requireActivity()).create()
            dialog.setView(showDialogView)

            showDialogView.tvGallery.setOnClickListener {
                context?.let { it1 ->
                    CropImage.activity()
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .setCropMenuCropButtonTitle("Done")
                        .setRequestedSize(400, 400)
                        .start(it1,this)
                }
                dialog.dismiss()
            }
            showDialogView.tvCamera.setOnClickListener {
                dialog.dismiss()
            }
            showDialogView.tvCancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }


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

            showDialogView.btnSignOut.setOnClickListener {
                val intent=Intent(requireActivity(),LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
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

    override fun showProfile(patient: Patient) {
        v.tvFName.text=patient.user.firstName
        v.tvLName.text=patient.user.lastName

    }

    override fun displayMessage(message: String) {
        Toast.makeText(requireActivity(),message,Toast.LENGTH_LONG).show()
    }

    override fun displayErrorMessage(message: String) {

    }

    override fun userData(user: User) {

    }

    override fun viewprogress(isShow: Boolean) {
        v.progress.visibility=if (isShow) View.VISIBLE else View.GONE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                v.ivProfile.setImageURI(result.uri)
                profileImage=(result.uri.path).toString()
                presenter.updateUser(profileImage)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onResume() {
        presenter.showProfile()
        super.onResume()
    }
}