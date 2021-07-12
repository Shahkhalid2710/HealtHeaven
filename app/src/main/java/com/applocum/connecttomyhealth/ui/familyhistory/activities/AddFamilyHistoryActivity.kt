package com.applocum.connecttomyhealth.ui.familyhistory.activities

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
import com.applocum.connecttomyhealth.ui.familyhistory.models.FamilyHistory
import com.applocum.connecttomyhealth.ui.familyhistory.presenters.FamilyHistoryPresenter
import com.applocum.connecttomyhealth.ui.medicalhistory.adapters.MedicalDiseaseAdapter
import com.applocum.connecttomyhealth.ui.medicalhistory.models.FalseMedicalHistory
import com.applocum.connecttomyhealth.ui.medicalhistory.models.Medical
import com.applocum.connecttomyhealth.ui.medicalhistory.models.MedicalHistory
import com.applocum.connecttomyhealth.ui.medicalhistory.models.TrueMedicalHistory
import com.applocum.connecttomyhealth.ui.medicalhistory.presenters.MedicalPresenter
import com.applocum.connecttomyhealth.ui.securitycheck.activities.SecurityActivity
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_family_history.*
import kotlinx.android.synthetic.main.custom_progress.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AddFamilyHistoryActivity : BaseActivity(), MedicalPresenter.View, FamilyHistoryPresenter.View,
    MedicalDiseaseAdapter.ItemClickListnter {
    var mListFamilyHistory: ArrayList<Medical> = ArrayList()
    private var selectedString = ""
    private var isMatched = false
    private var familyHistoryName = ""
    lateinit var medicalDiseaseAdapter: MedicalDiseaseAdapter
    private var isLoading = false

    @Inject
    lateinit var presenter: MedicalPresenter

    @Inject
    lateinit var familyHistoryPresenter: FamilyHistoryPresenter

    override fun getLayoutResourceId(): Int = R.layout.activity_add_family_history

    override fun handleInternetConnectivity(isConnect: Boolean?) {}

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).component.inject(this)
        presenter.injectView(this)
        familyHistoryPresenter.injectView(this)

        medicalDiseaseAdapter = MedicalDiseaseAdapter(this, ArrayList(), this)
        rvDisease.layoutManager = LinearLayoutManager(this)
        rvDisease.adapter = medicalDiseaseAdapter

        RxView.clicks(ivBack).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe { finish() }

        RxView.clicks(ivCancel).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                familyHistoryName = ""
                etAddFamilyHistory.text.clear()
                rlDisease.visibility = View.GONE
            }

        RxTextView.textChanges(etAddFamilyHistory)
            .debounce(500, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnEach {
                if (selectedString == etAddFamilyHistory.text.toString()) {
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
                    presenter.getDiseaseList(etAddFamilyHistory.text.toString())
                    val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(lladdfamilyhistory.windowToken, 0)
                }
            }.subscribe().let { presenter.disposables.add(it) }

        familyHistoryName = etAddFamilyHistory.text.toString()

        RxView.clicks(btnSaveFamilyHistory).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(lladdfamilyhistory.windowToken, 0)
                familyHistoryPresenter.addFamilyHistory(familyHistoryName)
            }
    }


    override fun displayMessage(message: String) {
        val snackbar = Snackbar.make(lladdfamilyhistory,message, Snackbar.LENGTH_LONG)
        snackbar.changeFont()
        val snackview = snackbar.view
        snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
        snackbar.show()
    }

    override fun getDiseaseList(list: ArrayList<Medical>) {
        mListFamilyHistory = list
        rvDisease.visibility = View.VISIBLE
        rlDisease.visibility = View.GONE

        medicalDiseaseAdapter.mList.addAll(list)
        medicalDiseaseAdapter.notifyItemRangeInserted(medicalDiseaseAdapter.mList.size, list.size)
        RxRecyclerView.scrollEvents(rvDisease)
            .subscribe {
                val total = rvDisease.layoutManager?.itemCount ?: 0
                val last = (rvDisease.layoutManager as LinearLayoutManager)
                    .findLastVisibleItemPosition()
                if (total > 0 && total <= last + 2) {
                    if (!isLoading) {
                        presenter.getDiseaseList(etAddFamilyHistory.text.toString())
                    }
                }
            }.let { presenter.disposables.add(it) }

    }

    override fun viewMedicalProgress(isShow: Boolean) {}

    override fun sendMedicalHistoryData(medicalHistory: MedicalHistory) {}

    override fun showActiveMedicalHistory(trueMedicalHistory: ArrayList<TrueMedicalHistory>) {}

    override fun showPastMedicalHistory(falseMedicalHistory: ArrayList<FalseMedicalHistory>) {}

    override fun noInternet(isConnect: Boolean) {
        if (!isConnect)
        {
            val snackbar = Snackbar.make(lladdfamilyhistory,R.string.no_internet, Snackbar.LENGTH_LONG)
            snackbar.changeFont()
            val snackview = snackbar.view
            snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            snackbar.show()
        }
    }

    override fun showProgress() {
        isLoading = true
        rvDisease.post {
            medicalDiseaseAdapter.mList.add(null)
            medicalDiseaseAdapter.notifyItemInserted(medicalDiseaseAdapter.mList.size)
        }
    }

    override fun hideProgress() {
        isLoading = false
        rvDisease.post {
            medicalDiseaseAdapter.mList.remove(null)
            medicalDiseaseAdapter.notifyItemRemoved(medicalDiseaseAdapter.mList.size)
        }
    }

    override fun sessionExpired(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
        val intent=Intent(this,SecurityActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    override fun displayErrorMessage(message: String) {
        val snackbar = Snackbar.make(lladdfamilyhistory, message, Snackbar.LENGTH_LONG)
        snackbar.changeFont()
        val snackview = snackbar.view
        snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
        snackbar.show()
    }

    override fun displaySuccessMessage(message: String) {
        this.finish()
    }

    override fun viewFamilyHistoryProgress(isShow: Boolean) {
        progress.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun familyHistoryList(list: ArrayList<FamilyHistory>) {}

    override fun onItemClick(medical: Medical, position: Int) {
        familyHistoryName = medical.id.toString()
        if (!etAddFamilyHistory.text.isNullOrBlank()) {
            mListFamilyHistory.clear()
        }
        selectedString = medical.description
        etAddFamilyHistory.setText(medical.description)
        rlDisease.visibility = View.VISIBLE
        tvDiseaseName.text = medical.description
        rvDisease.visibility = View.GONE
    }
}