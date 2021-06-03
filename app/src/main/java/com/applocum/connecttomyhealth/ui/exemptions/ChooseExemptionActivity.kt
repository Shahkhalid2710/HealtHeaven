package com.applocum.connecttomyhealth.ui.exemptions

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.exemptions.models.Exemption
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_choose_exemption.*
import kotlinx.android.synthetic.main.activity_choose_exemption.ivBack
import java.util.concurrent.TimeUnit

class ChooseExemptionActivity : BaseActivity() {
    private var mListExemption:ArrayList<Exemption> = ArrayList()

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RxView.clicks(ivBack).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                finish()
            }

        val exemption1=Exemption("A","I am under 16 years of age")
        val exemption2=Exemption("B","I am 16,17 or 18 years of age and in full-time education")
        val exemption3=Exemption("C","I am 60 years of age or over")
        val exemption4=Exemption("D","I have a valid maternity exemption certificatation")
        val exemption5=Exemption("E","I have a valid medical exemption certification.")
        val exemption6=Exemption("F","I have a valid prescription pre-payment certification.")
        val exemption7=Exemption("G","I have a valid war pention exemption certificate.")
        val exemption8=Exemption("L","I have been named on a current HC2 charges certificate.")
        val exemption9=Exemption("X","I was prescribed free-of-charge contraceptives")
        val exemption10=Exemption("H","I get income support or income-related employment and support allowance.")
        val exemption11=Exemption("K","I get income-based jobseeker's allowance")
        val exemption12=Exemption("M","I am entitled to, or named on, a valid NHS tax credit exemption certificate")

        mListExemption.add(exemption1)
        mListExemption.add(exemption2)
        mListExemption.add(exemption3)
        mListExemption.add(exemption4)
        mListExemption.add(exemption5)
        mListExemption.add(exemption6)
        mListExemption.add(exemption7)
        mListExemption.add(exemption8)
        mListExemption.add(exemption9)
        mListExemption.add(exemption10)
        mListExemption.add(exemption11)
        mListExemption.add(exemption12)

        rvExemptions.layoutManager= LinearLayoutManager(this)
        rvExemptions.adapter=ExemptionAdapter(this,mListExemption)

        RxView.clicks(btnAddCertificate).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this,UploadCertificateActivity::class.java))
            }

    }

    override fun getLayoutResourceId(): Int = R.layout.activity_choose_exemption
}