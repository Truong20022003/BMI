package com.example.bmi.Ui.News

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bmi.Base.BaseActivity
import com.example.bmi.Base.BaseViewModel
import com.example.bmi.R
import com.example.bmi.databinding.ActivityNewsBinding

class NewsActivity : BaseActivity<ActivityNewsBinding, BaseViewModel>() {
    override fun createBinding() = ActivityNewsBinding.inflate(layoutInflater)
    override fun setViewModel() = BaseViewModel()
    override fun initView() {
        super.initView()

    }
}