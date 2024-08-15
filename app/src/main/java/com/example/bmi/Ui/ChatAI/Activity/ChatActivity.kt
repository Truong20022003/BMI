package com.example.bmi.Ui.ChatAI.Activity


import android.content.Intent
import android.os.Handler
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.viewpager2.widget.ViewPager2
import com.example.bmi.Base.BaseActivity
import com.example.bmi.R
import com.example.bmi.Ui.ChatAI.Adapter.MainChatAdapter
import com.example.bmi.databinding.ActivityChatBinding
import com.example.bmi.Ui.ChatAI.ViewModel.ChatActiviyViewModel
import com.example.bmi.Ui.Home.HealthyCheck
import com.example.bmi.view.tap
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatActivity : BaseActivity<ActivityChatBinding, ChatActiviyViewModel>() {
    //    private var adView: AdView? = null
    private var handler: Handler? = null
    private lateinit var runnable: Runnable
    override fun createBinding(): ActivityChatBinding {
        return ActivityChatBinding.inflate(layoutInflater)
    }

    override fun setViewModel(): ChatActiviyViewModel {
        return ChatActiviyViewModel()
    }

    override fun initView() {
        super.initView()


        val mainAdapter = MainChatAdapter(this)
        binding.viewPager.adapter = mainAdapter
        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.offscreenPageLimit = 3

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {

                    0 -> {
                        binding.tvTitle.text = getString(R.string.ai_assistant)
                        binding.ivHistory.visibility = View.VISIBLE
                    }

                    1 -> {
                        binding.tvTitle.text = getString(R.string.history)
                        binding.ivHistory.visibility = View.GONE
                    }

                }
            }
        })


        binding.ivHistory.setOnClickListener() {
            binding.viewPager.currentItem = 1
        }

        binding.ivBack.tap {
            if (binding.viewPager.currentItem == 0) {
                finish()
            } else {
                binding.viewPager.currentItem = 0
            }
        }

        binding.ivHome.tap {
            startActivity(Intent(this, HealthyCheck::class.java))
            finish()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.viewPager.currentItem == 0) {
                    finish()
                } else {
                    binding.viewPager.currentItem = 0
                }
            }
        })

    }

}