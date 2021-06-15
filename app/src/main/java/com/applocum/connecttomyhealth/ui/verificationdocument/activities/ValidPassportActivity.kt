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
import com.bumptech.glide.Glide
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
    }

    private fun getRealPathFromURI(uri: Uri?): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor = managedQuery(uri, projection, null, null, null)
        val columnIndex: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(columnIndex)
    }
}