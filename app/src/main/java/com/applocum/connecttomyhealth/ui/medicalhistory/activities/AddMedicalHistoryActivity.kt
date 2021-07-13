package com.applocum.connecttomyhealth.ui.medicalhistory.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.changeFont
import com.applocum.connecttomyhealth.ui.BaseActivity
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
import kotlinx.android.synthetic.main.activity_add_medical_history.*
import kotlinx.android.synthetic.main.activity_add_medical_history.ivBack
import kotlinx.android.synthetic.main.activity_add_medical_history.llMedicalHistory
import kotlinx.android.synthetic.main.custom_progress.*
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.collections.ArrayList


class AddMedicalHistoryActivity : BaseActivity(), MedicalPresenter.View,
    MedicalDiseaseAdapter.ItemClickListnter {

    private var mListMedical: ArrayList<Medical> = ArrayList()
    private var diseaseid = ""
    private var selectedString = ""
    private var isMatched = false
    private var isActivePast = false
    lateinit var medicalDiseaseAdapter: MedicalDiseaseAdapter
    private var isLoading = false

    @Inject
    lateinit var presenter: MedicalPresenter

    override fun getLayoutResourceId(): Int = R.layout.activity_add_medical_history

    override fun handleInternetConnectivity(isConnect: Boolean?) {}

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).component.inject(this)
        presenter.injectView(this)

        medicalDiseaseAdapter = MedicalDiseaseAdapter(this, ArrayList(), this)
        rvMedicalDisease.layoutManager = LinearLayoutManager(this)
        rvMedicalDisease.adapter = medicalDiseaseAdapter

        RxView.clicks(ivBack).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe { finish() }

        RxView.clicks(etStartMonth).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                selectStartMonth()
            }

        RxView.clicks(etStartYear).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                selectStartYear()
            }

        RxView.clicks(etEndMonth).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                selectEndMonth()
            }

        RxView.clicks(etEndYear).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                selectEndYear()
            }

        RxTextView.textChanges(etDiseaseName)
            .debounce(500, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnEach {
                if (selectedString == etDiseaseName.text.toString()) {
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
                    presenter.getDiseaseList(etDiseaseName.text.toString())
                }
            }.subscribe().let { presenter.disposables.add(it) }

        cbActiveCurrently.setOnCheckedChangeListener { _, b ->
            if (b) {
                llEndDate.visibility = View.GONE
                isActivePast = true
            } else {
                llEndDate.visibility = View.VISIBLE
                isActivePast = false
            }
        }

        diseaseid = etDiseaseName.text.toString()

        RxView.clicks(btnSaveDiseases).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(llMedicalHistory.windowToken, 0)

                    presenter.addMedicalHistory(
                        diseaseid,
                        etStartMonth.text.toString(),
                        etStartYear.text.toString(),
                        isActivePast,
                        etEndMonth.text.toString(),
                        etEndYear.text.toString()
                    )
            }
    }

    override fun displayMessage(message: String) {
        val snackBar = Snackbar.make(llMedicalHistory, message, Snackbar.LENGTH_LONG)
        snackBar.changeFont()
        val snackView = snackBar.view
        snackView.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
        snackBar.show()
    }

    override fun getDiseaseList(list: ArrayList<Medical>) {
        mListMedical = list
        rvMedicalDisease.visibility = View.VISIBLE

        medicalDiseaseAdapter.mList.addAll(list)
        medicalDiseaseAdapter.notifyItemRangeInserted(medicalDiseaseAdapter.mList.size, list.size)

        RxRecyclerView.scrollEvents(rvMedicalDisease)
            .subscribe {
                val total = rvMedicalDisease.layoutManager?.itemCount ?: 0
                val last = (rvMedicalDisease.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                if (total > 0 && total <= last + 2) {
                    if (!isLoading) {
                        presenter.getDiseaseList(etDiseaseName.text.toString())
                    }
                }
            }.let { presenter.disposables.add(it) }
    }

    override fun viewMedicalProgress(isShow: Boolean) {
        progress.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun sendMedicalHistoryData(medicalHistory: MedicalHistory) {
        finish()
    }

    override fun showActiveMedicalHistory(trueMedicalHistory: ArrayList<TrueMedicalHistory>) {}

    override fun showPastMedicalHistory(falseMedicalHistory: ArrayList<FalseMedicalHistory>) {}

    override fun noInternet(isConnect: Boolean) {
        if (!isConnect) {
            val snackBar = Snackbar.make(llMedicalHistory, R.string.no_internet, Snackbar.LENGTH_LONG)
            snackBar.changeFont()
            val snackView = snackBar.view
            snackView.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            snackBar.show()
        }
    }

    override fun showProgress() {
        isLoading = true
        rvMedicalDisease.post {
            medicalDiseaseAdapter.mList.add(null)
            medicalDiseaseAdapter.notifyItemInserted(medicalDiseaseAdapter.mList.size)
        }
    }

    override fun hideProgress() {
        isLoading = false
        rvMedicalDisease.post {
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

    private fun selectStartMonth() {
        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialogStyle)
        builder.setTitle("Select month")
        val startMonth = resources.getStringArray(R.array.Months)
        val dataAdapter = ArrayAdapter(this, R.layout.custom_drop_down_item, startMonth)
        builder.setAdapter(dataAdapter) { _, which ->
            etStartMonth.setText(startMonth[which]).toString()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun selectStartYear() {
        val years = ArrayList<String>()
        val thisYear: Int = Calendar.getInstance().get(Calendar.YEAR)
        for (i in thisYear downTo 1991) {
            years.add(i.toString())
        }
        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialogStyle)
        builder.setTitle("Select year")
        val dataAdapter = ArrayAdapter(this, R.layout.custom_drop_down_item, years)
        builder.setAdapter(dataAdapter) { _, which ->
            etStartYear.setText(years[which]).toString()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun selectEndMonth() {
        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialogStyle)
        builder.setTitle("Select month")
        val endMonth = resources.getStringArray(R.array.Months)
        val dataAdapter = ArrayAdapter(this, R.layout.custom_drop_down_item, endMonth)
        builder.setAdapter(dataAdapter) { _, which ->
            etEndMonth.setText(endMonth[which]).toString()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun selectEndYear() {
        val years = ArrayList<String>()
        val thisYear: Int = Calendar.getInstance().get(Calendar.YEAR)
        for (i in thisYear downTo 1991) {
            years.add(i.toString())
        }

        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialogStyle)
        builder.setTitle("Select year")
        val dataAdapter = ArrayAdapter(this, R.layout.custom_drop_down_item, years)
        builder.setAdapter(dataAdapter) { _, which ->
            etEndYear.setText(years[which]).toString()
        }
        val dialog = builder.create()
        dialog.show()
    }

    override fun onItemClick(medical: Medical, position: Int) {
        diseaseid = medical.id.toString()
        if (!etDiseaseName.text.isNullOrBlank()) {
            mListMedical.clear()
        }
        selectedString = medical.description
        etDiseaseName.setText(medical.description)
        rvMedicalDisease.visibility = View.GONE
    }
}