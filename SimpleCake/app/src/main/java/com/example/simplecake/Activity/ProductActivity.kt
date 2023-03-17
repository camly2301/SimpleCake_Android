package com.example.simplecake.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.simplecake.LoadingDialog
import com.example.simplecake.R
import com.example.simplecake.adapter.ViewPagerAdapter
import com.example.simplecake.adapter.ViewPagerAdminAdapter
import com.google.android.material.tabs.TabLayout

class ProductActivity : AppCompatActivity() {
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var tabLayout: TabLayout
    lateinit var mViewpager: ViewPager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        initUI()
        //load Dialog
        val loadingDialog= LoadingDialog(this)
        loadingDialog.startLoadingDialog()
        val handler: Handler = Handler()
        handler.postDelayed(object :Runnable{
            override fun run() {
                loadingDialog.dismissDialog()
            }
        },3000)

        //end load
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val viewPagerAdapter= ViewPagerAdminAdapter(supportFragmentManager, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        mViewpager.adapter=viewPagerAdapter
        tabLayout.setupWithViewPager(mViewpager)

    }

    private fun initUI() {
        toolbar=findViewById(R.id.tool_bar)
        tabLayout=findViewById(R.id.tab_layout)
        mViewpager=findViewById(R.id.viewPager)
    }

}