package com.applocum.connecttomyhealth.ui.medicalhistory

import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.MenuItem
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_add_medical_history.*
import kotlinx.android.synthetic.main.activity_add_medical_history.ivBack

class AddMedicalHistoryActivity : BaseActivity(), PopupMenu.OnMenuItemClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ivBack.setOnClickListener { finish() }

        etDiseaseName.setOnClickListener {
            val ctw = ContextThemeWrapper(this, R.style.CustomPopupTheme)
            val popupMenu = PopupMenu(ctw, etDiseaseName)
            popupMenu.menuInflater.inflate(R.menu.menu_medical_history, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(this)
            popupMenu.show()
        }
    }

    override fun getLayoutResourceId(): Int = R.layout.activity_add_medical_history

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.neoplasm_of_abducens_nerve -> etDiseaseName.setText(R.string.neoplasm_of_abducens_nerve)
            R.id.neoplasm_of_junctional_region_of_epiglottis -> etDiseaseName.setText(R.string.neoplasm_of_junctional_region_of_epiglottis)
            R.id.jugular_lymphadenopathy -> etDiseaseName.setText(R.string.jugular_lymphadenopathy)
            R.id.oncogene_protine_v_abc -> etDiseaseName.setText(R.string.oncogene_protine_v_abc)
            else -> {
                val snackbar =
                    Snackbar.make(llMedicalHistory, "Please Add Disease Name", Snackbar.LENGTH_LONG)
                val snackview = snackbar.view
                snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
                snackbar.show()
            }
        }
        return true
    }
}