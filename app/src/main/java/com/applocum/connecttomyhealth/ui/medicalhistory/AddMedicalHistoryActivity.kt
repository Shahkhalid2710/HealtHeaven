package com.applocum.connecttomyhealth.ui.medicalhistory

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
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
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_medical_history.*
import kotlinx.android.synthetic.main.custom_progress.*
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.collections.ArrayList


class AddMedicalHistoryActivity : BaseActivity(), MedicalPresenter.View{

    var mListMedical: ArrayList<Medical> = ArrayList()
    var diseaseid=""
    private var selectedString=""
    private var isMatched =false
    private var isActivePast =false


    @Inject
    lateinit var presenter: MedicalPresenter

    override fun getLayoutResourceId(): Int = R.layout.activity_add_medical_history
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ivBack.setOnClickListener { finish() }
        (application as MyApplication).component.inject(this)
        presenter.injectView(this)

        etStartMonth.setOnClickListener { selectStartMonth() }
        etStartYear.setOnClickListener { selectStartYear() }
        etEndMonth.setOnClickListener { selectEndMonth() }
        etEndYear.setOnClickListener { selectEndYear() }


        RxTextView.textChanges(etDiseaseName)
            .debounce(500, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnEach {
               if (selectedString!=null && selectedString == etDiseaseName.text.toString())
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
                    presenter.getDiseaseList(etDiseaseName.text.toString())
                }
            }.subscribe().let { presenter.disposables.add(it) }


        cbActiveCurrently.setOnCheckedChangeListener { _, b ->
            if (b) {
                llEndDate.visibility=View.GONE
                isActivePast=true
            }
            else
            {
                llEndDate.visibility=View.VISIBLE
                isActivePast=false
            }
        }

        diseaseid=etDiseaseName.text.toString()

        btnSaveDiseases.setOnClickListener {
             presenter.addMedicalHistory(diseaseid,etStartMonth.text.toString(),etStartYear.text.toString(),isActivePast,etEndMonth.text.toString(),etEndYear.text.toString())
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
        mListMedical=list
        rvMedicalDisease.visibility=View.VISIBLE
        rvMedicalDisease.layoutManager= LinearLayoutManager(this)
        rvMedicalDisease.adapter=MedicalDiseaseAdapter(this,mListMedical,object :MedicalDiseaseAdapter.ItemClickListnter{
           override fun onItemClick(medical: Medical, position: Int) {
               diseaseid=medical.id.toString()
               if(!etDiseaseName.text.isNullOrBlank())
               {
                   mListMedical.clear()
               }
               selectedString=medical.description
               etDiseaseName.setText(medical.description)
               rvMedicalDisease.visibility=View.GONE
           }
       })
    }
    override fun viewProgress(isShow: Boolean) {
         progressMedical.visibility=if (isShow) View.VISIBLE else View.GONE
    }

    override fun viewMedicalProgress(isShow: Boolean) {
        progress.visibility=if (isShow) View.VISIBLE else View.GONE
    }

    override fun sendMedicalHistoryData(medicalHistory: MedicalHistory) {
          finish()
    }

    override fun showActiveMedicalHistory(trueMedicalHistory: ArrayList<TrueMedicalHistory>) {

    }

    override fun showPastMedicalHistory(falseMedicalHistory: ArrayList<FalseMedicalHistory>) {
    }

    private fun selectStartMonth()
    {
        val builder = AlertDialog.Builder(this,R.style.CustomAlertDialogStyle)
        builder.setTitle("Select month")
        val startMonth=resources.getStringArray(R.array.Months)
        val dataAdapter = ArrayAdapter(this,R.layout.custom_drop_down_item,startMonth)
        builder.setAdapter(dataAdapter) { _, which ->
            etStartMonth.setText(startMonth[which]).toString()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun selectStartYear()
    {
        val years = ArrayList<String>()
        val thisYear: Int = Calendar.getInstance().get(Calendar.YEAR)
        for (i in thisYear downTo 1991) {
            years.add(i.toString())
        }
        val builder = AlertDialog.Builder(this,R.style.CustomAlertDialogStyle)
        builder.setTitle("Select year")
        val dataAdapter = ArrayAdapter(this,R.layout.custom_drop_down_item,years)
        builder.setAdapter(dataAdapter) { _, which ->
            etStartYear.setText(years[which]).toString()
        }
        val dialog = builder.create()
        dialog.show()

    }
    private fun selectEndMonth()
    {
        val builder = AlertDialog.Builder(this,R.style.CustomAlertDialogStyle)
        builder.setTitle("Select month")
        val endMonth=resources.getStringArray(R.array.Months)
        val dataAdapter = ArrayAdapter(this,R.layout.custom_drop_down_item,endMonth)
        builder.setAdapter(dataAdapter) { _, which ->
            etEndMonth.setText(endMonth[which]).toString()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun selectEndYear()
    {
        val years = ArrayList<String>()
        val thisYear: Int = Calendar.getInstance().get(Calendar.YEAR)
        for (i in thisYear downTo 1991) {
            years.add(i.toString())
        }

        val builder = AlertDialog.Builder(this,R.style.CustomAlertDialogStyle)
        builder.setTitle("Select year")
        val dataAdapter = ArrayAdapter(this,R.layout.custom_drop_down_item,years)
        builder.setAdapter(dataAdapter) { _, which ->
            etEndYear.setText(years[which]).toString()
        }
        val dialog = builder.create()
        dialog.show()

    }
}