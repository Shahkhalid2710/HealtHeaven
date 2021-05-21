package com.applocum.connecttomyhealth.ui.mydownloads

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.prescription.models.Document
import kotlinx.android.synthetic.main.activity_document_view.*


class DocumentViewActivity : BaseActivity() {

    lateinit var document: Document


    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ivBack.setOnClickListener { finish() }

        document= intent.getSerializableExtra("document") as Document


      /*  val settings: WebSettings = webView.settings
        settings.javaScriptEnabled=true
        settings.loadsImagesAutomatically=true
        settings.useWideViewPort=true
        settings.loadWithOverviewMode=true
        settings.mediaPlaybackRequiresUserGesture=true
        webView.scrollBarStyle= View.SCROLLBARS_OUTSIDE_OVERLAY
*/



        webView.settings.javaScriptEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
            }
        }
        webView.loadUrl(document.file)

        //Log.d("fileee","->"+document.file)


    }
    override fun getLayoutResourceId(): Int=R.layout.activity_document_view
}