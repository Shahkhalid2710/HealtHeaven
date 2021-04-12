package com.applocum.connecttomyhealth.ui.familyhistory

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
import kotlinx.android.synthetic.main.activity_add_family_history.*
import kotlinx.android.synthetic.main.activity_add_family_history.ivBack

class AddFamilyHistoryActivity : BaseActivity(), PopupMenu.OnMenuItemClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ivBack.setOnClickListener {
            finish()
        }
        etAddFamilyHistory.setOnClickListener {
            val ctw = ContextThemeWrapper(this, R.style.CustomPopupTheme)
            val popupMenu = PopupMenu(ctw, etAddFamilyHistory)
            popupMenu.menuInflater.inflate(R.menu.menu_family_history, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(this)
            popupMenu.show()
        }
    }

    override fun getLayoutResourceId(): Int =R.layout.activity_add_family_history

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.repair_of_joint_capsule_of_knee -> etAddFamilyHistory.setText(R.string.repair_of_joint_capsule_of_knee)
            R.id.knee_replacing -> etAddFamilyHistory.setText(R.string.knee_replacing)
            R.id.hemoglobin_manitaba -> etAddFamilyHistory.setText(R.string.hemoglobin_manitaba)
            R.id.neoplasm_of_stomach -> etAddFamilyHistory.setText(R.string.neoplasm_of_stomach)
            else -> {
                val snackbar =
                    Snackbar.make(lladdfamilyhistory, "Please Add Family History", Snackbar.LENGTH_LONG)
                val snackview = snackbar.view
                snackview.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
                snackbar.show()
            }
        }
        return true
    }
}