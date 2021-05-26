package com.applocum.connecttomyhealth.ui.mydownloads

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.prescription.models.Document
import kotlinx.android.synthetic.main.activity_document_view.*


class DocumentViewActivity : BaseActivity() {

    lateinit var document: Document

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ivBack.setOnClickListener { finish() }

        document= intent.getSerializableExtra("document") as Document

        progressWeb.visibility=View.VISIBLE

        val mWebSettings: WebSettings = webView.settings

        mWebSettings.javaScriptCanOpenWindowsAutomatically = true
        mWebSettings.javaScriptEnabled = true
        mWebSettings.setSupportZoom(true)
        mWebSettings.loadsImagesAutomatically=true

        webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                progressWeb.visibility=View.GONE

            }
        }
        webView.loadUrl(document.file)

    }
    override fun getLayoutResourceId(): Int=R.layout.activity_document_view
}