package com.applocum.connecttomyhealth.ui.profile.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.changeFont
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.changepassword.activities.ChangePasswordActivity
import com.applocum.connecttomyhealth.ui.login.activities.LoginActivity
import com.applocum.connecttomyhealth.ui.mydownloads.activities.MyDownloadsActivity
import com.applocum.connecttomyhealth.ui.payment.activities.MemberShipActivity
import com.applocum.connecttomyhealth.ui.payment.activities.PaymentMethodActivity
import com.applocum.connecttomyhealth.ui.personaldetails.activities.PersonalDetailsActivity
import com.applocum.connecttomyhealth.ui.profiledetails.presenters.ProfileDetailsPresenter
import com.applocum.connecttomyhealth.ui.profiledetails.models.Patient
import com.applocum.connecttomyhealth.ui.securitycheck.activities.SecurityActivity
import com.applocum.connecttomyhealth.ui.settings.activities.SettingActivity
import com.applocum.connecttomyhealth.ui.signup.models.User
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.custom_profile_dialog.view.*
import kotlinx.android.synthetic.main.custom_progress.view.*
import kotlinx.android.synthetic.main.custom_signout_dialog.view.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.fragment_profile.view.btnSignOut
import java.io.File
import java.net.URI
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ProfileFragment : Fragment(),
    ProfileDetailsPresenter.View {
    lateinit var v: View

    @Inject
    lateinit var userHolder: UserHolder

    @Inject
    lateinit var presenter: ProfileDetailsPresenter

    @SuppressLint("CheckResult")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_profile, container, false)
        MyApplication.getAppContext().component.inject(this)
        presenter.injectview(this)

        presenter.showProfile()

        RxView.clicks(v.flProfile).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                val showDialogView = LayoutInflater.from(requireActivity())
                    .inflate(R.layout.custom_profile_dialog, null, false)
                val dialog = androidx.appcompat.app.AlertDialog.Builder(requireActivity()).create()
                dialog.setView(showDialogView)

                showDialogView.tvChooseImage.setOnClickListener {
                    context?.let { it1 ->
                        CropImage.activity()
                            .setAllowFlipping(false)
                            .setAllowCounterRotation(false)
                            .setBorderLineColor(resources.getColor(R.color.green))
                            .setBorderCornerColor(resources.getColor(R.color.green))
                            .setMinCropResultSize(400,400)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setCropShape(CropImageView.CropShape.OVAL)
                            .setCropMenuCropButtonIcon(R.drawable.ic_yes)
                            .setRequestedSize(500, 500)
                            .start(it1, this)
                    }
                    dialog.dismiss()
                }

                showDialogView.tvCancel.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.show()
            }


        RxView.clicks(v.llPersonalDetails).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(requireActivity(), PersonalDetailsActivity::class.java))
            }

        RxView.clicks(v.llClinicalRecords).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(requireActivity(), SecurityActivity::class.java))
            }

        RxView.clicks(v.llMyDownloads).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(requireActivity(), MyDownloadsActivity::class.java))
            }

        RxView.clicks(v.llPaymentMethods).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(requireActivity(), PaymentMethodActivity::class.java))
            }

        RxView.clicks(v.llMemberships).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(requireActivity(), MemberShipActivity::class.java))
            }

        RxView.clicks(v.llChangePassword).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(requireActivity(), ChangePasswordActivity::class.java))
            }


        RxView.clicks(v.llSetting).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(requireActivity(), SettingActivity::class.java))
            }

        RxView.clicks(v.btnSignOut).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                val showDialogView = LayoutInflater.from(requireActivity())
                    .inflate(R.layout.custom_signout_dialog, null, false)
                val dialog = AlertDialog.Builder(requireActivity()).create()
                dialog.setView(showDialogView)

                showDialogView.btnSignOut.setOnClickListener {
                    val intent = Intent(
                        requireActivity(),
                        LoginActivity::class.java
                    )
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    requireActivity().finish()
                    userHolder.clearUserData("", "", "", "", "", "", "")
                }
                showDialogView.btnNo.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.show()
            }
        return v
    }

    override fun showProfile(patient: Patient) {
        v.tvFName.text = patient.user.firstName
        v.tvLName.text = patient.user.lastName
        Glide.with(requireActivity()).clear(v.ivProfile)

        if (patient.image.isEmpty())
        {
            Glide.with(requireActivity()).load(R.drawable.ic_blank_profile_pic).into(v.ivProfile)
        }
        else {
            Glide.with(requireActivity()).load(patient.image).into(v.ivProfile)
        }
    }

    override fun displayMessage(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show()
    }

    override fun displayErrorMessage(message: String) {
        val snackbar = Snackbar.make(llProfile, message, Snackbar.LENGTH_LONG)
        snackbar.changeFont()
        val snackview = snackbar.view
        snackview.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.red))
        snackbar.show()
    }

    override fun userData(user: User) {}

    override fun viewprogress(isShow: Boolean) {
        v.progress.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val fileOfPic = File(URI(result.uri.toString()))
                presenter.updateUser(fileOfPic)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onResume() {
        presenter.showProfile()
        super.onResume()
    }
}