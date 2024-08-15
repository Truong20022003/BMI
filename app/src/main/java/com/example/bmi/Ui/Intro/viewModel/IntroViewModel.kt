package com.example.bmi.Ui.Intro.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bmi.R
import com.example.bmi.Ui.Intro.Model.IntroModel

class IntroViewModel : ViewModel() {
    private val intro = MutableLiveData<List<IntroModel>>()
    val listIntro: LiveData<List<IntroModel>> = intro

    fun getData() {
        val list = listOf(
            IntroModel(R.drawable.img_intro_1),
            IntroModel(R.drawable.img_intro_2),
            IntroModel(R.drawable.img_intro_3),
            IntroModel(R.drawable.img_intro_4)
        )

        intro.value = list

    }
}