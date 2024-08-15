package com.example.bmi.Ui.Language

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bmi.Base.BaseActivity
import com.example.bmi.Model.LanguageModel
import com.example.bmi.R
import com.example.bmi.Ui.Home.HealthyCheck
import com.example.bmi.Ui.Intro.IntroActivity
import com.example.bmi.Utils.SystemUtils
import com.example.bmi.databinding.ActivityLanguageSettingBinding
import com.example.bmi.databinding.ActivityLanguageStartBinding
import com.example.bmi.view.tap
import java.util.Locale

class LanguageSettingActivity : BaseActivity<ActivityLanguageSettingBinding, LanguageViewModel>() {

    private var adapter: LanguageAdapter? = null

    override fun createBinding(): ActivityLanguageSettingBinding {
        return ActivityLanguageSettingBinding.inflate(layoutInflater)
    }

    override fun setViewModel(): LanguageViewModel {
        return LanguageViewModel()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun initView() {
        super.initView()

        viewModel = LanguageViewModel()

        adapter = LanguageAdapter(this, emptyList()) { language ->
            viewModel.setSelectedLanguage(this, language)
        }

        binding.rcvLanguageStart.layoutManager = LinearLayoutManager(this)
        binding.rcvLanguageStart.adapter = adapter
        viewModel.languages.observe(this) { languages ->
            adapter?.updateData(languages)
        }


        viewModel.selectedLanguage.observe(this) { selectedLanguage ->
            adapter?.setSelectedLanguage(selectedLanguage)
        }
        // Initialize data
        viewModel.languageSetting(this)


        binding.imgSave.tap {
            val selectedLanguage = viewModel.selectedLanguage.value
            if (selectedLanguage != null) {
                viewModel.setLocale(this, selectedLanguage.code)
                SystemUtils.saveLocale(this, selectedLanguage.code)
            }
            startActivity(Intent(this, HealthyCheck::class.java))
            finish()
        }

        binding.ivBack.tap {
            finish()
        }

    }


}