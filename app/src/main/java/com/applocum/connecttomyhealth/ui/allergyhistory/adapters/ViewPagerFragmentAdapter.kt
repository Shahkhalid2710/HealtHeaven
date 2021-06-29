package com.applocum.connecttomyhealth.ui.allergyhistory.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter

class ViewPagerFragmentAdapter(var context: Context, fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    var list: ArrayList<Fragment> = ArrayList()
    private var titlelist: ArrayList<String> = ArrayList()

    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    fun addfragment(fragment: Fragment, title: String) {
        list.add(fragment)
        titlelist.add(title)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titlelist[position]
    }
}