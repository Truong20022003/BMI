package com.example.bmi.Ui.Language

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bmi.Base.BaseActivity
import com.example.bmi.Model.LanguageModel
import com.example.bmi.Ui.Home.HealthyCheck
import com.example.bmi.Ui.Intro.IntroActivity
import com.example.bmi.Ui.Permission.PermissionActivity
import com.example.bmi.Utils.SystemUtils
import com.example.bmi.databinding.ActivityLanguageStartBinding
import java.util.Locale

class LanguageStartActivity : BaseActivity<ActivityLanguageStartBinding, LanguageViewModel>() {

    private var adapter: LanguageAdapter? = null
    override fun createBinding() = ActivityLanguageStartBinding.inflate(layoutInflater)

    override fun setViewModel() = LanguageViewModel()

    override fun initView() {
        super.initView()
        restoreLocale()
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
            updateSaveButtonVisibility(selectedLanguage)
        }
        // Initialize data
        viewModel.languageStart(this)


        binding.imgSave.setOnClickListener {
            val selectedLanguage = viewModel.selectedLanguage.value
            if (selectedLanguage != null) {
                viewModel.setLocale(this, selectedLanguage.code)
                SystemUtils.saveLocale(this,selectedLanguage.code)

                    startActivity(Intent(this, IntroActivity::class.java))
                    finish()

            } else {
                Toast.makeText(
                    this,
                    "aaaa",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    private fun restoreLocale() {
        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val languageCode = sharedPreferences.getString("selected_language", "en")
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    private fun updateSaveButtonVisibility(selectedLanguage: LanguageModel?) {
        binding.imgSave.visibility = if (selectedLanguage != null) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

}