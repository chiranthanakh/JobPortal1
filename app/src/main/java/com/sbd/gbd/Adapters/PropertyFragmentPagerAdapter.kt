package com.sbd.gbd.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sbd.gbd.Activities.Propertys.PropertyGlandFragment
import com.sbd.gbd.Activities.Propertys.PropertyHomesFragment
import com.sbd.gbd.Activities.Propertys.PropertyLayoutFragment
import com.sbd.gbd.Activities.Propertys.PropertySitesFragment

class PropertyFragmentPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle)
{

    // List of fragments
    private val fragments = listOf(
        PropertySitesFragment(),
        PropertyHomesFragment(),
        PropertyGlandFragment(),
        PropertyLayoutFragment()
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}