package com.applocum.connecttomyhealth.ui.allergyhistory

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

@Suppress("DEPRECATION")
class ViewPagerFragmentAdapter(var context: Context, fm: FragmentManager): FragmentPagerAdapter(fm) {
    var list:ArrayList<Fragment> = ArrayList()
    private var titlelist:ArrayList<String> = ArrayList()

    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    override fun getCount(): Int {
        return list.size
    }

    fun addfragment(fragment: Fragment, title:String)
    {
        list.add(fragment)
        titlelist.add(title)
    }
    override fun getPageTitle(position: Int): CharSequence? {
        return titlelist[position]
    }
}