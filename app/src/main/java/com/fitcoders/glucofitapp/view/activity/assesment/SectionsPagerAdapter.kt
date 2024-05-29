package com.fitcoders.glucofitapp.view.activity.assesment

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fitcoders.glucofitapp.view.fragment.healthconditin.HealthConditionFragment
import com.fitcoders.glucofitapp.view.fragment.lifestyle.LifeStyleFragment
import com.fitcoders.glucofitapp.view.fragment.userinformation.UserInformationFragment

class SectionsPagerAdapter (activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = UserInformationFragment()
            1 -> fragment = HealthConditionFragment()
            2 -> fragment = LifeStyleFragment()
        }
        return fragment as Fragment
    }

}