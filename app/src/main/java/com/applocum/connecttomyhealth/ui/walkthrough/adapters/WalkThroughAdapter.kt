package com.applocum.connecttomyhealth.ui.walkthrough.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.ui.walkthrough.models.SelectItem

class WalkThroughAdapter(context: Context,list:ArrayList<SelectItem>) :PagerAdapter() {
    var mContext=context
    var mList=list

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as LinearLayout
    }

    override fun getCount(): Int {
        return mList.size
    }

    @SuppressLint("InflateParams")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val layoutInflater:LayoutInflater= mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view=layoutInflater.inflate(R.layout.custom_onboarding_screen,null)
        val imageview:ImageView=view.findViewById(R.id.ivOnBoardScreen)
        val tvName:TextView=view.findViewById(R.id.tvOnBoardName)
        val tvDes:TextView=view.findViewById(R.id.tvOnBoardDes)

        imageview.setImageResource(mList[position].sImage)
        tvDes.text=mList[position].sDes
        tvName.text=mList[position].sName

        container.addView(view)
        return view
    }
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }
}