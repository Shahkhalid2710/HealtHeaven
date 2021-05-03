package com.applocum.connecttomyhealth.ui.mygp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.mygp.models.GpService
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_g_p_service.*
import kotlinx.android.synthetic.main.custom_gp_service_dialog.view.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.collections.ArrayList

class AddGPServiceActivity : BaseActivity() ,GpservicePresenter.View{

    @Inject
    lateinit var presenter: GpservicePresenter

    lateinit var gpServiceAdapter:GpServiceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ivBack.setOnClickListener { finish() }

        (application as MyApplication).component.inject(this)
        presenter.injectview(this)
        presenter.getgpList("")

        RxTextView.textChanges(etGpSearch)
            .debounce(500, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnEach {
                if (etGpSearch.text.toString().isEmpty()) {
                }
            }
            .observeOn(Schedulers.computation())
            .filter { s -> s.length >= 2}
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                presenter.getgpList(etGpSearch.text.toString())
            }.subscribe().let { presenter.disposables.add(it) }

    }

    override fun getLayoutResourceId(): Int =R.layout.activity_add_g_p_service

    override fun displayMessage(message: String) {
    }

    override fun getGpList(list: ArrayList<GpService>) {
        gpServiceAdapter= GpServiceAdapter(this,list,object :GpServiceAdapter.ItemClickListner{
            override fun onItemClick(gpService: GpService, position: Int) {
                val showDialogView= LayoutInflater.from(this@AddGPServiceActivity).inflate(R.layout.custom_gp_service_dialog,null,false)
                val dialog = AlertDialog.Builder(this@AddGPServiceActivity).create()
                dialog.setView(showDialogView)
                dialog.setCanceledOnTouchOutside(false)

                showDialogView.tvGpName.text=gpService.practice_name
                showDialogView.tvGpArea.text=gpService.address
                showDialogView.tvGpCity.text=gpService.city

                showDialogView.btnSubmit.setOnClickListener {
                    dialog.dismiss()
                    val intent= Intent(this@AddGPServiceActivity,GpServiceActivity::class.java)
                    startActivity(intent)
                }
                showDialogView.btnCancel.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.show()
            }
        })
        rvAddGp.layoutManager=LinearLayoutManager(this)
        rvAddGp.adapter=gpServiceAdapter
    }

    override fun viewProgress(isShow: Boolean) {
        progress.visibility=if (isShow) View.VISIBLE else View.GONE
    }
}