package com.applocum.connecttomyhealth.ui.investigation.activities

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.changeFont
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.investigation.models.Investigation
import com.applocum.connecttomyhealth.ui.investigation.presenters.InvestigationPresenter
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
import kotlinx.android.synthetic.main.activity_add_investigation.*
import kotlinx.android.synthetic.main.custom_progress.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.collections.ArrayList

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class AddInvestigationActivity : BaseActivity(), DatePickerDialog.OnDateSetListener,
    MedicalPresenter.View, InvestigationPresenter.View,
    MedicalDiseaseAdapter.ItemClickListnter {

    private var day: Int = 0
    private var month: Int = 0
    private var year: Int = 0
    private var myDay: Int = 0
    private var myMonth: Int = 0
    private var myYear: Int = 0
    var mListMedical: ArrayList<Medical> = ArrayList()
    private var selectedString = ""
    private var isMatched = false
    private var investigationName = ""
    lateinit var medicalDiseaseAdapter: MedicalDiseaseAdapter
    private var isLoading = false

    @Inject
    lateinit var presenter: MedicalPresenter

    @Inject
    lateinit var investigationPresenter: InvestigationPresenter

    override fun getLayoutResourceId(): Int = R.layout.activity_add_investigation

    override fun handleInternetConnectivity(isConnect: Boolean?) {}

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as MyApplication).component.inject(this)
        presenter.injectView(this)
        investigationPresenter.injectView(this)

        RxView.clicks(ivBack).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe {
                finish()
            }

        medicalDiseaseAdapter = MedicalDiseaseAdapter(this, ArrayList(), this)
        rvDisease.layoutManager = LinearLayoutManager(this)
        rvDisease.adapter = medicalDiseaseAdapter

        RxTextView.textChanges(etInvestigationName)
            .debounce(500, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnEach {
                if (selectedString == etInvestigationName.text.toString()) {
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
                    presenter.getDiseaseList(etInvestigationName.text.toString())
                    val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(llAddInvesigation.windowToken, 0)
                }
            }.subscribe().let { presenter.disposables.add(it) }

        etInvestigationdate.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            this.day = calendar.get(Calendar.DAY_OF_MONTH)
            this.month = calendar.get(Calendar.MONTH)
            this.year = calendar.get(Calendar.YEAR)

            val datePickerDialog = DatePickerDialog(this, R.style.DialogTheme, this, year, month, day)
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
            datePickerDialog.show()
        }


        investigationName = etInvestigationName.text.toString()

        RxView.clicks(btnAddInvestigation).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe {
                val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(llAddInvesigation.windowToken, 0)
                investigationPresenter.addInvestigation(
                    investigationName,
                    etInvestigationdate.text.toString(),
                    etInvestigationDescription.text.toString()
                )
            }
    }

    @SuppressLint("SimpleDateFormat")
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        myYear = year
        myMonth = month
        myDay = dayOfMonth

        val date = "" + myDay + "/" + (myMonth + 1) + "/" + myYear
        var spf = SimpleDateFormat("dd/MM/yy")
        val newDate = spf.parse(date)
        spf = SimpleDateFormat("yyyy-MM-dd")
        val newDateString = spf.format(newDate)
        println(newDateString)
        etInvestigationdate.setText(newDateString)
    }

    override fun displaySuccessMessage(message: String) {
        this.finish()
    }

    override fun displayMessage(message: String) {
        val snackBar = Snackbar.make(llAddInvesigation, message, Snackbar.LENGTH_LONG)
        snackBar.changeFont()
        val snackView = snackBar.view
        snackView.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
        snackBar.show()
    }

    override fun viewInvestigationProgress(isShow: Boolean) {
        progress.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun getDiseaseList(list: ArrayList<Medical>) {
        mListMedical = list
        rvDisease.visibility = View.VISIBLE

        medicalDiseaseAdapter.mList.addAll(list)
        medicalDiseaseAdapter.notifyItemRangeInserted(medicalDiseaseAdapter.mList.size, list.size)
        RxRecyclerView.scrollEvents(rvDisease)
            .subscribe {
                val total = rvDisease.layoutManager?.itemCount ?: 0
                val last = (rvDisease.layoutManager as LinearLayoutManager)
                    .findLastVisibleItemPosition()
                if (total > 0 && total <= last + 2) {
                    if (!isLoading) {
                        presenter.getDiseaseList(etInvestigationName.text.toString())
                    }
                }
            }.let { presenter.disposables.add(it) }
    }

    override fun viewMedicalProgress(isShow: Boolean) {
        progress.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun investigationList(list: ArrayList<Investigation?>,page:String?) {}

    override fun sendMedicalHistoryData(medicalHistory: MedicalHistory) {}

    override fun showActiveMedicalHistory(trueMedicalHistory: ArrayList<TrueMedicalHistory>) {}

    override fun showPastMedicalHistory(falseMedicalHistory: ArrayList<FalseMedicalHistory>) {}

    override fun noInternet(isConnect: Boolean) {
        if (!isConnect) {
            val snackBar =
                Snackbar.make(llAddInvesigation, R.string.no_internet, Snackbar.LENGTH_LONG)
            snackBar.changeFont()
            val snackView = snackBar.view
            snackView.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            snackBar.show()
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

    override fun onItemClick(medical: Medical, position: Int) {
        investigationName = medical.id.toString()
        if (!etInvestigationName.text.isNullOrBlank()) {
            mListMedical.clear()
        }
        selectedString = medical.description
        etInvestigationName.setText(medical.description)
        rvDisease.visibility = View.GONE
    }
}