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


class AddMedicalHistoryActivity : BaseActivity(), PopupMenu.OnMenuItemClickListener {

    override fun getLayoutResourceId(): Int = R.layout.activity_add_medical_history
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

        etSelectStartDateMonth.setOnClickListener {
            val ctw = ContextThemeWrapper(this, R.style.CustomPopupTheme)
            val popupMenu = PopupMenu(ctw, etSelectStartDateMonth)
            popupMenu.menuInflater.inflate(R.menu.menu_month, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(this)
            popupMenu.show()
        }

        etSelectEndDateMonth.setOnClickListener {
            val ctw = ContextThemeWrapper(this, R.style.CustomPopupTheme)
            val popupMenu = PopupMenu(ctw, etSelectEndDateMonth)
            popupMenu.menuInflater.inflate(R.menu.menu_month, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(this)
            popupMenu.show()
        }
    }


    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.neoplasm_of_abducens_nerve -> etDiseaseName.setText(R.string.neoplasm_of_abducens_nerve)
            R.id.neoplasm_of_junctional_region_of_epiglottis -> etDiseaseName.setText(R.string.neoplasm_of_junctional_region_of_epiglottis)
            R.id.jugular_lymphadenopathy -> etDiseaseName.setText(R.string.jugular_lymphadenopathy)
            R.id.oncogene_protine_v_abc -> etDiseaseName.setText(R.string.oncogene_protine_v_abc)
        }

       /* when(item?.itemId)
        {
            R.id.january->etSelectStartDateMonth.setText(R.string.january)
            R.id.february->etSelectStartDateMonth.setText(R.string.february)
            R.id.march->etSelectStartDateMonth.setText(R.string.march)
            R.id.april->etSelectStartDateMonth.setText(R.string.april)
            R.id.may->etSelectStartDateMonth.setText(R.string.may)
            R.id.june->etSelectStartDateMonth.setText(R.string.june)
            R.id.july->etSelectStartDateMonth.setText(R.string.july)
            R.id.auguest->etSelectStartDateMonth.setText(R.string.auguest)
            R.id.september->etSelectStartDateMonth.setText(R.string.september)
            R.id.october->etSelectStartDateMonth.setText(R.string.october)
            R.id.november->etSelectStartDateMonth.setText(R.string.november)
            R.id.december->etSelectStartDateMonth.setText(R.string.december)

        }*/

       /* when(item?.itemId)
        {
            R.id.january->etSelectEndDateMonth.setText(R.string.january)
            R.id.february->etSelectEndDateMonth.setText(R.string.february)
            R.id.march->etSelectEndDateMonth.setText(R.string.march)
            R.id.april->etSelectEndDateMonth.setText(R.string.april)
            R.id.may->etSelectEndDateMonth.setText(R.string.may)
            R.id.june->etSelectEndDateMonth.setText(R.string.june)
            R.id.july->etSelectEndDateMonth.setText(R.string.july)
            R.id.auguest->etSelectEndDateMonth.setText(R.string.auguest)
            R.id.september->etSelectEndDateMonth.setText(R.string.september)
            R.id.october->etSelectEndDateMonth.setText(R.string.october)
            R.id.november->etSelectEndDateMonth.setText(R.string.november)
            R.id.december->etSelectEndDateMonth.setText(R.string.december)

        }*/
        return true
    }
}