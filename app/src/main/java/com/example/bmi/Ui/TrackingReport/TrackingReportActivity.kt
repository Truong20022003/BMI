package com.example.bmi.Ui.TrackingReport

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bmi.Base.BaseActivity
import com.example.bmi.Base.BaseViewModel
import com.example.bmi.R
import com.example.bmi.databinding.ActivityTrackingReportBinding

class TrackingReportActivity : BaseActivity<ActivityTrackingReportBinding, BaseViewModel>() {
    override fun createBinding() = ActivityTrackingReportBinding.inflate(layoutInflater)

    override fun setViewModel() = BaseViewModel()

    override fun initView() {
        super.initView()

    }
}