package com.applocum.connecttomyhealth.ui.verificationdocument.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_valid_passport.*
import kotlinx.android.synthetic.main.activity_valid_passport.ivBack
import java.io.ByteArrayOutputStream


class ValidPassportActivity : BaseActivity() {
    private val RequestPermissionCode = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ivBack.setOnClickListener { finish() }
        EnableRuntimePermission()

        rlCamera.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            startActivityForResult(intent, 7)
        }
    }

    override fun getLayoutResourceId(): Int=R.layout.activity_valid_passport

    fun EnableRuntimePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            Toast.makeText(this, "CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), RequestPermissionCode)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            RequestPermissionCode -> if (grantResults.isNotEmpty() && grantResults.get(0) == PackageManager.PERMISSION_GRANTED) {
                //
            } else {
                Toast.makeText(
                    this,
                    "Permission Canceled, Now your application cannot access CAMERA.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 7 && resultCode == Activity.RESULT_OK) {
            val bitmap=data?.extras?.get("data") as Bitmap
            val intent=Intent(this,ValidationValidPassportActivity::class.java)
            startActivity(intent)
        }
    }

    private fun imageViewToByte(image: ImageView): ByteArray? {
        val bitmap = (image.drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }
}