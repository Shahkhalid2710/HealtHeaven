package com.applocum.connecttomyhealth.ui.exemptions.presenters

import com.applocum.connecttomyhealth.shareddata.endpoints.AppEndPoint
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ExemptionPresenter @Inject constructor(private val api: AppEndPoint) {
    val disposables = CompositeDisposable()
    lateinit var view: View

    fun injectView(view: View) {
        this.view = view
    }

    interface View {

    }
}