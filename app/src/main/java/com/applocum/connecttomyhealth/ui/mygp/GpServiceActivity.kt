package com.applocum.connecttomyhealth.ui.mygp

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
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.mygp.models.GpService
import com.applocum.connecttomyhealth.ui.mygp.models.Surgery
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_gp_service.*
import kotlinx.android.synthetic.main.custom_mygp_xml.*
import kotlinx.android.synthetic.main.custom_progress.*
import javax.inject.Inject


@Suppress("DEPRECATION", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class GpServiceActivity : BaseActivity(),GpservicePresenter.View{

    @Inject
    lateinit var presenter: GpservicePresenter

    private lateinit var map: GoogleMap
    private lateinit var supportMapFragment: SupportMapFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).component.inject(this)
        presenter.injectview(this)

        ivBack.setOnClickListener { finish() }
        ivBackMyGp.setOnClickListener { finish() }

        tvChangeGpService.setOnClickListener {
            startActivity(Intent(this,AddGPServiceActivity::class.java))
            this.finish()
        }
         btnAddGpService.setOnClickListener {
             startActivity(Intent(this,AddGPServiceActivity::class.java))
         }

        locationPermission()
        presenter.getGpService()

    }

    override fun getLayoutResourceId(): Int = R.layout.activity_gp_service

    private fun locationPermission()
    {
        ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),1)
    }

    override fun displayMessage(message: String) {
    }

    override fun getGpList(list: ArrayList<GpService>) {
    }

    override fun viewProgress(isShow: Boolean) {
    }

    override fun viewFullProgress(isShow: Boolean) {
        progress.visibility=if (isShow)View.VISIBLE else View.GONE
    }

    override fun showSurgery(surgery: Surgery) {
        llMyGp.visibility=View.GONE
        llGpService.visibility=View.VISIBLE

        supportMapFragment = supportFragmentManager.findFragmentById(R.id.mapwhere) as SupportMapFragment

        supportMapFragment.getMapAsync { googleMap ->
            map = googleMap

              val height= 150
              val width = 150
              val b = BitmapFactory.decodeResource(resources, R.drawable.ic_marker)
              val smallMarker = Bitmap.createScaledBitmap(b, width, height, false)
              val smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker)

            map.uiSettings.setAllGesturesEnabled(true)
            map.isMyLocationEnabled
            val marker = LatLng(surgery.lat,surgery.long)
            val cameraPosition = CameraPosition.Builder().target(marker).zoom(15.0f).build()
            map.addMarker(MarkerOptions().position(marker).title(surgery.practice_name)).setIcon(smallMarkerIcon)
            val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
            map.moveCamera(cameraUpdate)

            map.setOnInfoWindowClickListener {}
            map.setOnMarkerClickListener {
                false
            }

        }

        tvAddress.text=surgery.address
        tvName.text=surgery.practice_name

        btnCallGPService.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CALL_PHONE), 1)
            } else {
                startActivity(Intent(Intent.ACTION_CALL, Uri.fromParts("tel",surgery.phone, null)))
            }
        }

    }

}