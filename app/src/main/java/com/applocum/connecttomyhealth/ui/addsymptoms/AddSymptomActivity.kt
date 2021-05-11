package com.applocum.connecttomyhealth.ui.addsymptoms

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.applocum.connecttomyhealth.MyApplication
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.shareddata.endpoints.UserHolder
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.booksession.SessionBookActivity
import com.applocum.connecttomyhealth.ui.specialists.models.Specialist
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_add_symptom.*
import java.io.InputStream
import javax.inject.Inject


class AddSymptomActivity : BaseActivity() {
    private val requestCodeGallery= 999
    private var selectedImagePath:String=""

    @Inject
    lateinit var userHolder: UserHolder

    override fun getLayoutResourceId(): Int=R.layout.activity_add_symptom

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as MyApplication).component.inject(this)
        val specialist=intent.getSerializableExtra("specialist") as Specialist

        val alldayDr = "<font color='#008976'>alldayDr</font>"
        val nhsGP = "<font color='#008976'>NHS GP.</font>"
        cbGeoLocation.text = HtmlCompat.fromHtml(
            "I allow $alldayDr to access my geo Location. (This will only be used in an emergency)",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        cbRecords.text = HtmlCompat.fromHtml(
            "I give consult to alldayDr to share my records with my $nhsGP",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )


        ivAddSymptomImage.setOnClickListener {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                requestCodeGallery
            )
        }

        btnContinue.setOnClickListener {
            if (cbGeoLocation.isChecked && cbRecords.isChecked && checkSymptoms(etAddSymptoms.text.toString(),selectedImagePath)) {
                val intent = Intent(this, SessionBookActivity::class.java)
                intent.putExtra("specialist",specialist)
                val appointment = userHolder.getBookAppointmentData()
                appointment.pickedFilePath = selectedImagePath
                appointment.appointmentReason = etAddSymptoms.text.toString()
                appointment.allowGeoAccess=cbGeoLocation.isChecked
                appointment.sharedRecordWithNhs=cbRecords.isChecked
                userHolder.saveBookAppointmentData(appointment)
                startActivity(intent)
            } else {
                val snackbar = Snackbar.make(lladdsymptoms, "Please fill all data", Snackbar.LENGTH_LONG)
                val snackview = snackbar.view
                snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
                snackbar.show()
            }
        }
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode==requestCodeGallery)
        {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                val intent=Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent,requestCodeGallery)
            }else{
                Toast.makeText(this,"You don't have permission",Toast.LENGTH_LONG).show()
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode==requestCodeGallery && resultCode == Activity.RESULT_OK && data !=null)
        {
            val uri=data.data
            val inputStream : InputStream = uri.let { it?.let { it1 ->
                contentResolver.openInputStream(
                    it1
                )
            } }!!
            val bitmap=BitmapFactory.decodeStream(inputStream)
            ivSymptom.setImageBitmap(bitmap)
            selectedImagePath = getRealPathFromURI(uri)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun getRealPathFromURI(uri: Uri?): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor = managedQuery(uri, projection, null, null, null)
        val columnIndex: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(columnIndex)
    }

    private fun checkSymptoms(reason:String,image:String):Boolean
    {
        if (reason.isEmpty())
        {
            val snackbar = Snackbar.make(lladdsymptoms,"Please enter symptoms", Snackbar.LENGTH_LONG)
            val snackview = snackbar.view
            snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
            snackbar.show()
            return false
        }
        if (image.isEmpty())
        {
            val snackbar = Snackbar.make(lladdsymptoms,"Please add image", Snackbar.LENGTH_LONG)
            val snackview = snackbar.view
            snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
            snackbar.show()
            return false
        }
        return true
    }
}