package com.applocum.connecttomyhealth.ui.mygp.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.changeFont
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.mygp.models.GpService
import com.applocum.connecttomyhealth.ui.mygp.models.Surgery
import com.applocum.connecttomyhealth.ui.mygp.presenters.GpservicePresenter
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_gp_service.*
import kotlinx.android.synthetic.main.activity_gp_service.ivBack
import kotlinx.android.synthetic.main.custom_mygp_xml.*
import kotlinx.android.synthetic.main.custom_no_internet.view.*
import kotlinx.android.synthetic.main.custom_progress.*
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.inject.Inject
import kotlin.collections.ArrayList

@Suppress("DEPRECATION", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class GpServiceActivity : BaseActivity(), GpservicePresenter.View {

    @Inject
    lateinit var presenter: GpservicePresenter
    private lateinit var map: GoogleMap
    private lateinit var supportMapFragment: SupportMapFragment

    override fun getLayoutResourceId(): Int = R.layout.activity_gp_service

    override fun handleInternetConnectivity(isConnect: Boolean?) {}

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).component.inject(this)
        presenter.injectview(this)

        RxView.clicks(ivBack).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe { finish() }

        RxView.clicks(gpserivBackMyGp).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe { finish() }

        RxView.clicks(tvChangeGpService).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this, AddGPServiceActivity::class.java))
                this.finish()
                overridePendingTransition(0,0)
            }

        RxView.clicks(noInternet.tvRetry).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                presenter.getGpService()
            }

        RxView.clicks(btnAddGpService).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this, AddGPServiceActivity::class.java))
                this.finish()
                overridePendingTransition(0,0)
            }

        locationPermission()
    }

    private fun locationPermission() {
        ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
    }

    override fun displayMessage(message: String) {
        val snackBar = Snackbar.make(flGpService,message, Snackbar.LENGTH_LONG)
        snackBar.changeFont()
        val snackView = snackBar.view
        snackView.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
        snackBar.show()
    }

    override fun getGpList(list: ArrayList<GpService>) {}

    override fun viewFullProgress(isShow: Boolean) {
        progress.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun emptySurgery() {
        rlGpService.visibility = View.GONE
        llMyGp.visibility = View.VISIBLE
    }

    override fun showProgress() {}

    override fun hideProgress() {}

    override fun noInternetConnection(isConnect: Boolean) {
        if (!isConnect)
        {
            rlSurgery.visibility=View.GONE
            noInternet.visibility=View.VISIBLE
            val snackBar = Snackbar.make(flGpService, R.string.no_internet, Snackbar.LENGTH_LONG)
            snackBar.changeFont()
            val snackView = snackBar.view
            snackView.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            snackBar.show()
        }
        else
        {
            rlSurgery.visibility=View.VISIBLE
            noInternet.visibility=View.GONE
        }
    }

    @SuppressLint("CheckResult")
    override fun showSurgery(surgery: Surgery?) {
          if (surgery?.practice_name.isNullOrEmpty())
          {
              rlGpService.visibility = View.GONE
              llMyGp.visibility = View.VISIBLE
          }else {
              rlGpService.visibility = View.VISIBLE
              llMyGp.visibility = View.GONE
          }

        supportMapFragment = supportFragmentManager.findFragmentById(R.id.mapwhere) as SupportMapFragment

        supportMapFragment.getMapAsync { googleMap ->
            map = googleMap

            val height = 135
            val width = 100
            val b = BitmapFactory.decodeResource(resources, R.drawable.ic_map_marker)
            val smallMarker = Bitmap.createScaledBitmap(b, width, height, false)
            val smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker)

            map.uiSettings.setAllGesturesEnabled(true)
            map.isMyLocationEnabled

            if (surgery?.lat != null && surgery.long !=null)
            {
                val marker = surgery.long.let { surgery.lat.let { it1 -> LatLng(it1, it) } }
                val cameraPosition = CameraPosition.Builder().target(marker).zoom(16.0f).build()
                map.addMarker(marker.let { it -> it.let { it1 -> MarkerOptions().position(it1) } }).setIcon(smallMarkerIcon)
                val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
                map.moveCamera(cameraUpdate)

                map.setOnMarkerClickListener { false }
            }
            else
            {
                val snackBar = Snackbar.make(flGpService, R.string.invalid_location, Snackbar.LENGTH_LONG)
                snackBar.changeFont()
                val snackView = snackBar.view
                snackView.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
                snackBar.show()
            }
        }

        tvAddress.text = surgery?.address?.let { capitalize(it+","+surgery.city+","+surgery.county) }
        tvName.text = surgery?.practice_name?.let { capitalize(it) }

        RxView.clicks(btnCallGPService).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CALL_PHONE), 1)
                } else {
                    startActivity(Intent(Intent.ACTION_CALL, Uri.fromParts("tel", surgery?.phone, null)))
                }
            }
       }

    private fun capitalize(capString: String): String? {
        val capBuffer = StringBuffer()
        val capMatcher: Matcher =
            Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString)
        while (capMatcher.find()) {
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase(Locale.ROOT) + capMatcher.group(2).toLowerCase(Locale.ROOT))
        }
        return capMatcher.appendTail(capBuffer).toString()
    }

    override fun onResume() {
        super.onResume()
        presenter.getGpService()
    }
}