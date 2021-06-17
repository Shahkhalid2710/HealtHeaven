package com.applocum.connecttomyhealth.ui.allergyhistory.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.allergyhistory.activities.AddAllergyActivity
import com.applocum.connecttomyhealth.ui.allergyhistory.adapters.PastAllergyHistoryAdapter
import com.applocum.connecttomyhealth.ui.allergyhistory.models.FalseAllergy
import com.applocum.connecttomyhealth.ui.allergyhistory.models.TrueAllergy
import com.applocum.connecttomyhealth.ui.allergyhistory.presenters.AllergyHistoryPresenter
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.custom_allergy_xml.view.*
import kotlinx.android.synthetic.main.custom_loader_progress.view.*
import kotlinx.android.synthetic.main.fragment_past_allergy.*
import kotlinx.android.synthetic.main.fragment_past_allergy.view.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PastAllergyFragment : Fragment(), AllergyHistoryPresenter.View {
    @Inject
    lateinit var presenter: AllergyHistoryPresenter
    lateinit var v: View

    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_past_allergy, container, false)
        MyApplication.getAppContext().component.inject(this)
        presenter.injectView(this)

        RxView.clicks(v.layoutNotfoundPastAllergy.btnAddAllergy).throttleFirst(500,TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(requireActivity(),AddAllergyActivity::class.java))
                requireActivity().overridePendingTransition(0,0)
            }

        presenter.pastAllergy()

        return v
    }

    override fun displaySuccessMessage(message: String) {}

    override fun displayErrorMessage(message: String) {
        val snackbar = Snackbar.make(llPastAllergy, message, Snackbar.LENGTH_LONG)
        val snackview = snackbar.view
        snackview.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.red))
        snackbar.show()
    }

    override fun showActiveAllergy(activeAllergy: ArrayList<TrueAllergy>) {}

    override fun showPastAllergy(pastAllergy: ArrayList<FalseAllergy>) {
        if (pastAllergy.isEmpty())
        {
            layoutNotfoundPastAllergy.visibility=View.VISIBLE
            rvPastAllergy.visibility=View.GONE
        }else
        {
            layoutNotfoundPastAllergy.visibility=View.GONE
            rvPastAllergy.visibility=View.VISIBLE
        }

        rvPastAllergy.layoutManager = LinearLayoutManager(requireActivity())
        rvPastAllergy.adapter = PastAllergyHistoryAdapter(requireActivity(), pastAllergy)
    }

    override fun viewProgress(isShow: Boolean) {
        v.progress.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun viewAllergyProgress(isShow: Boolean) {}

    override fun onResume() {
        presenter.pastAllergy()
        super.onResume()
    }
}