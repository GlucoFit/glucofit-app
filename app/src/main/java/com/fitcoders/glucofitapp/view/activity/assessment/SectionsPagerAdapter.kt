package com.fitcoders.glucofitapp.view.activity.assessment

import HealthConditionFragment
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fitcoders.glucofitapp.view.fragment.lifestyle.LifeStyleFragment
import com.fitcoders.glucofitapp.view.fragment.userinformation.UserInformationFragment

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> UserInformationFragment()
            1 -> HealthConditionFragment()
            2 -> LifeStyleFragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}
