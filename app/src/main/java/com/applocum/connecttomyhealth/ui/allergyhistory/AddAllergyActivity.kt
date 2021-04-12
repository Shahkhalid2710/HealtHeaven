package com.applocum.connecttomyhealth.ui.allergyhistory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.MenuItem
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_add_allergy.*
import kotlinx.android.synthetic.main.activity_signup.*

class AddAllergyActivity : BaseActivity(), PopupMenu.OnMenuItemClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ivBack.setOnClickListener {
            finish()
        }

        etAddAllergy.setOnClickListener {
            val ctw = ContextThemeWrapper(this, R.style.CustomPopupTheme)
            val popupMenu = PopupMenu(ctw, etAddAllergy)
            popupMenu.menuInflater.inflate(R.menu.menu_allergy, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(this)
            popupMenu.show()
        }
    }

    override fun getLayoutResourceId(): Int =R.layout.activity_add_allergy

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.food_allerdy -> etAddAllergy.setText(R.string.food_allerdy_diet)
            R.id.dci_nycler_pleomorphim -> etAddAllergy.setText(R.string.dcis_nucler_pleomorphim)
            R.id.quilonia_ethiopica -> etAddAllergy.setText(R.string.quilonia_ethiopica)
            R.id.oncogene_protine_v_abc -> etAddAllergy.setText(R.string.oncogene_protine_v_abc)
            R.id.neoplasm_of_stomach -> etAddAllergy.setText(R.string.neoplasm_of_stomach)
            else -> {
                val snackbar =
                    Snackbar.make(llAddAllergy, "Please Add Allergy", Snackbar.LENGTH_LONG)
                val snackview = snackbar.view
                snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
                snackbar.show()
            }
        }
        return true
    }
}