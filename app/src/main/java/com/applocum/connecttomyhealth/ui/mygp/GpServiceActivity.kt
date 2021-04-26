package com.applocum.connecttomyhealth.ui.mygp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_gp_service.*
import kotlinx.android.synthetic.main.custom_location.view.*


@Suppress("DEPRECATION", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class GpServiceActivity : BaseActivity(){
    private lateinit var map: GoogleMap
    private lateinit var supportMapFragment: SupportMapFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ivBack.setOnClickListener { finish() }

       supportMapFragment =
            supportFragmentManager.findFragmentById(R.id.mapwhere) as SupportMapFragment


        supportMapFragment.getMapAsync { googleMap ->
            map = googleMap

            map.uiSettings.setAllGesturesEnabled(true)
            map.isMyLocationEnabled
            val marker = LatLng(23.011360, 72.584310)
            val cameraPosition = CameraPosition.Builder().target(marker).zoom(15.0f).build()
            map.addMarker(MarkerOptions().position(marker).title("You are here!"))
            val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
            map.moveCamera(cameraUpdate)

            map.setOnInfoWindowClickListener {
                Toast.makeText(
                    this,
                    "Infowindow clicked",
                    Toast.LENGTH_SHORT
                ).show()
            }
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

        v.btnCallGPService.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}