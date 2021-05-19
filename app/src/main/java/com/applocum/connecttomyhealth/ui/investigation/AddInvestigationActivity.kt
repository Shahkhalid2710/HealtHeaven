package com.applocum.connecttomyhealth.ui.investigation

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.investigation.models.Investigation
import com.applocum.connecttomyhealth.ui.medicalhistory.adapters.MedicalDiseaseAdapter
import com.applocum.connecttomyhealth.ui.medicalhistory.models.FalseMedicalHistory
import com.applocum.connecttomyhealth.ui.medicalhistory.models.Medical
import com.applocum.connecttomyhealth.ui.medicalhistory.models.MedicalHistory
import com.applocum.connecttomyhealth.ui.medicalhistory.models.TrueMedicalHistory
import com.applocum.connecttomyhealth.ui.medicalhistory.presenters.MedicalPresenter
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_investigation.*
import kotlinx.android.synthetic.main.activity_add_investigation.ivBack
import kotlinx.android.synthetic.main.custom_progress.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.collections.ArrayList

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class AddInvestigationActivity : BaseActivity(), DatePickerDialog.OnDateSetListener,MedicalPresenter.View,InvestigationPresenter.View {

    private var day: Int = 0
    private var month: Int = 0
    private var year: Int = 0
    private var myDay: Int = 0
    private var myMonth: Int = 0
    private var myYear: Int = 0
    var mListMedical: ArrayList<Medical> = ArrayList()
    private var selectedString=""
    private var isMatched =false
    private var investigationName=""

    @Inject
    lateinit var presenter: MedicalPresenter

    @Inject
    lateinit var investigationPresenter: InvestigationPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ivBack.setOnClickListener { finish() }
        (application as MyApplication).component.inject(this)
        presenter.injectView(this)
        investigationPresenter.injectView(this)

        RxTextView.textChanges(etInvestigationName)
            .debounce(500, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnEach {
                if (selectedString!=null && selectedString == etInvestigationName.text.toString())
                {
                    isMatched=true
                }
                else
                {
                    isMatched=false
                    selectedString=""
                }

            }
            .observeOn(Schedulers.computation())
            .filter { s -> s.length >= 2}
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                if (!isMatched)
                {
                    presenter.getDiseaseList(etInvestigationName.text.toString())
                }
            }.subscribe().let { presenter.disposables.add(it) }

        etInvestigationdate.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            this.day = calendar.get(Calendar.DAY_OF_MONTH)
            this.month = calendar.get(Calendar.MONTH)
            this.year = calendar.get(Calendar.YEAR)

            val datePickerDialog = DatePickerDialog(this,R.style.DialogTheme ,this, year, month, day)
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
            datePickerDialog.show()
        }


        investigationName=etInvestigationName.text.toString()

        btnAddInvestigation.setOnClickListener {
            investigationPresenter.addInvestigation(investigationName,etInvestigationdate.text.toString(),etInvestigationDescription.text.toString())
        }
    }

    override fun getLayoutResourceId(): Int =R.layout.activity_add_investigation

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
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

    override fun displayMessage(message: String) {
        val snackBar = Snackbar.make(llAddInvesigation, message, Snackbar.LENGTH_LONG)
        val snackView = snackBar.view
        snackView.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
        snackBar.show()
    }

    override fun viewInvestigationProgress(isShow: Boolean) {
         progress.visibility=if (isShow) View.VISIBLE else View.GONE
    }

    override fun getDiseaseList(list: ArrayList<Medical>) {
        mListMedical=list
        rvDisease.visibility= View.VISIBLE
        rvDisease.layoutManager= LinearLayoutManager(this)
        rvDisease.adapter=
            MedicalDiseaseAdapter(this,mListMedical,object : MedicalDiseaseAdapter.ItemClickListnter{
                override fun onItemClick(medical: Medical, position: Int) {
                    investigationName=medical.id.toString()
                    if(!etInvestigationName.text.isNullOrBlank())
                    {
                        mListMedical.clear()
                    }
                    selectedString=medical.description
                    etInvestigationName.setText(medical.description)
                    rvDisease.visibility= View.GONE
                }
            })
    }

    override fun viewProgress(isShow: Boolean) {
        progressInvestigation.visibility=if (isShow) View.VISIBLE else View.GONE
    }

    override fun viewMedicalProgress(isShow: Boolean) {
        progress.visibility=if (isShow) View.VISIBLE else View.GONE
    }

    override fun investigationList(list: ArrayList<Investigation>) {
    }

    override fun sendMedicalHistoryData(medicalHistory: MedicalHistory) {
    }

    override fun showActiveMedicalHistory(trueMedicalHistory: ArrayList<TrueMedicalHistory>) {
    }

    override fun showPastMedicalHistory(falseMedicalHistory: ArrayList<FalseMedicalHistory>) {
    }
}