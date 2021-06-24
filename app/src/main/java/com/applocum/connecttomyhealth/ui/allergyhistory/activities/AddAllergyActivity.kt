package com.applocum.connecttomyhealth.ui.allergyhistory.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.changeFont
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.allergyhistory.models.FalseAllergy
import com.applocum.connecttomyhealth.ui.allergyhistory.models.TrueAllergy
import com.applocum.connecttomyhealth.ui.allergyhistory.presenters.AllergyHistoryPresenter
import com.applocum.connecttomyhealth.ui.medicalhistory.presenters.MedicalPresenter
import com.applocum.connecttomyhealth.ui.medicalhistory.adapters.MedicalDiseaseAdapter
import com.applocum.connecttomyhealth.ui.medicalhistory.models.FalseMedicalHistory
import com.applocum.connecttomyhealth.ui.medicalhistory.models.Medical
import com.applocum.connecttomyhealth.ui.medicalhistory.models.MedicalHistory
import com.applocum.connecttomyhealth.ui.medicalhistory.models.TrueMedicalHistory
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_allergy.*
import kotlinx.android.synthetic.main.activity_add_allergy.cbActiveCurrently
import kotlinx.android.synthetic.main.activity_add_allergy.ivBack
import kotlinx.android.synthetic.main.custom_progress.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AddAllergyActivity : BaseActivity(), MedicalPresenter.View,
    AllergyHistoryPresenter.View {
    var mListMedical: ArrayList<Medical> = ArrayList()
    private var selectedString = ""
    private var isMatched = false
    private var isActivePast = false
    private var allergyName = ""

    @Inject
    lateinit var presenter: MedicalPresenter

    @Inject
    lateinit var allergyHistoryPresenter: AllergyHistoryPresenter

    override fun getLayoutResourceId(): Int = R.layout.activity_add_allergy

    override fun handleInternetConnectivity(isConnect: Boolean?) {}

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).component.inject(this)
        presenter.injectView(this)
        allergyHistoryPresenter.injectView(this)

        RxView.clicks(ivBack).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe { finish() }

        RxTextView.textChanges(etAddAllergy)
            .debounce(500, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnEach {
                if (selectedString == etAddAllergy.text.toString()) {
                    isMatched = true
                } else {
                    isMatched = false
                    selectedString = ""
                }
            }
            .observeOn(Schedulers.computation())
            .filter { s -> s.length >= 2 }
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                if (!isMatched) {
                    presenter.getDiseaseList(etAddAllergy.text.toString())
                }
            }.subscribe().let { presenter.disposables.add(it) }

        cbActiveCurrently.setOnCheckedChangeListener { _, b ->
            if (b) {
                isActivePast = true
            } else {
                isActivePast = false
            }
        }

        allergyName = etAddAllergy.text.toString()

        RxView.clicks(btnSaveAllergy).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                allergyHistoryPresenter.addAllergy(allergyName, isActivePast)
            }
       }

    override fun displayMessage(message: String) {
        val snackBar = Snackbar.make(llAddAllergy, message, Snackbar.LENGTH_LONG)
        snackBar.changeFont()
        val snackView = snackBar.view
        snackView.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
        snackBar.show()
    }

    override fun getDiseaseList(list: ArrayList<Medical>) {
        mListMedical = list
        rvAllergy.visibility = View.VISIBLE
        rvAllergy.layoutManager = LinearLayoutManager(this)
        rvAllergy.adapter =
            MedicalDiseaseAdapter(
                this,
                mListMedical,
                object : MedicalDiseaseAdapter.ItemClickListnter {
                    override fun onItemClick(medical: Medical, position: Int) {
                        allergyName = medical.id.toString()
                        if (!etAddAllergy.text.isNullOrBlank()) {
                            mListMedical.clear()
                        }
                        selectedString = medical.description
                        etAddAllergy.setText(medical.description)
                        rvAllergy.visibility = View.GONE
                    }
                })
    }

    override fun viewProgress(isShow: Boolean) {
        progressAllergy.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun viewAllergyProgress(isShow: Boolean) {
        progress.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun noInternetConnection(isConnect: Boolean) {
        if (!isConnect)
        {
            val snackBar = Snackbar.make(llAddAllergy,R.string.no_internet, Snackbar.LENGTH_LONG)
            snackBar.changeFont()
            val snackView = snackBar.view
            snackView.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            snackBar.show()
        }
    }

    override fun viewMedicalProgress(isShow: Boolean) {}

    override fun sendMedicalHistoryData(medicalHistory: MedicalHistory) {}

    override fun showActiveMedicalHistory(trueMedicalHistory: ArrayList<TrueMedicalHistory>) {}

    override fun showPastMedicalHistory(falseMedicalHistory: ArrayList<FalseMedicalHistory>) {}

    override fun noInternet(isConnect: Boolean) {
        if (!isConnect)
        {
            val snackBar = Snackbar.make(llAddAllergy,R.string.no_internet, Snackbar.LENGTH_LONG)
            snackBar.changeFont()
            val snackView = snackBar.view
            snackView.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            snackBar.show()
        }
    }

    override fun displaySuccessMessage(message: String) { this.finish()}

    override fun displayErrorMessage(message: String) {
        val snackBar = Snackbar.make(llAddAllergy, message, Snackbar.LENGTH_LONG)
        snackBar.changeFont()
        val snackView = snackBar.view
        snackView.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
        snackBar.show()
    }

    override fun showActiveAllergy(activeAllergy: ArrayList<TrueAllergy>) {}

    override fun showPastAllergy(pastAllergy: ArrayList<FalseAllergy>) {}
}