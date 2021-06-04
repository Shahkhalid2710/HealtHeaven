package com.applocum.connecttomyhealth.ui.verificationdocument.activities

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_valid_passport.*
import java.io.FileNotFoundException
import java.io.IOException


class ValidPassportActivity : BaseActivity() {
    private val requestCodee = 1
    private var imageUri: Uri? = null

    override fun getLayoutResourceId(): Int = R.layout.activity_valid_passport

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ivBack.setOnClickListener { finish() }
        enableRuntimePermission()

        rlCamera.setOnClickListener {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.TITLE, "MyPicture")
            values.put(MediaStore.Images.Media.DESCRIPTION, "Photo taken on " + System.currentTimeMillis())
            imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            startActivityForResult(intent, 0)
        }
    }

    private fun enableRuntimePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            Toast.makeText(this, "CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), requestCodee)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            requestCodee -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //
            } else {
                Toast.makeText(this, "Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            try {
                val imagepath = getRealPathFromURI(imageUri)
                val intent = Intent(this, ValidationValidPassportActivity::class.java)
                intent.putExtra("image", imagepath)
                startActivity(intent)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun getRealPathFromURI(uri: Uri?): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor = managedQuery(uri, projection, null, null, null)
        val columnIndex: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(columnIndex)
    }
}