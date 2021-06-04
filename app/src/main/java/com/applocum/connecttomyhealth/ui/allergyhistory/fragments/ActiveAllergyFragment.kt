package com.applocum.connecttomyhealth.ui.allergyhistory.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.allergyhistory.adapters.ActiveAllergyHistoryAdapter
import com.applocum.connecttomyhealth.ui.allergyhistory.models.FalseAllergy
import com.applocum.connecttomyhealth.ui.allergyhistory.models.TrueAllergy
import com.applocum.connecttomyhealth.ui.allergyhistory.presenters.AllergyHistoryPresenter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.custom_loader_progress.view.progress
import kotlinx.android.synthetic.main.fragment_active_allergy.*
import javax.inject.Inject

class ActiveAllergyFragment : Fragment(),
    AllergyHistoryPresenter.View {

    @Inject
    lateinit var presenter: AllergyHistoryPresenter

    lateinit var v: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_active_allergy, container, false)
        MyApplication.getAppContext().component.inject(this)
        presenter.injectView(this)

        presenter.activeAllergy()

        return v
    }

    override fun displaySuccessMessage(message: String) {

    }

    override fun displayErrorMessage(message: String) {
        val snackbar = Snackbar.make(llActiveAllergy, message, Snackbar.LENGTH_LONG)
        val snackview = snackbar.view
        snackview.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.red))
        snackbar.show()
    }

    override fun showActiveAllergy(activeAllergy: ArrayList<TrueAllergy>) {
        rvActiveAllergy.layoutManager=LinearLayoutManager(requireActivity())
        rvActiveAllergy.adapter=ActiveAllergyHistoryAdapter(requireActivity(),activeAllergy)
    }

    override fun showPastAllergy(pastAllergy: ArrayList<FalseAllergy>) {

    }

    override fun viewProgress(isShow: Boolean) {
        v.progress.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun viewAllergyProgress(isShow: Boolean) {
    }

    override fun onResume() {
        presenter.activeAllergy()
        super.onResume()
    }
}