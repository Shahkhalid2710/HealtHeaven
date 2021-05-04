package com.applocum.connecttomyhealth.ui.mygp

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.mygp.models.GpService
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_gp_service.*
import kotlinx.android.synthetic.main.custom_location.view.*


@Suppress("DEPRECATION", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class GpServiceActivity : BaseActivity(){
    private lateinit var map: GoogleMap
    private lateinit var supportMapFragment: SupportMapFragment
    private lateinit var gpService: GpService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ivBack.setOnClickListener { finish() }
        tvChangeGpService.setOnClickListener { finish() }
        locationPermission()

        gpService=intent.getSerializableExtra("gpService") as GpService
        supportMapFragment = supportFragmentManager.findFragmentById(R.id.mapwhere) as SupportMapFragment

        supportMapFragment.getMapAsync { googleMap ->
            map = googleMap

            map.uiSettings.setAllGesturesEnabled(true)
            map.isMyLocationEnabled
            val marker = LatLng(gpService.lat,gpService.long)
            val cameraPosition = CameraPosition.Builder().target(marker).zoom(15.0f).build()
            map.addMarker(MarkerOptions().position(marker).title(gpService.practice_name))
            val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
            map.moveCamera(cameraUpdate)

            map.setOnInfoWindowClickListener {}
            map.setOnMarkerClickListener {
                openLocationDialog()
                false
            }

        }

     /*  val autocompleteFragment =
              supportFragmentManager.findFragmentById(R.id.place_autocomplete_fragment) as PlaceAutocompleteFragment

          autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
              override fun onPlaceSelected(place: Place) {

                 map.clear()
                  map.addMarker(
                      MarkerOptions().position(place.latLng).title(place.name.toString()))
                  map.moveCamera(CameraUpdateFactory.newLatLng(place.latLng))
                  map.animateCamera(CameraUpdateFactory.newLatLngZoom(place.latLng, 15.0f))
              }

              override fun onError(status: Status?) {

              }
          })
*/
    }


    override fun getLayoutResourceId(): Int = R.layout.activity_gp_service

   @SuppressLint("InflateParams")
   private fun openLocationDialog() {
        val dialog = BottomSheetDialog(this)
        val v: View = LayoutInflater.from(this).inflate(R.layout.custom_location,null)
        dialog.setCancelable(true)
        dialog.setContentView(v)
        BottomSheetBehavior.from(v.parent as View).state = BottomSheetBehavior.STATE_COLLAPSED
        v.tvAddress.text=gpService.address
        v.tvName.text=gpService.practice_name

        v.btnCallGPService.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CALL_PHONE),1)
            } else {
                startActivity(Intent(Intent.ACTION_CALL, Uri.fromParts("tel",gpService.phone,null )))
            }
            dialog.dismiss()
        }
        dialog.show()
    }

    fun locationPermission()
    {
        ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),1)
    }
}