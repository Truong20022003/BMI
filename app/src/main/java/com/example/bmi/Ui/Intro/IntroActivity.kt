package com.example.bmi.Ui.Intro

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.bmi.Base.BaseActivity
import com.example.bmi.Base.BaseViewModel
import com.example.bmi.R
import com.example.bmi.Ui.Home.HealthyCheck
import com.example.bmi.Ui.Intro.adapter.IntroAdapter
import com.example.bmi.Ui.Intro.viewModel.IntroViewModel
import com.example.bmi.Ui.Permission.PermissionActivity
import com.example.bmi.databinding.ActivityIntroBinding

class IntroActivity : BaseActivity<ActivityIntroBinding, IntroViewModel>() {

    private var introAdapter: IntroAdapter? = null

    override fun createBinding(): ActivityIntroBinding {
        return ActivityIntroBinding.inflate(layoutInflater)
    }

    override fun setViewModel(): IntroViewModel {
        return IntroViewModel()
    }


    override fun viewModel() {
        super.viewModel()
        viewModel.getData()
        viewModel.listIntro.observe(this) {
            introAdapter?.list = it
            introAdapter = IntroAdapter(this, it)
            binding.viewPagerIntro.offscreenPageLimit = 5
            binding.viewPagerIntro.adapter = introAdapter
            introAdapter?.notifyDataSetChanged()
        }
    }

    override fun initView() {
        super.initView()
        binding.viewPagerIntro.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> {
                        binding.ads.visibility = View.VISIBLE
                        binding.viewIntro.visibility = View.GONE
                        binding.tvTitle.text = getString(R.string.title_intro_1)
                        binding.tvContent.text = getString(R.string.content_intro_1)
                        dotDefault()
                        binding.dot1.setImageResource(R.drawable.dot_select)
                    }
                    1 -> {
                        binding.ads.visibility = View.GONE
                        binding.viewIntro.visibility = View.VISIBLE
                        binding.tvTitle.text = getString(R.string.title_intro_2)
                        binding.tvContent.text = getString(R.string.content_intro_2)
                        dotDefault()
                        binding.dot2.setImageResource(R.drawable.dot_select)
                    }
                    2 -> {
                        binding.ads.visibility = View.GONE
                        binding.viewIntro.visibility = View.VISIBLE
                        binding.tvTitle.text = getString(R.string.title_intro_3)
                        binding.tvContent.text = getString(R.string.content_intro_3)
                        dotDefault()
                        binding.dot3.setImageResource(R.drawable.dot_select)
                    }
                    3 -> {
                        binding.ads.visibility = View.VISIBLE
                        binding.viewIntro.visibility = View.GONE
                        binding.tvTitle.text = getString(R.string.title_intro_4)
                        binding.tvContent.text = getString(R.string.content_intro_4)
                        dotDefault()
                        binding.dot4.setImageResource(R.drawable.dot_select)
                    }
                }
            }
        })

        binding.tvNext.setOnClickListener {
            if (binding.viewPagerIntro.currentItem >= 3) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                    startActivity(Intent(this, PermissionActivity::class.java))
                    finish()
                } else {
                    startActivity(Intent(this, HealthyCheck::class.java))
                    finish()
                }

            } else {
                binding.viewPagerIntro.currentItem = binding.viewPagerIntro.currentItem + 1
            }
        }
    }

    private fun dotDefault() {
        binding.dot1.setImageResource(R.drawable.dot_not_select)
        binding.dot2.setImageResource(R.drawable.dot_not_select)
        binding.dot3.setImageResource(R.drawable.dot_not_select)
        binding.dot4.setImageResource(R.drawable.dot_not_select)

    }

}