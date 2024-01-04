package com.example.myapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.R
import com.example.myapplication.SectionsPagerAdapter
import com.example.myapplication.databinding.ActivityOrdersBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class OrdersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrdersBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewPager: ViewPager2 = binding.vpOrderStatus
        val sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = sectionsPagerAdapter

        val tabLayout: TabLayout = binding.tbLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Unpaid"
                1 -> tab.text = "Processed"
                2 -> tab.text = "Shipped"
            }
        }.attach()
    }
}