package com.applocum.connecttomyhealth.ui.addsymptoms

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.booksession.SessionBookActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_add_symptoms.*

class AddSymptomsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ivBack.setOnClickListener { finish() }

        val alldayDr = "<font color='#008976'>alldayDr</font>"
        val nHSGP = "<font color='#008976'>NHS GP.</font>"
        cbGeoLocation.text = HtmlCompat.fromHtml("I allow $alldayDr to access my geo Location. (This will only be used in an emergency)",HtmlCompat.FROM_HTML_MODE_LEGACY)
        cbRecords.text= HtmlCompat.fromHtml("I give consult to alldayDr to share my records with my $nHSGP",HtmlCompat.FROM_HTML_MODE_LEGACY)

        btnContinue.setOnClickListener {
                checkData()
        }
    }

    override fun getLayoutResourceId(): Int=R.layout.activity_add_symptoms

   private fun checkData()
    {
        if (cbGeoLocation.isChecked && cbRecords.isChecked)
        {
                startActivity(Intent(this,SessionBookActivity::class.java))
        }
        else
        {
            val snackbar = Snackbar.make(lladdsymptoms,"Please check both conditions", Snackbar.LENGTH_LONG)
            val snackview = snackbar.view
            snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
            snackbar.show()
        }
    }
}