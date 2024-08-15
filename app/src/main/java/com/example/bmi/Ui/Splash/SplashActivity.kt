package com.example.bmi.Ui.Splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import com.example.bmi.Base.BaseActivity
import com.example.bmi.Base.BaseViewModel
import com.example.bmi.MainActivity
import com.example.bmi.Ui.Home.HealthyCheck
import com.example.bmi.Ui.Language.LanguageStartActivity
import com.example.bmi.Ui.Settings.SettingActivity
import com.example.bmi.databinding.ActivitySplashBinding

class SplashActivity : BaseActivity<ActivitySplashBinding, BaseViewModel>() {

    private val handler = Handler(Looper.getMainLooper())
    override fun createBinding() = ActivitySplashBinding.inflate(layoutInflater)
    override fun setViewModel() = BaseViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        Thread(Runnable {
            for (progress in 0..100) {
                handler.post {
                    binding.pbSplash.progress = progress
                    binding.tvPersent.text = "$progress%"
                }
                try {
                    Thread.sleep(20)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                if (progress == 100) {
                    startActivity(Intent(this, LanguageStartActivity::class.java))
                    finish()
                }
            }
        }).start()

    }

    override fun initView() {
        super.initView()
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        })
    }
}