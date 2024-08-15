package com.example.bmi.Ui.Home

import android.content.Intent
import com.example.bmi.Base.BaseActivity
import com.example.bmi.Base.BaseViewModel
import com.example.bmi.Ui.BmiCalculator.BmiCalculatorActivity
import com.example.bmi.Ui.ChatAI.Activity.ChatActivity
import com.example.bmi.Ui.Settings.SettingActivity
import com.example.bmi.Ui.TrackingReport.TrackingReportActivity
import com.example.bmi.databinding.ActivityHealthyCheckBinding
import com.example.bmi.view.tap

class HealthyCheck : BaseActivity<ActivityHealthyCheckBinding, BaseViewModel>() {
    override fun createBinding() = ActivityHealthyCheckBinding.inflate(layoutInflater)

    override fun setViewModel() = BaseViewModel()

    override fun initView() {
        super.initView()
        binding.llAissistant.tap {
            val intent = Intent(this, ChatActivity::class.java)
            startActivity(intent)
        }

        binding.ivSetting.tap {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }
        binding.llBmiCalculator.tap {
            val intent = Intent(this, BmiCalculatorActivity::class.java)
            startActivity(intent)
        }
        binding.llTrackingreport.tap {
            val intent = Intent(this, TrackingReportActivity::class.java)
            startActivity(intent)
        }

    }
}