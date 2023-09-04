package com.example.and3_4project

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.and3_4project.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: FragmentPageAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolBar
        setSupportActionBar(toolbar)

        val viewPager2 = binding.viewPager
        val tabLayout = binding.tabLayout

        adapter = FragmentPageAdapter(supportFragmentManager, lifecycle)
        viewPager2.adapter = adapter

        //탭설정
        tabLayout.addTab(tabLayout.newTab().setText("Contact"))
        tabLayout.addTab(tabLayout.newTab().setText("Mypage"))


        // TabLayout의 탭 선택 리스너 설정
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    viewPager2.currentItem = tab.position // 선택된 탭에 해당하는 페이지로 이동
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // 사용하지 않음
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // 사용하지 않음
            }
        })

        // ViewPager2의 페이지 변경 콜백 설정
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position)) // 페이지 변경 시 탭도 변경
            }
        })
    }
}