package com.applocum.connecttomyhealth.ui.familyhistory

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.changeFont
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.familyhistory.models.FamilyHistory
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
import kotlinx.android.synthetic.main.activity_add_family_history.*
import kotlinx.android.synthetic.main.activity_add_family_history.ivBack
import kotlinx.android.synthetic.main.activity_add_family_history.rvDisease
import kotlinx.android.synthetic.main.custom_progress.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AddFamilyHistoryActivity : BaseActivity(),MedicalPresenter.View,FamilyHistoryPresenter.View {
    var mListFamilyHistory: ArrayList<Medical> = ArrayList()
    private var selectedString=""
    private var isMatched =false
    private var familyHistoryName=""

    @Inject
    lateinit var presenter: MedicalPresenter

    @Inject
    lateinit var familyHistoryPresenter: FamilyHistoryPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).component.inject(this)
        presenter.injectView(this)
        familyHistoryPresenter.injectView(this)

        ivBack.setOnClickListener { finish() }

        ivCancel.setOnClickListener {
            etAddFamilyHistory.text.clear()
            rlDisease.visibility=View.GONE
        }

        RxTextView.textChanges(etAddFamilyHistory)
            .debounce(500, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnEach {
                if (selectedString!=null && selectedString == etAddFamilyHistory.text.toString())
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
                    presenter.getDiseaseList(etAddFamilyHistory.text.toString())
                }
            }.subscribe().let { presenter.disposables.add(it) }

        familyHistoryName=etAddFamilyHistory.text.toString()

        btnSaveFamilyHistory.setOnClickListener {
             familyHistoryPresenter.addFamilyHistory(familyHistoryName)
        }
    }

    override fun getLayoutResourceId(): Int =R.layout.activity_add_family_history

    override fun displayMessage(message: String) {
    }

    override fun getDiseaseList(list: ArrayList<Medical>) {
        mListFamilyHistory=list
        rvDisease.visibility= View.VISIBLE
        rlDisease.visibility=View.GONE
        rvDisease.layoutManager= LinearLayoutManager(this)
        rvDisease.adapter=
            MedicalDiseaseAdapter(this,mListFamilyHistory,object : MedicalDiseaseAdapter.ItemClickListnter{
                override fun onItemClick(medical: Medical, position: Int) {
                    familyHistoryName=medical.id.toString()
                    if(!etAddFamilyHistory.text.isNullOrBlank())
                    {
                        mListFamilyHistory.clear()
                    }
                    selectedString=medical.description
                    etAddFamilyHistory.setText(medical.description)
                    rlDisease.visibility=View.VISIBLE
                    tvDiseaseName.text=medical.description
                    rvDisease.visibility= View.GONE
                }
            })
    }

    override fun viewProgress(isShow: Boolean) {
        progressfamilyHistory.visibility=if (isShow) View.VISIBLE else View.GONE
    }

    override fun viewMedicalProgress(isShow: Boolean) {

    }

    override fun sendMedicalHistoryData(medicalHistory: MedicalHistory) {
    }

    override fun showActiveMedicalHistory(trueMedicalHistory: ArrayList<TrueMedicalHistory>) {
    }

    override fun showPastMedicalHistory(falseMedicalHistory: ArrayList<FalseMedicalHistory>) {
    }

    override fun displayErrorMessage(message: String) {
        val snackbar = Snackbar.make(lladdfamilyhistory, message, Snackbar.LENGTH_LONG)
        snackbar.changeFont()
        val snackview = snackbar.view
        snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
        snackbar.show()
    }

    override fun displaySuccessMessage(message: String) {
        //Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
        this.finish()
    }

    override fun viewFamilyHistoryProgress(isShow: Boolean) {
        progress.visibility=if (isShow) View.VISIBLE else View.GONE
    }

    override fun familyHistoryList(list: ArrayList<FamilyHistory>) {
    }
}