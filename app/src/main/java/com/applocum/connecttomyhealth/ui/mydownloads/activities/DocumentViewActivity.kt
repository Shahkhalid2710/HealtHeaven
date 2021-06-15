package com.applocum.connecttomyhealth.ui.mydownloads.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.prescription.models.Document
import kotlinx.android.synthetic.main.activity_document_view.*
import java.net.URLEncoder


class DocumentViewActivity : BaseActivity() {
    lateinit var document: Document

    override fun getLayoutResourceId(): Int = R.layout.activity_document_view

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ivBack.setOnClickListener { finish() }

        document = intent.getSerializableExtra("document") as Document

        progressWeb.visibility = View.VISIBLE

        val mWebSettings: WebSettings = webView.settings

        mWebSettings.javaScriptCanOpenWindowsAutomatically = true
        mWebSettings.javaScriptEnabled = true
        mWebSettings.setSupportZoom(true)
        mWebSettings.builtInZoomControls = true
        mWebSettings.displayZoomControls = false

        webView.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url="+ URLEncoder.encode(document.file, "ISO-8859-1"))

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return false
            }
            override fun onPageFinished(view: WebView, url: String) {
                progressWeb.visibility = View.GONE
            }
        }
    }
}