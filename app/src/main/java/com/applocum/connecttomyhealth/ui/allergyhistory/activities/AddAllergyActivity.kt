package com.applocum.connecttomyhealth.ui.allergyhistory.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
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
import com.applocum.connecttomyhealth.ui.securitycheck.activities.SecurityActivity
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView
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
    AllergyHistoryPresenter.View, MedicalDiseaseAdapter.ItemClickListnter {
    var mListMedical: ArrayList<Medical> = ArrayList()
    private var selectedString = ""
    private var isMatched = false
    private var isActivePast = false
    private var allergyName = ""
    lateinit var medicalDiseaseAdapter: MedicalDiseaseAdapter
    private var isLoading = false

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

        medicalDiseaseAdapter = MedicalDiseaseAdapter(this, ArrayList(), this)
        rvAllergy.layoutManager = LinearLayoutManager(this)
        rvAllergy.adapter = medicalDiseaseAdapter

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
                    medicalDiseaseAdapter.mList.remove(null)
                    medicalDiseaseAdapter.mList.clear()
                    medicalDiseaseAdapter.notifyDataSetChanged()
                    presenter.resetPage()
                    presenter.getDiseaseList(etAddAllergy.text.toString())
                    val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(llAddAllergy.windowToken, 0)
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
                val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(llAddAllergy.windowToken, 0)
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

        medicalDiseaseAdapter.mList.addAll(list)
        medicalDiseaseAdapter.notifyItemRangeInserted(medicalDiseaseAdapter.mList.size, list.size)
        RxRecyclerView.scrollEvents(rvAllergy)
            .subscribe {
                val total = rvAllergy.layoutManager?.itemCount ?: 0
                val last = (rvAllergy.layoutManager as LinearLayoutManager)
                    .findLastVisibleItemPosition()
                if (total > 0 && total <= last + 2) {
                    if (!isLoading) {
                        presenter.getDiseaseList(etAddAllergy.text.toString())
                    }
                }
            }.let { presenter.disposables.add(it) }

    }

    override fun viewProgress(isShow: Boolean) {}

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

    override fun showProgress() {
        isLoading = true
        rvAllergy.post {
            medicalDiseaseAdapter.mList.add(null)
            medicalDiseaseAdapter.notifyItemInserted(medicalDiseaseAdapter.mList.size)
        }
    }

    override fun hideProgress() {
        isLoading = false
        rvAllergy.post {
            medicalDiseaseAdapter.mList.remove(null)
            medicalDiseaseAdapter.notifyItemRemoved(medicalDiseaseAdapter.mList.size)
        }
    }

    override fun sessionExpired(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
        val intent=Intent(this,SecurityActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
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

    override fun onItemClick(medical: Medical, position: Int) {
        allergyName = medical.id.toString()
        if (!etAddAllergy.text.isNullOrBlank()) {
            mListMedical.clear()
        }
        selectedString = medical.description
        etAddAllergy.setText(medical.description)
        rvAllergy.visibility = View.GONE
    }
}