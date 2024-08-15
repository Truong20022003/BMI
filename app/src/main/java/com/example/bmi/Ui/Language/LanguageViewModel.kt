package com.example.bmi.Ui.Language

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bmi.Base.BaseViewModel
import com.example.bmi.Model.LanguageModel
import com.example.bmi.R
import com.example.bmi.Utils.SystemUtils
import java.util.Locale

class LanguageViewModel: BaseViewModel() {

    val languages = MutableLiveData<List<LanguageModel>>()
    val selectedLanguage = MutableLiveData<LanguageModel>()

    fun setLocale(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }

    private val _langDevice = MutableLiveData<String>()
    val langDevice: LiveData<String>
        get() = _langDevice

    private val _codeLang = MutableLiveData<String>()
    val codeLang: LiveData<String>
        get() = _codeLang

    fun languageStart(context: Context) {
        var langDevice = "en"
        var codeLang = "en"
        val listLanguage = mutableListOf<LanguageModel>()

        listLanguage.add(LanguageModel("English", "en", false, R.drawable.frag_eng))
        listLanguage.add(LanguageModel("Hindi", "hi", false, R.drawable.frag_india))
        listLanguage.add(LanguageModel("Spanish", "es", false, R.drawable.frag_spanish))
        listLanguage.add(LanguageModel("French", "fr", false, R.drawable.frag_french))
        listLanguage.add(LanguageModel("Portuguese", "pt", false, R.drawable.frag_portu))
        listLanguage.add(LanguageModel("Indonesian", "in", false, R.drawable.frag_indo))
        listLanguage.add(LanguageModel("German", "de", false, R.drawable.frag_germany))


        _langDevice.postValue(langDevice)
        _codeLang.postValue(codeLang)
        languages.postValue(listLanguage)
    }

    fun languageSetting(context: Context) {

        var langDevice = "en"
        var codeLang = "en"
        val listLanguage = mutableListOf<LanguageModel>()

        listLanguage.add(LanguageModel("English", "en", false, R.drawable.frag_eng))
        listLanguage.add(LanguageModel("Hindi", "hi", false, R.drawable.frag_india))
        listLanguage.add(LanguageModel("Spanish", "es", false, R.drawable.frag_spanish))
        listLanguage.add(LanguageModel("French", "fr", false, R.drawable.frag_french))
        listLanguage.add(LanguageModel("Portuguese", "pt", false, R.drawable.frag_portu))
        listLanguage.add(LanguageModel("Indonesian", "in", false, R.drawable.frag_indo))
        listLanguage.add(LanguageModel("German", "de", false, R.drawable.frag_germany))


        // Lấy ngôn ngữ được chọn từ SystemUtils
        val preferredLanguage = SystemUtils.getPreLanguage(context)

        // Tìm và di chuyển ngôn ngữ được chọn lên đầu danh sách
        val selectedLanguageIndex = listLanguage.indexOfFirst { it.code == preferredLanguage }
        if (selectedLanguageIndex != -1) {
            val selectedLanguage = listLanguage.removeAt(selectedLanguageIndex).copy(active = true)
            listLanguage.add(0, selectedLanguage)
        }

        // Cập nhật giá trị langDevice và codeLang
        langDevice = preferredLanguage.toString()
        codeLang = preferredLanguage.toString()

        // Cập nhật danh sách: Đảm bảo chỉ có item được chọn có active = true
        listLanguage.forEachIndexed { index, language ->
            if (index != 0) {
                listLanguage[index] = language.copy(active = false)
            }
        }

        // Post giá trị lên LiveData
        _langDevice.postValue(langDevice)
        _codeLang.postValue(codeLang)
        languages.postValue(listLanguage)

        Log.d("VIETT", "$listLanguage")


    }

    fun setSelectedLanguage(context: Context, language: LanguageModel) {
        selectedLanguage.value = language
        _codeLang.postValue(language.code)
        saveSelectedLanguage(context, language.code)
    }

    private fun saveSelectedLanguage(context: Context, languageCode: String) {
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("selected_language", languageCode).apply()
    }
}