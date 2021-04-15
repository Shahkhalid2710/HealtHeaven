package com.applocum.connecttomyhealth.ui.mygp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.mygp.models.GpService
import kotlinx.android.synthetic.main.activity_add_g_p_service.*
import java.util.*
import kotlin.collections.ArrayList

class AddGPServiceActivity : BaseActivity(), TextWatcher {
    var mListGpService:ArrayList<GpService> = ArrayList()
    lateinit var gpServiceAdapter:GpServiceAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ivBack.setOnClickListener { finish() }

        val gpService1=GpService("Route Du Fort Surgery","Lido M/C, St. Saviours Road","St. Saviours, Jersey")
        val gpService2=GpService("Lido Medical Practice","St. Saviours Road","St. Saviours, Jersey")
        val gpService3=GpService("Dr. Do Yates’ Practice","Well Street, Cheadle","Stroke on Trent, Staffordshire")
        val gpService4=GpService("Mount Group Practice","Doncaster","South Yorkshire")
        val gpService5=GpService("Gateway Primary Care","Doncaster Gate","Rotherham")
        val gpService6=GpService("Gate Group Practice","Doncaster","South Yorkshire")
        val gpService7=GpService("Route Du Fort Surgery","Lido M/C, St. Saviours Road","St. Saviours, Jersey")

        mListGpService.add(gpService1)
        mListGpService.add(gpService2)
        mListGpService.add(gpService3)
        mListGpService.add(gpService4)
        mListGpService.add(gpService5)
        mListGpService.add(gpService6)
        mListGpService.add(gpService7)

        gpServiceAdapter= GpServiceAdapter(this,mListGpService)
        rvAddGp.layoutManager=LinearLayoutManager(this)
        rvAddGp.adapter=gpServiceAdapter

        etGpSearch.addTextChangedListener(this)

    }

    override fun getLayoutResourceId(): Int =R.layout.activity_add_g_p_service

    override fun afterTextChanged(s: Editable?) {
        filter(s.toString())
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

    private fun filter(text: String) {
        val filteredList: ArrayList<GpService> = ArrayList()
        for (item in mListGpService) {
            if (item.gName.toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT))) {
                filteredList.add(item)
            }
        }
        gpServiceAdapter.filterList(filteredList)
    }
}